package client;

public interface StateListener {
	public enum State{
		INITIAL,
		CONNECTING,
		CONNECTED,
		DISCONNECTING
	}
	
	public void onStateChanged(State new_state);
}
