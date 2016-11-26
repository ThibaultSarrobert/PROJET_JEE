package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import client.ComListener;

public class Server implements Runnable, ComListener {
	private ServerSocket m_sock=null;
	private ArrayList<ClientHandler> m_clients = null;
	private boolean m_quit = false;
	private int m_port;
	
	public Server(int port){
		m_clients=new ArrayList<ClientHandler>();
		m_quit = false;
		m_port = port;
	}
	
	protected void finalize(){
		try{
			m_sock.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
		}
	}

	@Override
	public void run() {
		try {

			m_sock=new ServerSocket(m_port);

		} catch (IOException e) {
			m_quit = true;
		}
		while(!m_quit){
			ClientHandler h;
			try {
				h=new ClientHandler(m_sock.accept());
				m_clients.add(h);
				new Thread(h).start();
			} catch (IOException e) {
				m_quit = true;
			}
		}
		try {
			m_sock.close();
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) {
		Server s = new Server(4444);
		s.run();
	}

	@Override
	public void onTrameReceived(String trame) {
		for(ClientHandler c : m_clients){
			c.post(trame);
		}
	}
}
