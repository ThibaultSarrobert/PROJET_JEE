package server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerWaiter implements Runnable {
	private ServerSocket m_sock = null;
	private boolean m_quit = false;
	private int m_port = 0;
	private ILinker m_linker = null;
	
	public ServerWaiter(int port, ILinker linker){
		m_port = port;
		m_linker = linker;
	}
	
	protected void finalize(){
		try{
			if(m_sock != null){
				m_sock.close();
			}
		} catch (IOException e) {
			System.out.println("Could not close socket");
		}
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
		//TODO interroger la database et se connecter à chacun des serveurs disponibles
		try {
			m_sock=new ServerSocket(m_port);
		} catch (IOException e) {
			m_quit = true;
		}
		while(!m_quit){
			ServerHandler h = null;
			try {
				h=new ServerHandler(m_sock.accept());
				m_linker.linkServer(h);
			} catch (IOException e) {
				m_quit = true;
			}
		}
		try {
			if(m_sock!=null){
				m_sock.close();
			}
		} catch (IOException e) {
		}
	}

}
