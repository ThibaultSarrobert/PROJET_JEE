package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHandler implements Runnable {
	private Socket m_sock = null;
	private boolean m_quit = false;
	private ArrayList<ServeurListener> m_listeners = new ArrayList<ServeurListener>();

	public ServerHandler(Socket sock){
		m_sock = sock;
	}
	
	public synchronized void addListener(ServeurListener listener){
		m_listeners.add(listener);
	}
	
	public synchronized void post(String msg) throws IOException{
		PrintWriter out = new PrintWriter(m_sock.getOutputStream(), true);
		out.println(msg);
	}
	
	public synchronized void stop(){
		m_quit = true;
		try {
			m_sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String line;
		BufferedReader in=null;
		try {
			in=new BufferedReader(new InputStreamReader(m_sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			m_quit=true;
		}
		while(!m_quit){
			try {
				line=in.readLine();
				if(line==null){
					m_quit=true;
				}else{
					for(ServeurListener l : m_listeners){
						synchronized(l){
							l.serverMessaging(line);
						}
					}
				}
			} catch (IOException e) {
				m_quit=true;
			}
		}
		for(ServeurListener l : m_listeners){
			synchronized (l) {
				l.linkClosing();
			}
		}
		try {
			m_sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
