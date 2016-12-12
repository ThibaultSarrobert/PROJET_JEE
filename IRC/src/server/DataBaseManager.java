package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBaseManager {
	private Connection m_conn = null;
	
	public class ServerCoord{
		private String m_hostname;
		private int m_portClient;
		private int m_portServeur;
		
		public ServerCoord(String addr, int portClient, int portServeur){
			m_hostname=addr;
			m_portClient=portClient;
			m_portServeur=portServeur;
		}
		
		public String getHostname(){
			return m_hostname;
		}
		public int getClientPort(){
			return m_portClient;
		}
		public int getServerPort(){
			return m_portServeur;
		}
	}
	
	public DataBaseManager(String hostname, int port, String databaseName) throws ClassNotFoundException, SQLException{
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://"+hostname+":"+Integer.toString(port)+"/"+databaseName;
		m_conn = DriverManager.getConnection(url, "postgres", "postgres");
	}
	
	protected void finalize(){
		try {
			m_conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getTrameHistory(){
		ArrayList<String> history = new ArrayList<String>();
		
		try {
			Statement statement = m_conn.createStatement();
			String query = "SELECT trame, validity FROM conversation";
			ResultSet res = statement.executeQuery(query);

			java.sql.Date sqlToday = new java.sql.Date(new java.util.Date().getTime());
			
			while(res.next()){
				if(res.getDate("validity").after(sqlToday)){
					history.add(res.getString("trame"));
				}
			}
			
			res.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return history;
	}
	
	public void addTrame(String trame){
		try {
			Statement statement = m_conn.createStatement();
			
			java.sql.Date validity = new java.sql.Date(new java.util.Date().getTime()+(7*24*60*60*1000));
			
			String query = "INSERT INTO conversation (trame, validity) VALUES ('"+trame+"', '"+validity.toString()+"')";
			statement.executeUpdate(query);
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<ServerCoord> getServerList(){
		ArrayList<ServerCoord> serverlist = new ArrayList<ServerCoord>();
		
		try {
			Statement statement = m_conn.createStatement();
			String query = "SELECT hostname, port, link_port FROM serveurs";
			ResultSet res = statement.executeQuery(query);
			
			while(res.next()){
				serverlist.add(new ServerCoord(res.getString("hostname"), res.getInt("port"), res.getInt("link_port")));
			}
			
			res.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return serverlist;
	}
	
	public void addServer(String hostname, int portClient, int portServeur){
		try {
			Statement statement = m_conn.createStatement();
						
			String query = "INSERT INTO serveurs (hostname, port, link_port) VALUES ('"+hostname+"', '"+Integer.toString(portClient)+"', '"+Integer.toString(portServeur)+"')";
			statement.executeUpdate(query);
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeServer(String hostname, int portClient, int portServeur){
		try {
			Statement statement = m_conn.createStatement();
						
			String query = "DELETE FROM serveurs WHERE hostname='"+hostname+"' AND port="+Integer.toString(portClient)+" AND link_port="+Integer.toString(portServeur);
			statement.executeUpdate(query);
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
