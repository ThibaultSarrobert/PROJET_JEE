package server;

import java.io.IOException;
import java.util.ArrayList;

public class Server implements Runnable, ClientListener, ServeurListener, IDataPool, ILinker {
	private ClientWaiter m_clientWaiter = null;
	private ArrayList<ClientHandler> m_clients = new ArrayList<ClientHandler>();
	private ServerWaiter m_serverWaiter = null;
	private ArrayList<ServerHandler> m_annexeServers = new ArrayList<ServerHandler>();
		
	public Server(){
	}

	@Override
	public void run() {
		//TODO read the configuration file
		//TODO register this server in the database
		m_serverWaiter = new ServerWaiter(4445, this);
		new Thread(m_serverWaiter).start();
		m_clientWaiter = new ClientWaiter(4444, this, this);
		new Thread(m_clientWaiter).start();
		System.out.println("Serveur en ligne");
	}
	

	public static void main(String[] args) {
		Server s = new Server();
		s.run();
	}

	public void interpret(String trame) {
		if(trame.startsWith("!q")){
			m_serverWaiter.stop();
			m_clientWaiter.stop();
			for(ServerHandler sh : m_annexeServers){
				sh.stop();
			}
			for(ClientHandler ch : m_clients){
				ch.stop();
			}
			System.exit(0);
		}else if(trame.startsWith("-u")){
			for(ClientHandler c : m_clients){
				if(trame.substring(2).equals(c.getName())){
					c.stop();
					m_clients.remove(c);
					break;
				}
			}
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

	@Override
	public void linkClient(ClientHandler client) {
		client.addListener(this);
		m_clients.add(client);
		new Thread(client).start();
	}

	@Override
	public void linkServer(ServerHandler serv) {
		serv.addListener(this);
		m_annexeServers.add(serv);
		new Thread(serv).start();
	}

	@Override
	public void clientMessaging(String trame) {
		ArrayList<ServerHandler> trash = new ArrayList<ServerHandler>();
		for(ServerHandler sh : m_annexeServers){
			try {
				sh.post(trame);
			} catch (IOException e) {
				trash.add(sh);
			}
		}
		for(ServerHandler sh : trash){
			m_annexeServers.remove(sh);
		}
		interpret(trame);
	}

	@Override
	public void serverMessaging(String trame) {
		interpret(trame);
	}
}
