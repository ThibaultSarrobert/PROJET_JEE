package client;

import IHMAuto.ChatClientWindow;
import IHMAuto.ChatListener;
import IHMAuto.ConnexionWindow;
import IHMAuto.InfoConnectListener;

public class Client implements StateListener, ComListener, InfoConnectListener, ChatListener {
	private CommunicationHandler m_com=null;
	private String m_pseudo=null;
	private ConnexionWindow m_connectWindow = null;
	private ChatClientWindow m_chatWindow = null;
	
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
	}
	
	@Override
	public void onTrameReceived(String trame){
		if(m_chatWindow != null){
			m_chatWindow.addMessage(trame);
		}
	}

	@Override
	public void onStateChanged(State new_state) {
		if(new_state==StateListener.State.CONNECTED){
			m_chatWindow=new ChatClientWindow(m_pseudo);
			m_chatWindow.addListener(this);
			m_chatWindow.setVisible(true);
			m_connectWindow.setVisible(false);
		}else{
			if(m_chatWindow!=null){
				m_chatWindow.dispose();
			}
			m_chatWindow=null;
			m_connectWindow.setVisible(true);
		}
	}

	@Override
	public void deconnectionAsked() {
		m_com.disconnect();
	}

	@Override
	public void sendMessage(String msg) {
		m_com.post(msg);
	}

	@Override
	public void StatusChanged(int status) {
		// TODO Auto-generated method stub
		
	}
}