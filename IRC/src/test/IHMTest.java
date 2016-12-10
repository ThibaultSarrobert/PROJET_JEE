package test;

import java.awt.event.ActionEvent;
import java.util.Scanner;

import javax.swing.JButton;

import client.Client;
import junit.framework.TestCase;

public class IHMTest extends TestCase{

	
	public void TestBasicScenario()
	{
		JButton butTest = new JButton();
		ActionEvent actevent = new ActionEvent(butTest, 0,"OK");
		Client c = new Client();
		
		c.getConnexionWindow().getIDField().setText("ClientBasic");
		c.getConnexionWindow().actionPerformed(actevent);
		c.sendMessage("Hello ! This is an automatic test for a basic scenario in the best IRC !");
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		int test;
		Scanner sc = new Scanner(System.in);
		IHMTest testIHM = new IHMTest();
		System.out.println("Welcome to the Test Board for IHM\n0- Quit\n1- Test with a basic scenario");
		test = sc.nextInt();
		switch(test)
		{
		
		case 1 : testIHM.TestBasicScenario();;
				 break;
				

				 
		default: System.exit(0);
		}
	}

}
