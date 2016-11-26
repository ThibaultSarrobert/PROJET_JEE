package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
	private ServerSocket m_sock=null;
	private ArrayList<ClientHandler> m_clients = null;
	
	public Server(){
		m_clients=new ArrayList<ClientHandler>();
	}
	
	public void listenSocket(){
		try {
			m_sock=new ServerSocket(5555);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		while(true){
			ClientHandler h;
			try {
				h=new ClientHandler(m_sock.accept());
				m_clients.add(h);
				new Thread(h).start();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
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

	public static void main(String[] args) {
		Server s = new Server();
		s.listenSocket();
	}

}
