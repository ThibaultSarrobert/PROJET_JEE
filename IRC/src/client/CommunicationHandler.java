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
	
	public synchronized void stop(){
		m_quit=true;
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
		this.changeState(StateListener.State.CONNECTING);
		try {
			this.m_sock=new Socket(ipaddr, port);
			this.changeState(StateListener.State.CONNECTED);
		} catch (IOException e) {
			System.out.println("Couldn't create socket");
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
			System.out.println("Cannot get output stream");
		}
	}
	
	@Override
	public void run() {
		while(!m_quit){
			if(this.getState() == StateListener.State.CONNECTED){
				String line;
				BufferedReader in = null;
				try{
					in = new BufferedReader(new InputStreamReader(m_sock.getInputStream()));
				} catch (IOException e) {
					this.disconnect();
				}

				while(this.getState() == StateListener.State.CONNECTED){
					try{
						line = in.readLine();
						for(ComListener l : m_comListeners){
							synchronized(l){
								l.onTrameReceived(line);
							}
						}
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
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}
}
