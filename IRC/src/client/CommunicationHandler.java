package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class CommunicationHandler implements Runnable {
	private Socket m_sock = null;
	public static final int TIMEOUT = 5000;
	private StateListener.State m_state=StateListener.State.INITIAL;
	private ArrayList<StateListener> m_stateListeners = new ArrayList<StateListener>();
	private ArrayList<ComListener> m_comListeners = new ArrayList<ComListener>();
	private boolean m_quit = false;
	
	private void interpret(String trame){
		if(this.getState()==StateListener.State.CONNECTING){
			if(trame.startsWith("!n")){
				this.disconnect();
				for(ComListener l : m_comListeners){
					synchronized(l){
						l.Error("pseudo déjà pris");
					}
				}
			}else if(trame.startsWith("!o")){
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
		if(this.getState()!=StateListener.State.INITIAL)
		{
			this.disconnect();
		}
		try {
			this.m_sock=new Socket(ipaddr, port);
			this.changeState(StateListener.State.CONNECTING);
		} catch (IOException e) {
			for(ComListener l : m_comListeners){
				synchronized(l){
					l.Error("La connexion a échoué");
				}
			}
			this.m_sock=new Socket();
			this.changeState(StateListener.State.INITIAL);
		}
	}
	
	public synchronized void disconnect(){
		this.changeState(StateListener.State.DISCONNECTING);
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
					l.Error("L'envoi n'a pas pu s'effectuer");
				}
			}
		}
	}
	
	@Override
	public void run() {
		while(!m_quit){
			if(this.getState() != StateListener.State.INITIAL){
				String line;
				BufferedReader in = null;
				try{
					in = new BufferedReader(new InputStreamReader(m_sock.getInputStream()));
				} catch (IOException e) {
					this.disconnect();
				}

				while(this.getState() != StateListener.State.INITIAL){
					try{
						line = in.readLine();
						this.interpret(line);
					}catch (IOException e) {
						this.disconnect();
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
