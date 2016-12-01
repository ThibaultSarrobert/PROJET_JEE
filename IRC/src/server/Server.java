package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import client.ComListener;

public class Server implements Runnable, ComListener, IDataPool {
	private ServerSocket m_sock=null;
	private ArrayList<ClientHandler> m_clients = new ArrayList<ClientHandler>();
	private boolean m_quit = false;
	private int m_port;
		
	public Server(int port){
		m_port = port;
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

	@Override
	public void run() {
		try {
			m_sock=new ServerSocket(m_port);
			System.out.println("Serveur en ligne");
		} catch (IOException e) {
			m_quit = true;
		}
		while(!m_quit){
			ClientHandler h = null;
			try {
				h=new ClientHandler(m_sock.accept(), this);
				h.addListener(this);
				synchronized(this){
					m_clients.add(h);
				}
				new Thread(h).start();
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
	

	public static void main(String[] args) {
		Server s = new Server(4444);
		s.run();
	}

	@Override
	public void onTrameReceived(String trame) {
		if(trame.startsWith("!q")){
			synchronized(this){
				this.m_quit=true;
			}
		}else if(trame.startsWith("-u")){
			for(ClientHandler c : m_clients){
				if(trame.substring(2).equals(c.getName())){
					c.stop();
					m_clients.remove(c);
					break;
				}
			}
			for(ClientHandler c : m_clients){
				try {
					c.post(trame);
				} catch (IOException e) {
				}
			}
		}else{
			ArrayList<ClientHandler> trash = new ArrayList<ClientHandler>();
			for(ClientHandler c : m_clients){
				try {
					c.post(trame);
				} catch (IOException e) {
					trash.add(c);
				}
			}
			for(ClientHandler h : trash){
				m_clients.remove(h);
			}
		}
	}

	@Override
	public ArrayList<String> getUserPool() {
		ArrayList<String> userPool = new ArrayList<String>();
		synchronized(this){
			for(ClientHandler c : m_clients){
				String user = c.getName();
				if(user != null){
					userPool.add(user);
				}
			}
		}
		return userPool;
	}
}
