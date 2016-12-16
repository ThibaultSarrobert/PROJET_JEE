package IHMAuto;

public interface ChatListener {
	public void askDeconnection();
	public void askInitialization();
	public void sendMessage(String msg);
	public void StatusChanged(int status);
	public void KickUser(String pseudo);
	public void hideMessage(String msg);
	public void askShutdown();
}
