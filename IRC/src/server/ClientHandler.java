package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import client.ComListener;

public class ClientHandler implements Runnable {
	private Socket m_sock = null;
	private boolean m_quit = false;
	private ArrayList<ComListener> m_comListeners = new ArrayList<ComListener>();
	
	public ClientHandler(Socket sock){
		m_sock=sock;
	}
	
	public synchronized void addListener(ComListener listener){
		m_comListeners.add(listener);
	}
	
	public synchronized void post(String msg) throws IOException{
		PrintWriter out = new PrintWriter(m_sock.getOutputStream(), true);
		out.println(msg);
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
					for(ComListener l : m_comListeners){
						synchronized(l){
							l.onTrameReceived(line);
						}
					}
				}
			} catch (IOException e) {
				try {
					m_sock.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.exit(-1);
				}
				m_quit=true;
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
