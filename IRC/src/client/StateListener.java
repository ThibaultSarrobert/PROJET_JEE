package client;

public interface StateListener {
	public enum State{
		INITIAL,
		CONNECTING,
		CONNECTED,
		DISCONNECTING,
		RECONNECTING
	}
	
	public void onStateChanged(State new_state);
}
