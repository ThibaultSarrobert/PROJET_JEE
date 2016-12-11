package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
	private Socket m_sock = null;
	private IDataPool m_dataPool = null;
	private boolean m_quit = false;
	private String m_pseudo = null;
	private ArrayList<ClientListener> m_listeners = new ArrayList<ClientListener>();
	
	private void interpret(String trame){
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
					this.post("!n");
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
		}else{
			this.propagate(trame);
		}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//TODO récupérer l'historique des messages et le transmettre
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
