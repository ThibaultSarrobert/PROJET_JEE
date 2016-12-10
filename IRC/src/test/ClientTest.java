package test;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JTextField;

import client.Client;
import junit.framework.TestCase;

public class ClientTest extends TestCase{

	
	
	public void TestPseudo() {
		JButton butTest = new JButton();
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
		ActionEvent actevent = new ActionEvent(butTest, 0,"OK");
		
		Client c = new Client();
		c.getConnexionWindow().getIDField().setText("ab");	
		c.getConnexionWindow().actionPerformed(actevent);
		
		Client c2 = new Client();
		c2.getConnexionWindow().getIDField().setText("abcdefghijklmnopqrstuvwxyz");
		c2.getConnexionWindow().actionPerformed(actevent);
		
	}
	
	public void TestNumberMaxClient()
	{
		JButton butTest = new JButton();
		ActionEvent actevent = new ActionEvent(butTest, 0,"OK");
		
		for(int i = 0; i< 100 ; i++)
		{
			Client c = new Client();
			c.getConnexionWindow().getIDField().setText("Client"+i);
			c.getConnexionWindow().actionPerformed(actevent);
			c.sendMessage("Hello ! This is Client"+i);

		}
	}
	
	public void TestNumberMaxMessage()
	{
		JButton butTest = new JButton();
		ActionEvent actevent = new ActionEvent(butTest, 0,"OK");
		Client c = new Client();
		c.getConnexionWindow().getIDField().setText("ClientTest");
		c.getConnexionWindow().actionPerformed(actevent);
		
		for (int i = 0; i< 100000; i++)
		{

			c.sendMessage(Integer.toString(i));
		}
	}

	
	public static void main(String[] args) {
		
		int test;
		Scanner sc = new Scanner(System.in);
		ClientTest t = new ClientTest();
		System.out.println("Welcome to the Test Board\n0- Quit\n1- Test Same Pseudo\n2- Test restrictions on number characters pseudo\n3- Test Number max client\n4- Test Number Max Message");
		test = sc.nextInt();
		switch(test)
		{
		
		case 1 : t.TestPseudo();
				 break;
				
		case 2 : t.TestCharactersPseudo();
				 break;
		case 3 : t.TestNumberMaxClient();
				 break;
		case 4 : t.TestNumberMaxMessage();
				 break;
				 
		default: System.exit(0);
		}

		
		
	}
	

}