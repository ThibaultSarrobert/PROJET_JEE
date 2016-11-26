package IHMAuto;

public interface ChatListener {
	public void deconnectionAsked();
	public void sendMessage(String msg);
	public void StatusChanged(int status);
}
