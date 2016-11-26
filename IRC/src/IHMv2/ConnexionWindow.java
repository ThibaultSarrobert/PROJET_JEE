package IHMv2;

import java.awt.Color;
import java.awt.Container;
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
import javax.swing.JTextField;

public class ConnexionWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static String OK = "OK";
	private static String HELP = "AIDE";
	private static String ADMIN = "ADMIN";
	private JTextField serveurField;
	private JTextField IDField;
	private JTextField portField;

	public ConnexionWindow(){
		//Initialisation de la JFRAME avec ses reglages - options
		Container contentPane = getContentPane();
		contentPane.setBackground(new Color(29,34,44));
		setTitle("Connexion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200,200);
		setSize(650,120);
		setResizable(false);
		setVisible(true);

	//Contenu de la JFrame :
	//Partie Pseudo
	IDField = new JTextField(10);
	JLabel labelID = new JLabel("Pseudo : ");
	labelID.setLabelFor(IDField);
	//Partie Serveur IP
	serveurField = new JTextField(10);
	JLabel labelServ = new JLabel("IP du server : ");
	labelServ.setLabelFor(serveurField);
	//Partie Port
	portField = new JTextField(5);
	JLabel labelPort = new JLabel("Port : ");
	labelPort.setLabelFor(portField);

	//Partie Bouton
	//Bouton OK
	JButton butOk = new JButton(OK);
	butOk.setActionCommand(OK);
	butOk.addActionListener(this);
	
	//Bouton AIDE
	JButton butAide = new JButton(HELP);
	butAide.setActionCommand(HELP);
	butAide.addActionListener(this);
	//Bouton ADMIN
	JButton butAdmin = new JButton(ADMIN);
	butAdmin.setActionCommand(ADMIN);
	butAdmin.addActionListener(this);
	
	
	//PANNEAU BOUTON
	JComponent buttonPane = new JPanel();
	buttonPane.setLayout(new GridLayout(3,1));
	buttonPane.add(butOk);
	buttonPane.add(butAide);
	buttonPane.add(butAdmin);
	//PANNEAU TEXTE
	JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
	textPane.add(labelID);
	textPane.add(IDField);
	textPane.add(labelServ);
	textPane.add(serveurField);
	textPane.add(labelPort);
	textPane.add(portField);
	
	//PANNEAU FINAL
	JPanel finalPane = new JPanel();
	finalPane.add(textPane);
	finalPane.add(buttonPane);
	
	contentPane.add(finalPane);
	setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(OK.equals(cmd))
		{	
			this.setVisible(false);
			@SuppressWarnings("unused")
			ChatClientWindow C = new ChatClientWindow();
		}
		else if(HELP.equals(cmd))
		{
			JOptionPane.showMessageDialog(getParent(), "Votre pseudo doit être compris entre 3 à 15 charactères\n "
					+ "Si il n'est pas accepté : c'est qu'il est deja utilisé\n");
		}
		else{
			@SuppressWarnings("unused")
			ConnexionWindowAdmin A = new ConnexionWindowAdmin();
		}
	}
}
