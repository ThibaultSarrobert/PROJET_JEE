package client;

public class Client{
	private CommunicationHandler m_com=null;
	
	public Client(){
		m_com = new CommunicationHandler();
		m_com.configure("127.0.0.1", 5555);
		new Thread(m_com).start();;
	}
	
	public void addStateListener(StateListener listener){
		m_com.addStateListener(listener);
	}
	
	public void addComListener(ComListener listener){
		m_com.addComListener(listener);
	}

	public static void main(String[] args) {
		new Client();
	}
}
