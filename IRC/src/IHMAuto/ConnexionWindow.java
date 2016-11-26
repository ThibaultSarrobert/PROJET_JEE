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
import javax.swing.JTextField;
import javax.swing.UIManager;


public class ConnexionWindow extends JFrame implements ActionListener, FocusListener{
	// Initialisation de nos variables 
	private static final long serialVersionUID = 1L;
	private static String OK = "OK";
	private static String HELP = "AIDE";
	private static String ADMIN = "ADMIN";
	private String titreFenetre = "Connexion";
	private JTextField IDField;
	private Color backBlue = new Color(29,34,44);
	private Color buttBlue = new Color(10,129,183);
	private Color backField = new Color(20,25,34);
	private Color borderGrey = new Color(53,62,80);
	private ArrayList<InfoConnectListener> m_infoListeners = new ArrayList<InfoConnectListener>();
	
	//Fin de l'initialisation de nos variables
	
	public ConnexionWindow() {
		setIconImage(new ImageIcon(this.getClass().getResource("/logo_appli.jpg")).getImage());
		Container contentPane = getContentPane();
		setTitle(titreFenetre);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200,200);
		setSize(400,120);
		setResizable(false);
		setVisible(true);
		
		@SuppressWarnings("unused")
		UIManager UI=new UIManager();
		UIManager.put("OptionPane.background", backField);
		UIManager.put("OptionPane.messageForeground", Color.white);
		UIManager.put("Button.background", buttBlue);
		UIManager.put("Button.foreground", Color.white);
		UIManager.put("Panel.background", backField);
		
		
	//Contenu de la JFrame :
	//Partie Pseudo
	IDField = new JTextField(15);
	IDField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderGrey)); //Bordure de couleur grise
	IDField.setBackground(backField); // fond de couleur backField
	IDField.setForeground(Color.white); // Texte de couleur blanche
	IDField.addFocusListener(listenerID); //Focus listener pour gerer la brillance du Field lorsque l'on est dessus
	JLabel labelID = new JLabel("Pseudo : "); //On met le mot pseudo
	labelID.setLabelFor(IDField); //IDField et le label du labelID
	labelID.setForeground(Color.white); //Le texte ecrit sera blanc

	//Partie Bouton
	//Bouton OK
	JButton butOk = new JButton(OK); //Bouton appele OK
	butOk.setFocusPainted(false); //Pas de contour quand on passe dessus
	butOk.setBorderPainted(false); //Pas de bord 
	butOk.setActionCommand(OK); //Action pour ouvrir la fenetre Chat si les id sont bon 
	butOk.addActionListener(this); // On ecoute ce bouton 
	butOk.setBackground(buttBlue); //Couleur du bouton buttBlue
	butOk.setForeground(Color.white); //Couleur du text blanc
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
	butAdmin.setBorderPainted(false);
	butAdmin.setActionCommand(ADMIN);
	butAdmin.addActionListener(this);
	butAdmin.setBackground(buttBlue);
	butAdmin.setForeground(Color.white);
	
	
	
	//PANNEAU TEXTE
	FlowLayout fl_textPane = new FlowLayout(FlowLayout.TRAILING); //Creation du FlowLayout
	fl_textPane.setHgap(20); //Pour que le contenu prenne toute la place de la fenetre
	JPanel textPane = new JPanel(fl_textPane); //Creation du panneau du texte
	textPane.add(labelID); //On ajoute le label ID
	textPane.add(IDField); //On ajoute le Field ID
	//PANNEAU BOUTON
	JComponent buttonPane = new JPanel(new GridLayout(3,1,0,3)); //Creation du panneau bouton en gridLayout
	buttonPane.add(butOk); //On insere le button ok 
	buttonPane.add(butAide); //" " aide
	buttonPane.add(butAdmin);//" " admin
	
	
	//Initi des couleurs de fond
	textPane.setBackground(backBlue);
	buttonPane.setBackground(backBlue);
	
	//PANNEAU FINAL
	JPanel finalPane = new JPanel(); //Creation du panneau final
	finalPane.setBackground(backBlue); //Couleur du fond backBlue
	finalPane.add(textPane); //On ajoute le panneau texte
	finalPane.add(buttonPane); //On ajoute le panneau bouton 
	
	contentPane.add(finalPane);//On ajoute le tout dans le contentPane
	setVisible(true);//On le rend visible
	}
	
	public void addInfoListener(InfoConnectListener listener){
		m_infoListeners.add(listener);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(OK.equals(cmd))
		{
			String ID = IDField.getText();
			if("".equals(ID)){				

				 JOptionPane.showMessageDialog(null,"Veuillez entrer un ID","Erreur",JOptionPane.ERROR_MESSAGE);
			}
			
			else if(ID.length()<3||ID.length()>15){
				JOptionPane.showMessageDialog(null,"Votre ID doit etre compris entre 3 et 15 charactères","Erreur",JOptionPane.ERROR_MESSAGE);
			}
			else{
				for(InfoConnectListener l : m_infoListeners){
					l.askForConnect(ID, "127.0.0.1", 4444);
				}
			}
		}
		else if(HELP.equals(cmd))
		{
			JOptionPane.showMessageDialog(null,"Votre pseudo doit étre compris entre 3 et  15 charactères\n "
					+ "Si il n'est pas accepté : c'est qu'il est deja utilisé\n", "Aide",JOptionPane.ERROR_MESSAGE);
		}
		else{
				String ID = IDField.getText();
				if("".equals(ID)){
					JOptionPane.showMessageDialog(null, "Veuillez entrer un ID pour l'admin","Erreur",JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					
				this.setFocusableWindowState(false);
				ConnexionAdminWindow A = new ConnexionAdminWindow();
				}
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



@Override
public void focusGained(FocusEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void focusLost(FocusEvent e) {
	// TODO Auto-generated method stub
	
}
}
