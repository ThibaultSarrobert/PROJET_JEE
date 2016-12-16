package IHMAuto;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
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

import org.ini4j.Ini;

import server.DataBaseManager;

public class ConnexionAdminWindow extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private static String OK = "OK";
	private static String EXIT = "EXIT";
	private String titreFenetre = "Admin Connection";
	private JPasswordField passwordField;
	private Color backBlue = new Color(29,34,44);
	private Color buttBlue = new Color(10,129,183);
	private Color backField = new Color(20,25,34);
	private Color borderBlue = new Color(53,62,80);
	private Color borderGrey = new Color(53,62,80);
	private String pseudo = new String(); 
	private ConnexionWindow CWT;
	private ArrayList<InfoConnectListener> m_infoListeners = new ArrayList<InfoConnectListener>();
	
	public ConnexionAdminWindow(String ID,ConnexionWindow CW){
		CWT=CW;
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
	
	JLabel labelPWD = new JLabel("Password : ");
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
	
	private DataBaseManager.ServerCoord choisirServeur(){
		try {
			Ini inifile = new Ini(new File("config.ini"));
			Ini.Section dbsection = inifile.get("database");
			String db_hostname = dbsection.get("hostname");
			int db_port = dbsection.get("port", int.class);
			String db_name = dbsection.get("name");
			
			DataBaseManager db = new DataBaseManager(db_hostname, db_port, db_name);
			ArrayList<DataBaseManager.ServerCoord> serverlist = db.getServerList();
			if(serverlist.size() == 1){
				return serverlist.get(0);
			}else{
			return (DataBaseManager.ServerCoord) JOptionPane.showInputDialog(this, 
			        "Which server do you want to connect to ?",
			        "Server Choices",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        serverlist.toArray(), 
				    serverlist.get(0));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error Acquiring the List of Servers","Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	@Override
public void actionPerformed(ActionEvent e) 
	{
		String cmd = e.getActionCommand();
		char[] mdp = passwordField.getPassword();
		String s = new String(mdp);
		if(OK.equals(cmd)) 
		{
			DataBaseManager.ServerCoord choix = choisirServeur();
			if(choix != null){
				for(InfoConnectListener l : m_infoListeners){
					l.askForAdminConnect(pseudo, s, choix.getHostname(), choix.getClientPort());
				}
				
			}
			//this.setVisible(false);
			passwordField.setText("");
			
		}
		else if(EXIT.equals(cmd))
		{
			this.dispose();
			CWT.setVisible(true);
			
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
