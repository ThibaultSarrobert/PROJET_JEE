package server;

public interface ILinker {
	public void linkClient(ClientHandler client);
	public void linkServer(ServerHandler serv);
}
