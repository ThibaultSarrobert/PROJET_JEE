package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket m_sock = null;
	private boolean m_quit = false;
	
	public ClientHandler(Socket sock){
		m_sock=sock;
	}
	
	@Override
	public void run() {
		String line;
		BufferedReader in=null;
		PrintWriter out=null;
		try {
			in=new BufferedReader(new InputStreamReader(m_sock.getInputStream()));
			out=new PrintWriter(m_sock.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		while(!m_quit){
			try {
				line=in.readLine();
				out.println(line);
			} catch (IOException e) {
				try {
					m_sock.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.exit(-1);
				}
				m_quit=true;
			}
		}
	}

}
