package IHMv2;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class ConnexionWindowAdmin extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JFrame controllingFrameAdmin;
	private static String OK = "OK";
	private static String EXIT = "EXIT";
	private JPasswordField passwordField;
	
	public ConnexionWindowAdmin(){
		//Initialisation de la JFRAME avec ses reglages - options
		controllingFrameAdmin = new JFrame("Connexion Admin");
		controllingFrameAdmin.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		controllingFrameAdmin.setResizable(false);
		controllingFrameAdmin.setLocationRelativeTo(null);
		setOpaque(true);
		controllingFrameAdmin.setContentPane(this);
		controllingFrameAdmin.setVisible(true);
		controllingFrameAdmin.setAlwaysOnTop(true);
		
	
	//Contenu de la JFrame :
		//Partie Pseudo
	
	passwordField = new JPasswordField(10);
	passwordField.setActionCommand(OK);
	passwordField.addActionListener(this);
	
	JLabel labelPWD = new JLabel("Mot de Passe : ");
	labelPWD.setLabelFor(passwordField);
	
	//Partie Bouton
		//Bouton OK
		JButton butOk = new JButton(OK);
		butOk.setActionCommand(OK);
		butOk.addActionListener(this);
		//Bouton EXIT
		JButton butExit = new JButton(EXIT);
		butExit.setActionCommand(EXIT);
		butExit.addActionListener(this);
		
		//PANNEAU BOUTON
		JComponent buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout(2,1));
		buttonPane.add(butOk);
		buttonPane.add(butExit);
		//PANNEAU TEXTE
		JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		textPane.add(labelPWD);
		textPane.add(passwordField);
	
		add(textPane);
		add(buttonPane);
		controllingFrameAdmin.pack();
	}

	@Override
public void actionPerformed(ActionEvent e) 
	{
		String cmd = e.getActionCommand();

		if (OK.equals(cmd)) 
		{
			JOptionPane.showMessageDialog(controllingFrameAdmin, "Ouverture CHAT window en tant qu'Admin");
		}
		else if(EXIT.equals(cmd))
		{
			controllingFrameAdmin.setVisible(false);
		}
	}
}
