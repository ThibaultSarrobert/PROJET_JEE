package IHMAuto;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ConnexionWindow extends JFrame implements ActionListener, FocusListener{
	private static final long serialVersionUID = 1L;
	private static String OK = "OK";
	private static String HELP = "AIDE";
	private static String ADMIN = "ADMIN";
	private JTextField serveurField;
	private JTextField IDField;
	private JTextField portField;
	private Color backBlue = new Color(29,34,44);
	private Color buttBlue = new Color(10,129,183);
	private Color backField = new Color(20,25,34);
	private Color borderGrey = new Color(53,62,80);
	public JMenu menu1;
	public JMenu menu2;
	
	public ConnexionWindow() {
		Container contentPane = getContentPane();
		setTitle("Connexion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200,200);
		setSize(700,120);
		setResizable(false);
		setVisible(true);
		
	//Contenu de la JFrame :
	//Partie Pseudo
	IDField = new JTextField(10);
	IDField.setToolTipText("");
	IDField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderGrey));
	IDField.setBackground(backField);
	IDField.setForeground(new Color(255, 255, 255));
	IDField.addFocusListener(listenerID);
	JLabel labelID = new JLabel("Pseudo : ");
	labelID.setLabelFor(IDField);
	labelID.setForeground(Color.white);
	//Partie Serveur IP
	serveurField = new JTextField(10);
	serveurField.setBackground(backField);
	serveurField.setForeground(Color.white);
	serveurField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderGrey));
	serveurField.addFocusListener(listenerServ);
	JLabel labelServ = new JLabel("IP du server : ");
	labelServ.setLabelFor(serveurField);
	labelServ.setForeground(Color.white);
	//Partie Port
	portField = new JTextField(5);
	portField.setBackground(backField);
	portField.setForeground(Color.white);
	portField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderGrey));
	portField.addFocusListener(listenerPort);
	JLabel labelPort = new JLabel("Port : ");
	labelPort.setLabelFor(portField);
	labelPort.setForeground(Color.white);

	//Partie Bouton
	//Bouton OK
	JButton butOk = new JButton(OK);
	butOk.setFocusPainted(false);
	butOk.setBorderPainted(false);
	butOk.setActionCommand(OK);
	butOk.addActionListener(this);
	butOk.setBackground(buttBlue);
	butOk.setForeground(new Color(255, 255, 255));
	
	//Bouton AIDE
	JButton butAide = new JButton(HELP);
	butAide.setFocusPainted(false);
	butAide.setActionCommand(HELP);
	butAide.addActionListener(this);
	butAide.setBackground(buttBlue);
	butAide.setForeground(Color.white);
	butAide.setBorderPainted(false);
	//Bouton ADMIN
	JButton butAdmin = new JButton(ADMIN);
	butAdmin.setFocusPainted(false);
	butAdmin.setActionCommand(ADMIN);
	butAdmin.addActionListener(this);
	butAdmin.setBackground(buttBlue);
	butAdmin.setForeground(Color.white);
	butAdmin.setBorderPainted(false);
	
	
	
	//PANNEAU BOUTON
	JComponent buttonPane = new JPanel();
	buttonPane.setLayout(new GridLayout(3,1,0,3));
	buttonPane.add(butOk);
	buttonPane.add(butAide);
	buttonPane.add(butAdmin);
	//PANNEAU TEXTE
	FlowLayout fl_textPane = new FlowLayout(FlowLayout.TRAILING);
	fl_textPane.setHgap(20);
	JPanel textPane = new JPanel(fl_textPane);
	textPane.add(labelID);
	textPane.add(IDField);
	textPane.add(labelServ);
	textPane.add(serveurField);
	textPane.add(labelPort);
	textPane.add(portField);
	
	//Initi des couleurs de fond
	textPane.setBackground(backBlue);
	buttonPane.setBackground(backBlue);
	
	//PANNEAU FINAL
	JPanel finalPane = new JPanel();
	finalPane.setBackground(backBlue);
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
			@SuppressWarnings("unused")
			AideConnexionDialog AC = new AideConnexionDialog();
		}
		else{
			this.setVisible(false);
			@SuppressWarnings("unused")
			ConnexionAdminWindow A = new ConnexionAdminWindow();
			}
		}
	
FocusListener listenerID = new FocusListener() {
	public void focusGained(FocusEvent e) {
	   IDField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, buttBlue));
	}

	      public void focusLost(FocusEvent e) {
	    	  IDField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderGrey));
	      }
	
};
FocusListener listenerServ = new FocusListener() {
    public void focusGained(FocusEvent e) {
    	serveurField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, buttBlue));
    }

    public void focusLost(FocusEvent e) {
    	serveurField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderGrey));
    }

};
FocusListener listenerPort = new FocusListener() {
    public void focusGained(FocusEvent e) {
    	portField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, buttBlue));
    }

    public void focusLost(FocusEvent e) {
    	portField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderGrey));
    }

};

@Override
public void focusGained(FocusEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void focusLost(FocusEvent e) {
	// TODO Auto-generated method stub
	
}
}
