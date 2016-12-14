package client;

import java.awt.GraphicsConfiguration;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import javax.swing.JOptionPane;

import IHMAuto.ChatClientWindow;
import IHMAuto.ChatListener;
import IHMAuto.ConnexionWindow;
import IHMAuto.InfoConnectListener;

public class Client implements StateListener, ComListener, InfoConnectListener, ChatListener {
	private CommunicationHandler m_com=null;
	private String m_pseudo=null;
	private ConnexionWindow m_connectWindow = null;
	private ChatClientWindow m_chatWindow = null;
	private boolean isAdmin = false;
	
	public Client(){
		m_com = new CommunicationHandler();
		m_com.addComListener(this);
		m_com.addStateListener(this);
		m_connectWindow = new ConnexionWindow();
		m_connectWindow.addInfoListener(this);
		new Thread(m_com).start();
	}
	
	protected void finalize(){
		m_com.stop();
	}

	public static void main(String[] args) {
		new Client();
	}

	@Override
	public void askForConnect(String pseudo, String ipaddr, int port) {
		m_pseudo=pseudo;
		m_com.configure(ipaddr, port);
		m_com.post("+u"+m_pseudo);
	}
	
	@Override
	public void onTrameReceived(String trame){
		if(trame.startsWith("+m")){
			if(m_chatWindow != null){
				m_chatWindow.addMessage(trame.substring(2));
			}
		}else if(trame.startsWith("+u")){
			if(m_chatWindow!=null){
				m_chatWindow.addUser(trame.substring(2));
			}
		}else if(trame.startsWith("-u")){
			if(m_chatWindow != null){
				m_chatWindow.supprUser(trame.substring(2));
			}
		}else if(trame.startsWith("!f")){
			if(m_chatWindow!=null){
				m_chatWindow.changeStatus(trame.substring(2), 0);
			}
		}else if(trame.startsWith("!b")){
			if(m_chatWindow!=null){
				m_chatWindow.changeStatus(trame.substring(2), 1);
			}
		}else if(trame.startsWith("!l")){
			if(m_chatWindow!=null){
				m_chatWindow.changeStatus(trame.substring(2), 2);
			}
		}
	}

	@Override
	public void onStateChanged(State new_state) {
		if(new_state==StateListener.State.CONNECTED){
			m_chatWindow=new ChatClientWindow(m_pseudo,m_com.get_isAdmin());
			m_chatWindow.addListener(this);
			m_chatWindow.setVisible(true);
			m_connectWindow.setVisible(false);	
			isAdmin=false;
		}else{
			if(m_chatWindow!=null){
				m_chatWindow.dispose();
			}
			m_chatWindow=null;
			m_connectWindow.setVisible(true);
		}
	}

	@Override
	public void askDeconnection() {
		m_com.post("-u"+m_pseudo);
		m_com.disconnect();
	}

	@Override
	public void sendMessage(String msg) {
		m_com.post("+m"+m_pseudo+" : "+msg);
	}

	@Override
	public void StatusChanged(int status) {
		switch(status){
		case 0:
			m_com.post("!f"+m_pseudo);
			break;
		case 1:
			m_com.post("!b"+m_pseudo);
			break;
		case 2:
			m_com.post("!l"+m_pseudo);
			break;
		}
	}

	@Override
	public void askInitialization() {
		m_com.post("!i");
	}

	@Override
	public void Error(String error) {
		JOptionPane.showMessageDialog(m_chatWindow, error, "Erreur", 0);
	}


	@Override
	public void askForAdminConnect(String pseudo, String mdp, String ipaddr, int port) {
		m_pseudo=pseudo;
		m_com.configure(ipaddr, port);
		String sha="";
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
		m_com.connexionAdmin(m_pseudo,sha);
	}

	private String byteToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for(byte b : hash){
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		return result;
	}

	@Override
	public void KickUser(String pseudo) {
		m_com.post("-u"+pseudo);
	}

	
	public ConnexionWindow getConnexionWindow()
	{
		return m_connectWindow;
	}
	
	public ChatClientWindow getChatClientWindow()
	{
		return m_chatWindow;

	}
}
