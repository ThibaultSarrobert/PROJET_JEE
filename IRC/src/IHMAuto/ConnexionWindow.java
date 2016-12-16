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
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.ini4j.Ini;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

import server.DataBaseManager;


public class ConnexionWindow extends JFrame implements ActionListener, FocusListener{
	// Initialisation de nos variables 
	private static final long serialVersionUID = 1L;
	private static  String FB = "Facebook";
	private static String OK = "OK";
	private static String HELP = "HELP";
	private static String ADMIN = "ADMIN";
	private String titreFenetre = "Connection";
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
		setSize(500,120);
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
	//Bouton FACEBOOK
	JButton butFB = new JButton(FB);
	butFB.setFocusPainted(false);
	butFB.setBorderPainted(false);
	butFB.setActionCommand(FB);
	butFB.addActionListener(this);
	butFB.setBackground(buttBlue);
	butFB.setForeground(Color.white);
	
	
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
	//PANNEAU Facebook
	JComponent fbPane = new JPanel(new GridLayout(1,1,0,3)); //Creation du FlowLayout
	fbPane.add(butFB);
	
	//Initi des couleurs de fond
	textPane.setBackground(backBlue);
	buttonPane.setBackground(backBlue);
	fbPane.setBackground(backBlue);
	
	//PANNEAU FINAL
	JPanel finalPane = new JPanel(); //Creation du panneau final
	finalPane.setBackground(backBlue); //Couleur du fond backBlue
	finalPane.add(textPane); //On ajoute le panneau texte
	finalPane.add(buttonPane); //On ajoute le panneau bouton 
	finalPane.add(fbPane);
	
	contentPane.add(finalPane);//On ajoute le tout dans le contentPane
	setVisible(true);//On le rend visible
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

				 JOptionPane.showMessageDialog(null,"Please enter an ID","Error",JOptionPane.ERROR_MESSAGE);
			}
			
			else if(ID.length()<3||ID.length()>15){
				JOptionPane.showMessageDialog(null,"Your ID must be between 3 and 15 characters","Error",JOptionPane.ERROR_MESSAGE);
			}
			else if(ID.contains(" ")||ID.contains("!")||ID.contains("|")){
				JOptionPane.showMessageDialog(null,"Your ID must not contain spaces, ! or | ","Errorr",JOptionPane.ERROR_MESSAGE);
			}
			else{
				DataBaseManager.ServerCoord choix = choisirServeur();
				if(choix != null){
					for(InfoConnectListener l : m_infoListeners){
						l.askForConnect(ID, choix.getHostname(), choix.getClientPort());
					}
				}
			}
		}
		else if(HELP.equals(cmd))
		{
			JOptionPane.showMessageDialog(null,"Your ID must be between 3 and 15 characters\n "
					+ "If it is not accepted: it is already used\n", "Help",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(ADMIN.equals(cmd)){
				String ID = IDField.getText();
				if("".equals(ID)){
					JOptionPane.showMessageDialog(null, "Please enter an ID","Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(ID.length()<3||ID.length()>15){
					JOptionPane.showMessageDialog(null,"Your ID must be between 3 and 15 characters","Error",JOptionPane.ERROR_MESSAGE);
				}
				else{
					
				this.setFocusableWindowState(false);
				ConnexionAdminWindow CAW = new ConnexionAdminWindow(ID);
					
				for(InfoConnectListener l : m_infoListeners){
						CAW.addInfoListener(l);
					}
				}
			}
		else if(FB.equals(cmd)){
				IDField.setText(facebookConnection());
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

public JTextField getIDField()
{
	return IDField;
}

public String facebookConnection(){
            boolean endOfWhile = true;
            String name="";
            String domain = "http://www.google.fr";
            String appID = "580495938817741";
            String authUrl = "https://graph.facebook.com/oauth/authorize?type=user_agent&client_id="+appID+"&redirect_uri="+domain+"&scope=user_about_me,"
                + "user_actions.books,user_actions.fitness,user_actions.music,user_actions.news,user_actions.video,user_birthday,user_education_history,"
                + "user_events,user_photos,user_friends,user_games_activity,user_hometown,user_likes,user_location,user_photos,user_relationship_details,"
                + "user_relationships,user_religion_politics,user_status,user_tagged_places,user_videos,user_website,user_work_history,ads_management,ads_read,email,"
                + "manage_pages,publish_actions,read_insights,read_page_mailboxes,rsvp_event";           
            String accessToken;

            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            
            WebDriver driver = new ChromeDriver();
            driver.get(authUrl);

            while(endOfWhile==true){
                if(!driver.getCurrentUrl().contains("facebook.com")){
                    String url = driver.getCurrentUrl();
                    accessToken = url.replaceAll(".*#access_token=(.+)&.*", "$1");
                    
                    driver.quit();

                    FacebookClient fbClient = new DefaultFacebookClient(accessToken);
                    User me = fbClient.fetchObject("me", User.class);

                    name=me.getName();
                    endOfWhile = false;
                }
            }
            if(name.contains("")){
                name=name.replace(" ", "");
            }
           
            if(name.length()>15){
                name=name.substring(0, 15);
            }
            return name;
    }
}

