package server;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.ini4j.Ini;

public class Server implements Runnable, ClientListener, ServeurListener, IDataPool, ILinker {
        //Implement of Logger and Category for log4f
        private final static Logger logger = Logger.getLogger(Server.class); 
		static Category category = Category.getInstance(Server.class.getName()); 
        
	private ClientWaiter m_clientWaiter = null;
	private ArrayList<ClientHandler> m_clients = new ArrayList<ClientHandler>();
	private ServerWaiter m_serverWaiter = null;
	private ArrayList<ServerHandler> m_annexeServers = new ArrayList<ServerHandler>();
	private DataBaseManager m_db = null;
	private String m_hostname;
	private int m_portClient;
	private int m_portServeur;
	private int m_id = 0;
	
	public Server(){
	}

	@Override
	public void run() {
            logger.trace("Launching of method run()");
            logger.info("Lanching of the serveur");
            
		try{
			Ini inifile = new Ini(new File("config.ini"));
			Ini.Section dbsection = inifile.get("database");
			String db_hostname = dbsection.get("hostname");
			int db_port = dbsection.get("port", int.class);
			String db_name = dbsection.get("name");

                        logger.info("Loading of file config.ini");
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
				m_id = m_db.addServer(m_hostname, m_portClient, m_portServeur);

				System.out.println("Online port server "+m_portClient+", "+m_portServeur);
                logger.trace("Server online on the ports "+m_portClient+", "+m_portServeur);
                                
			} catch (IOException e) {
				e.printStackTrace();
                                logger.fatal("Problem of lauching of the server");
			}
			
		} catch (ClassNotFoundException | SQLException e) {

			System.out.println("Database trouble");
			logger.fatal("Error : access to the database wrong");
            e.printStackTrace();
		}catch (IOException e){
			System.out.println("config.ini missing");
			logger.fatal("The fille config.ini is absent or badly configured");

			
		}
	}
	

	public static void main(String[] args) { 
		Server s = new Server();
		s.run();
	}
	
	private void propagate(String trame){
            logger.trace("Enter in the method propagate");
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
		}else if(trame.startsWith("+u")){
			m_db.addUser(trame.substring(2), m_id);
			propagate(trame);
		}else if(trame.startsWith("-u")){
			m_db.removeUser(trame.substring(2));
			for(ClientHandler c : m_clients){
				if(trame.substring(2).equals(c.getName())){
					c.stop();
					m_clients.remove(c);
					break;
				}
			}
			propagate(trame);
		}else if(trame.startsWith("-m")){
			m_db.removeTrame("+"+trame.substring(1));
			propagate(trame);
		}else{
			propagate(trame);
		}
	}

	@Override
	public ArrayList<String> getUserPool() {
		return m_db.getUserList();
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
		propagate(trame);
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

	@Override
	public void linkClosing() {
		ArrayList<String> userdeleted = m_db.getUserList();
		for(DataBaseManager.ServerCoord server : m_db.getServerList()){
			try {
				this.linkServer(new ServerHandler(new Socket(server.getHostname(), server.getClientPort())));
			} catch (IOException e) {
				m_db.removeServer(server.getHostname(), server.getClientPort(), server.getServerPort());
			}
		}
		userdeleted.removeAll(m_db.getUserList());
		for(String name : userdeleted){
			propagate("-u"+name);
		}
	}
}
