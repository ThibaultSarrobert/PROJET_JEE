package test;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JTextField;

import client.Client;
import junit.framework.TestCase;

public class ClientTest extends TestCase{

	
	
	public void TestPseudo() {
		JButton butTest = new JButton();
		JTextField fieldtest = new JTextField();
		ActionEvent actevent = new ActionEvent(butTest, 0,"OK");
		
		Client c = new Client();
		c.getConnexionWindow().getIDField().setText("SamePseudo");	
		c.getConnexionWindow().actionPerformed(actevent);
		
		Client c2 = new Client();
		c2.getConnexionWindow().getIDField().setText("SamePseudo");
		c2.getConnexionWindow().actionPerformed(actevent);
		
		
	}
	
	public void TestCharactersPseudo()
	{
		JButton butTest = new JButton();
		JTextField fieldtest = new JTextField();
		ActionEvent actevent = new ActionEvent(butTest, 0,"OK");
		
		Client c = new Client();
		c.getConnexionWindow().getIDField().setText("ab");	
		c.getConnexionWindow().actionPerformed(actevent);
		
		Client c2 = new Client();
		c2.getConnexionWindow().getIDField().setText("abcdefghijklmnopqrstuvwxyz");
		c2.getConnexionWindow().actionPerformed(actevent);
		
	}

	
	public static void main(String[] args) {
		/*JButton butTest = new JButton();
		JTextField fieldtest = new JTextField();
		c.m_connectWindow.getIDField().setText("Test");
		ActionEvent actevent = new ActionEvent(butTest, 0,"OK");
		c.m_connectWindow.actionPerformed(actevent);
		c.m_chatWindow.getTextUtilisateur().setText("Hello, this is an automatic test");
		KeyEvent keyeve = new KeyEvent(fieldtest, 10, 10, 10, 10);
		c.m_chatWindow.keyReleased(keyeve); */
		
		ClientTest t = new ClientTest();
		//t.TestPseudo();
		//t.TestCharactersPseudo();
		
		
	}
	

}