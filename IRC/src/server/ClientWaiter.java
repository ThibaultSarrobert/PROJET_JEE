package server;

import java.io.IOException;
import java.net.ServerSocket;

public class ClientWaiter implements Runnable {
	private ServerSocket m_sock = null;
	private boolean m_quit = false;
	private ILinker m_linker = null;
	private IDataPool m_dataPool = null;
	
	public ClientWaiter(int port, ILinker linker, IDataPool dataPool) throws IOException{
		m_sock=new ServerSocket(port);
		m_linker=linker;
		m_dataPool=dataPool;
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
		while(!m_quit){
			ClientHandler h = null;
			try {
				h=new ClientHandler(m_sock.accept(), m_dataPool);
				m_linker.linkClient(h);
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
