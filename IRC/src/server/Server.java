package server;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import org.ini4j.Ini;

public class Server implements Runnable, ClientListener, ServeurListener, IDataPool, ILinker {
	private ClientWaiter m_clientWaiter = null;
	private ArrayList<ClientHandler> m_clients = new ArrayList<ClientHandler>();
	private ServerWaiter m_serverWaiter = null;
	private ArrayList<ServerHandler> m_annexeServers = new ArrayList<ServerHandler>();
	private DataBaseManager m_db = null;
	private String m_hostname;
	private int m_portClient;
	private int m_portServeur;
	
	public Server(){
	}

	@Override
	public void run() {
		try{
			Ini inifile = new Ini(new File("config.ini"));
			Ini.Section dbsection = inifile.get("database");
			String db_hostname = dbsection.get("hostname");
			int db_port = dbsection.get("port", int.class);
			String db_name = dbsection.get("name");

			Ini.Section servsection = inifile.get("server");
			m_hostname=servsection.get("hostname");
			m_portClient=servsection.get("portClient", int.class);
			m_portServeur=servsection.get("portServeur", int.class);
		
			m_db = new DataBaseManager(db_hostname, db_port, db_name);
			for(DataBaseManager.ServerCoord server : m_db.getServerList()){
				try {
					this.linkServer(new ServerHandler(new Socket(server.getHostname(), server.getServerPort())));
				} catch (IOException e) {
					m_db.removeServer(server.getHostname(), server.getClientPort(), server.getServerPort());
				}
			}
			
			try {
				m_serverWaiter = new ServerWaiter(m_portServeur, this);
				new Thread(m_serverWaiter).start();
				m_clientWaiter = new ClientWaiter(m_portClient, this, this);
				new Thread(m_clientWaiter).start();
				m_db.addServer(m_hostname, m_portClient, m_portServeur);
				System.out.println("Serveur en ligne sur les ports "+m_portClient+", "+m_portServeur);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			if(m_db != null) m_db.removeServer(m_hostname, m_portClient, m_portServeur);
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
		for(String s : m_db.getTrameHistory()){
			if(s.startsWith("+u")){
				userPool.add(s.substring(2));
			}else if(s.startsWith("-u")){
				userPool.remove(s.substring(2));
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
		m_db.addTrame(trame);
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

	@Override
	public ArrayList<String> getMessagePool() {
		ArrayList<String> msgPool = new ArrayList<String>();
		for(String s : m_db.getTrameHistory()){
			if(s.startsWith("+m")){
				msgPool.add(s.substring(2));
			}else if(s.startsWith("-m")){
				msgPool.remove(s.substring(2));
			}
		}
		return msgPool;
	}
}
