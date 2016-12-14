package IHMAuto;

public interface InfoConnectListener {
	public void askForConnect(String pseudo, String ipaddr, int port);
	public void askForAdminConnect(String pseudo, String mdp, String ipaddr, int port);
}
