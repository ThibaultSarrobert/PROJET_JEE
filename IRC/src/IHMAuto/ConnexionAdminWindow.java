package IHMAuto;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class ConnexionAdminWindow extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private static String OK = "OK";
	private static String EXIT = "EXIT";
	private String titreFenetre = "Connexion en tant qu'admin";
	private JPasswordField passwordField;
	private Color backBlue = new Color(29,34,44);
	private Color buttBlue = new Color(10,129,183);
	private Color backField = new Color(20,25,34);
	private Color borderBlue = new Color(53,62,80);
	private Color borderGrey = new Color(53,62,80);
	private String pseudo = new String(); 
	private ArrayList<InfoConnectListener> m_infoListeners = new ArrayList<InfoConnectListener>();
	
	public ConnexionAdminWindow(String ID){
		
		setIconImage(new ImageIcon(this.getClass().getResource("/logo_appli.jpg")).getImage());
		//Initialisation de la JFRAME avec ses reglages - options
		setTitle(titreFenetre);
		Container contentPane = getContentPane();
		setBackground(backBlue);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(329,100);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setAlwaysOnTop(false);
		
		pseudo=ID;
	//Contenu de la JFrame :
		//Partie Pseudo
	
	passwordField = new JPasswordField(10);
	passwordField.setForeground(Color.WHITE);
	passwordField.setActionCommand(OK);
	passwordField.addFocusListener(listenerPassword);
	passwordField.setBackground(backField);
	passwordField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderBlue));
	
	JLabel labelPWD = new JLabel("Mot de Passe : ");
	labelPWD.setForeground(Color.WHITE);
	labelPWD.setLabelFor(passwordField);
	
	//Partie Bouton
		//Bouton OK
		JButton butOk = new JButton(OK);
		butOk.setBackground(buttBlue);
		butOk.setForeground(Color.WHITE);
		butOk.setFocusable(false);
		butOk.setBorderPainted(false);
		butOk.setActionCommand(OK);
		butOk.addActionListener(this);
		//Bouton EXIT
		JButton butExit = new JButton(EXIT);
		butExit.setBackground(buttBlue);
		butExit.setForeground(Color.WHITE);
		butExit.setFocusable(false);
		butExit.setBorderPainted(false);
		butExit.setActionCommand(EXIT);
		butExit.addActionListener(this);
		
		//PANNEAU BOUTON
		JComponent buttonPane = new JPanel();
		buttonPane.setBackground(backBlue);
		buttonPane.setLayout(new GridLayout(2,1,0,3));
		buttonPane.add(butOk);
		buttonPane.add(butExit);
		//PANNEAU TEXTE
		JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		textPane.setBackground(backBlue);
		textPane.add(labelPWD);
		textPane.add(passwordField);
		
		JPanel finalPane = new JPanel();
		finalPane.setBackground(backBlue);
		finalPane.add(textPane);
		finalPane.add(buttonPane);
		
		contentPane.add(finalPane);
	}
	
	public void addInfoListener(InfoConnectListener listener){
		m_infoListeners.add(listener);
	}

	@Override
public void actionPerformed(ActionEvent e) 
	{
		String cmd = e.getActionCommand();
		char[] mdp = passwordField.getPassword();
		String s = new String(mdp);
		if (OK.equals(cmd)) 
		{
				for(InfoConnectListener l : m_infoListeners){
					l.askForAdminConnect(pseudo, s, "127.0.0.1", 4444);
				}
				passwordField.setText("");
				this.setVisible(false);
		}
		else if(EXIT.equals(cmd))
		{
			this.setVisible(false);
		}
	}
	
FocusListener listenerPassword = new FocusListener() {
	public void focusGained(FocusEvent e) {
	   passwordField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, buttBlue));
	}

	public void focusLost(FocusEvent e) {
	   passwordField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderGrey));
	 }
		
	};
}
