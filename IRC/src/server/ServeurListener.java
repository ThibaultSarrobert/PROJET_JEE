package server;

public interface ServeurListener {
	public void serverMessaging(String trame);
	public void linkClosing();
}
