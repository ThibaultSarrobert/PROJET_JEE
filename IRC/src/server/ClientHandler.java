package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

public class ClientHandler implements Runnable {
	private Socket m_sock = null;
	private IDataPool m_dataPool = null;
	private boolean m_quit = false;
	private String m_pseudo = null;

	private String mdpCrypt = new String();

	private ArrayList<ClientListener> m_listeners = new ArrayList<ClientListener>();
	
	private void interpret(String trame){
		mdpCrypt=cryptMdp(mdpCrypt);
		
		if(trame.startsWith("+u")){
			boolean dejaPris=false;
			for(String s : m_dataPool.getUserPool()){
				if(s.equals(trame.substring(2))){
					dejaPris=true;
					break;
				}
			}
			if(dejaPris){
				try {
					this.post("!n"+"loginError");
				} catch (IOException e) {
				}
			}else{
				try {
					m_pseudo=trame.substring(2);
					this.propagate(trame);
					this.post("!o");
				} catch (IOException e) {
				}
			}
		}else if(trame.startsWith("!i")){
			for(String nom : m_dataPool.getUserPool()){
				try {
					this.post("+u"+nom);
				} catch (IOException e) {
					m_quit=true;
				}
			}
			for(String msg : m_dataPool.getMessagePool()){
				try {
					this.post("+m"+msg);
				} catch (IOException e) {
					m_quit=true;
				}
			}
		}else if(trame.startsWith("+a")){
			boolean dejaPris=false;
			int index = trame.indexOf("|");
			for(String s : m_dataPool.getUserPool()){
				if(s.equals(trame.substring(2,index))){
					dejaPris=true;
					break;
				}
			}
			
			if(dejaPris){
				try {
					this.post("!n"+"loginError");
				} catch (IOException e) {
				}
			}else{
				try {
					if(mdpCrypt.equals(trame.substring(index+1))){
					m_pseudo=trame.substring(2,index);
					this.propagate("+u"+m_pseudo);
					this.post("!a");
					}
					else{
						this.post("!n"+"mdpError");
					}
				} catch (IOException e) {
				}
			}
		}
		else{
			this.propagate(trame);
		}
	}
	
	private String cryptMdp(String mdp){
		String sha="";
                mdp = "mdp";
		try{
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			try {
				crypt.update(mdp.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sha = byteToHex(crypt.digest());
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return sha;
	}
	
	private String byteToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for(byte b : hash){
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	private void propagate(String trame){
		for(ClientListener l : m_listeners){
			synchronized(l){
				l.clientMessaging(trame);
			}
		}
	}
	
	public String getName(){
		return m_pseudo;
	}
	
	public ClientHandler(Socket sock, IDataPool datas){
		m_sock=sock;
		m_dataPool=datas;
	}
	
	public synchronized void addListener(ClientListener listener){
		m_listeners.add(listener);
	}
	
	public synchronized void post(String msg) throws IOException{
		PrintWriter out = new PrintWriter(m_sock.getOutputStream(), true);
		out.println(msg);
	}
	
	public synchronized void stop(){
		m_quit = true;
		try {
			m_sock.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//TODO r�cup�rer l'historique des messages et le transmettre
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
					interpret(line);
				}
			} catch (IOException e) {
				m_quit=true;
			}
		}
		try {
			m_sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(m_pseudo != null){
			this.interpret("-u"+m_pseudo);
		}
	}

}
