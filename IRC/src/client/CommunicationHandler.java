package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import org.ini4j.Ini;

import server.DataBaseManager;

public class CommunicationHandler implements Runnable {
	private Socket m_sock = null;
	public static final int TIMEOUT = 5000;
	private StateListener.State m_state=StateListener.State.INITIAL;
	private ArrayList<StateListener> m_stateListeners = new ArrayList<StateListener>();
	private ArrayList<ComListener> m_comListeners = new ArrayList<ComListener>();
	private boolean m_quit = false;
	private boolean m_isAdmin=false;
	private int m_reconIndex = 0;
	private String m_reconPseudo;
	
	public boolean get_isAdmin() {
		return m_isAdmin;
	}
	
	public void tryRecon(String pseudo){
		m_reconPseudo = pseudo;
	}

	private void interpret(String trame){
		if(this.getState()==StateListener.State.CONNECTING){
			if(trame.startsWith("!n")){
				if(trame.contains("loginError")){
					this.disconnect();
					for(ComListener l : m_comListeners){
						synchronized(l){
							l.Error("This nickname is already taken by a logged-in user");
						}
					}
				}
				else if(trame.contains("mdpError")){
					this.disconnect();
					for(ComListener l : m_comListeners){
						synchronized(l){
							l.Error("Wrong password");
						}
					}
				}
			}else if(trame.startsWith("!o")){
				this.changeState(StateListener.State.CONNECTED);
			}else if(trame.startsWith("!a")){
				m_isAdmin=true;
				this.changeState(StateListener.State.CONNECTED);
			}
		}else if(this.getState()==StateListener.State.CONNECTED){
			for(ComListener l : m_comListeners){
				synchronized(l){
					l.onTrameReceived(trame);
				}
			}
		}
	}
	
	
	public synchronized void stop(){
		m_quit=true;
		disconnect();
	}
	
	private void changeState(StateListener.State new_state){
		this.m_state=new_state;
		for(StateListener l : m_stateListeners){
			synchronized(l){
				l.onStateChanged(new_state);
			}
		}
	}
	
	public synchronized StateListener.State getState(){
		return m_state;
	}
	
	public CommunicationHandler(){
		this.m_sock = new Socket();
		this.m_state=StateListener.State.INITIAL;
	}
	
	public synchronized void addStateListener(StateListener listener){
		m_stateListeners.add(listener);
	}
	
	public synchronized void addComListener(ComListener listener){
		m_comListeners.add(listener);
	}
	
	public synchronized void configure(String ipaddr, int port){
		if(this.getState()==StateListener.State.CONNECTED)
		{
			this.disconnect();
		}
		try {
			this.m_sock=new Socket(ipaddr, port);
			this.changeState(StateListener.State.CONNECTING);
		} catch (IOException e) {
			for(ComListener l : m_comListeners){
				synchronized(l){
					l.Error("Connection failed");
				}
			}
			this.m_sock=new Socket();
			if(m_state != StateListener.State.RECONNECTING){
				this.changeState(StateListener.State.INITIAL);
			}
		}
	}
	
	public synchronized void disconnect(){
		this.changeState(StateListener.State.DISCONNECTING);
		m_isAdmin=false;
		try {
			this.m_sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.m_sock=new Socket();
			this.changeState(StateListener.State.INITIAL);
		}
	}
	
	public synchronized void post(String msg){
		PrintWriter out = null;
		try {
			out = new PrintWriter(m_sock.getOutputStream(), true);
			out.println(msg);
		} catch (IOException e) {
			for(ComListener l : m_comListeners){
				synchronized(l){
					l.Error("Sending could not be done");
				}
			}
		}
	}
	
	public synchronized void connexionAdmin(String pseudo, String mdp){
		this.post("+a"+pseudo+"|"+mdp);
		
	}
	
	
	
	
	
	@Override
	public void run() {
		while(!m_quit){
			if(this.getState() == StateListener.State.CONNECTING || this.getState() == StateListener.State.CONNECTED || this.getState() == StateListener.State.DISCONNECTING){
				String line = null;
				BufferedReader in = null;
				try{
					in = new BufferedReader(new InputStreamReader(m_sock.getInputStream()));
				} catch (IOException e) {
					this.disconnect();
				}

				try{
					while(this.getState() == StateListener.State.CONNECTING || this.getState() == StateListener.State.CONNECTED || this.getState() == StateListener.State.DISCONNECTING){
						line = in.readLine();
						if(line==null){
							this.disconnect();
						}else{
							this.interpret(line);
						}
					}
				}catch (IOException e) {
					if(m_state != StateListener.State.DISCONNECTING && m_state != StateListener.State.CONNECTING){
						changeState(StateListener.State.RECONNECTING);
						
						try {
							Ini inifile = new Ini(new File("config.ini"));
							Ini.Section dbsection = inifile.get("database");
							String db_hostname = dbsection.get("hostname");
							int db_port = dbsection.get("port", int.class);
							String db_name = dbsection.get("name");
							
							DataBaseManager db = new DataBaseManager(db_hostname, db_port, db_name);
							ArrayList<DataBaseManager.ServerCoord> serverlist = db.getServerList();
							for(int i=0; i<24; ++i){
								Thread.sleep(5000);
								configure(serverlist.get(m_reconIndex).getHostname(), serverlist.get(m_reconIndex).getClientPort());
								if(++m_reconIndex >= serverlist.size()) m_reconIndex=0;
								if(m_state == StateListener.State.CONNECTING){
									m_reconIndex = 0;
									post("+u"+m_reconPseudo);
									i=24;
								}
							}
						} catch (ClassNotFoundException | IOException | SQLException | InterruptedException e1) {
						}
					}
				}
			}
		}
		this.disconnect();
	}
	
	protected void finalize(){
		try{
			if(m_sock != null){
				m_sock.close();
			}
		} catch (IOException e) {
		}
	}
	
	
}
