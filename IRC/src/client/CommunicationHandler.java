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
	private ArrayList<StateListener> m_stateListeners = null;
	private ArrayList<ComListener> m_comListeners = null;
	private boolean m_quit = false;
	
	public void stop(){
		m_quit=true;
	}
	
	private void changeState(StateListener.State new_state){
		this.m_state=new_state;
		for(StateListener l : m_stateListeners){
			l.onStateChanged(new_state);
		}
	}
	
	public StateListener.State getState(){
		return m_state;
	}
	
	public CommunicationHandler(){
		this.m_quit=false;
		this.m_sock = new Socket();
		this.m_state=StateListener.State.INITIAL;
		this.m_stateListeners=new ArrayList<StateListener>();
		this.m_comListeners=new ArrayList<ComListener>();
	}
	
	public void addStateListener(StateListener listener){
		m_stateListeners.add(listener);
	}
	
	public void addComListener(ComListener listener){
		m_comListeners.add(listener);
	}
	
	public void configure(String ipaddr, int port){
		this.disconnect();
		this.changeState(StateListener.State.CONNECTING);
		try {
			this.m_sock=new Socket(ipaddr, port);
			this.changeState(StateListener.State.CONNECTED);
		} catch (IOException e) {
			this.m_sock=new Socket();
			this.changeState(StateListener.State.INITIAL);
		}
	}
	
	public void disconnect(){
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
	
	public void post(String msg){
		PrintWriter out = null;
		try {
			out = new PrintWriter(m_sock.getOutputStream(), true);
			out.println(msg);
		} catch (IOException e) {
		}
	}
	
	@Override
	public void run() {
		while(!m_quit){
			if(m_state == StateListener.State.CONNECTED){
				String line;
				BufferedReader in = null;
				try{
					in = new BufferedReader(new InputStreamReader(m_sock.getInputStream()));
				} catch (IOException e) {
					this.disconnect();
				}

				while(m_state == StateListener.State.CONNECTED){
					try{
						line = in.readLine();
						for(ComListener l : m_comListeners){
							l.onTrameReceived(line);
						}
					}catch (IOException e) {
						this.disconnect();
					}
				}
			}
		}
	}
	
	protected void finalize(){
		try{
			m_sock.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}
}
