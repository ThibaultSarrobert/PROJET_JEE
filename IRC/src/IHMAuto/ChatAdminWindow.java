package IHMAuto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import java.awt.Component;
import javax.swing.ScrollPaneConstants;

public class ChatAdminWindow extends JFrame implements ActionListener, FocusListener,KeyListener {

	private static final long serialVersionUID = 1L;
	private static final String CHAT = "CHAT";
	private static final String DECO = "DECO";
	private static final String QUITTER = "QUITTER";
	private static final String AIDE = "AIDE";
	private static final String APROPOS = "APROPOS";
	private ArrayList<ChatListener> m_listeners = new ArrayList<ChatListener>();
	private static final String LIGNE = "LIGNE";
	private static final String ABSENT = "ABSENT";
	private static final String OCCUPE = "OCCUPE";
	JMenu menuStatut;
	private String statutLogo = "▶";
	private String statut = "En Ligne";
	private JTextField TextUtilisateur;
	private DefaultListModel<String> chat = new DefaultListModel<String>();
	private JList<String> chatList = null;
	private DefaultListModel<String> users = new DefaultListModel<String>();
	public static final Color borderBlue = new Color(53,62,80);
	public static final Color buttBlue = new Color(15,114,176);
	public static final Color backField = new Color(20,25,34);
	public static final Color backBlueLight = new Color(40,50,68);
	JScrollPane chatScrollPane;
	Font font = new Font("Serial", Font.BOLD, 22);
	Font font2 = new Font("Serial", Font.BOLD, 18);
	Font fontChat = new Font("Serial", Font.PLAIN, 16);
	Font fontUser = new Font("Serial", Font.PLAIN, 20);
	
	private class CustomCellRenderer extends JTextArea implements ListCellRenderer<String>{
		private static final long serialVersionUID = -8221786621557567653L;

		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) {
			this.setText(value.toString());
			this.setLineWrap(true);
			this.setEditable(false);
			this.setSize(list.getWidth(), list.getHeight());
			this.setFont(fontChat);

	         Color background;
	         Color foreground;
	         
	         // check if this cell is selected
	         if (isSelected) {
	             background = backField;
	             foreground = buttBlue;

	         // unselected, and not the DnD drop location
	         }
	         else {
	        	 if(index%2 == 1){
	        		 background = ChatClientWindow.backBlueLight;
	        	 }else{
	        		 background = ChatClientWindow.backField;
	        	 }
	             foreground = buttBlue;
	         }
	         
	         setBackground(background);
	         setForeground(foreground);

	         return this;
		}
		
	}
	
	
	public ChatAdminWindow(){
		
		setIconImage(new ImageIcon(this.getClass().getResource("/logo_appli.jpg")).getImage());
		
		setTitle("Connected as Admin - ");
		setBackground(Color.BLACK);
		getContentPane().setBackground(Color.BLUE);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Action lorsque l'on ferme la fenetre
		setLocation(200,200); //Localisation sur l'ecran a l'ouverture
		setSize(1000,500); //Taille de la fenetre
		setResizable(false);//Ne peut être ajuster sur la taille
				
		Container contentPane = getContentPane();// COntainer qui contient le BorderLayout
		
		// Realisation des onglets du menu
		//Creation d'une barre de menu
		JMenuBar menuBar = new JMenuBar(); //Bar appele menuBar
		menuBar.setBackground(backBlueLight);//Fond de couleur backBlueLight
		//On creer un panel pour laisser un vide avant le texte "chat general"
		JPanel panelVide1 = new JPanel();
		panelVide1.setMaximumSize(new Dimension(600, 0));
		menuBar.add(panelVide1);
		//On creer un label pour le texte 
		JLabel pseudoLabelBar = new JLabel("General Chat");//Label pour le texte dans la bar
		pseudoLabelBar.setFont(font);//On met la font defini par font
		pseudoLabelBar.setForeground(buttBlue);//On met le texte en couleur buttBlue
		menuBar.add(pseudoLabelBar);//On l'insere dans la barre 
		//AJOUT DE LA GLUE
		menuBar.add(Box.createHorizontalGlue());//On met de la glue pour que les menus soient a droite
		//Menu Statut
		menuStatut = new JMenu("∨");
		menuStatut.setForeground(Color.GREEN);
		menuStatut.setFont(font2);
			//Premier Item du menuStatut
			JMenuItem itemEnLigne = new JMenuItem("Online");//Item Deconnecter
			menuStatut.add(itemEnLigne);//On l'ajoute a l'onglet menuOutils
			itemEnLigne.setActionCommand(LIGNE);//On set la commande l'action DECO
			itemEnLigne.addActionListener(this);//On ecoute cet item
			//SEPARATION
			menuStatut.addSeparator();// separe d'un trait les deux items du menuStatut
			//2nd Item du menuStatut
			JMenuItem itemAbsent = new JMenuItem("Away");//Item Deconnecter
			menuStatut.add(itemAbsent);//On l'ajoute a l'onglet menuOutils
			itemAbsent.setActionCommand(ABSENT);//On set la commande l'action DECO
			itemAbsent.addActionListener(this);//On ecoute cet item	
			//SEPARATION
			menuStatut.addSeparator();// separe d'un trait les deux items du menuStatut
			//3eme Item du menuStatut
			JMenuItem itemOccupe = new JMenuItem("Busy");//Item Deconnecter
			menuStatut.add(itemOccupe);//On l'ajoute a l'onglet menuOutils
			itemOccupe.setActionCommand(OCCUPE);//On set la commande l'action DECO
			itemOccupe.addActionListener(this);//On ecoute cet item	
		menuBar.add(menuStatut);
		//Menu Outils
		JMenu menuOutils = new JMenu("☼");//premiere onglet du menu
		menuOutils.setForeground(buttBlue);//Texte de couleur buttBlue
		menuOutils.setFont(font);//On met la font defini par font
			//Premier Item du menu1
			JMenuItem itemDeco = new JMenuItem("Deconnection");//Item Deconnecter
			menuOutils.add(itemDeco);//On l'ajoute a l'onglet menuOutils
			itemDeco.setActionCommand(DECO);//On set la commande l'action DECO
			itemDeco.addActionListener(this);//On ecoute cet item
			//SEPARATION
			menuOutils.addSeparator();// separe d'un trait les deux items du menuOutils
			//Deuxieme item du menu
			JMenuItem quitter = new JMenuItem("Quit");//Item quitter
			menuOutils.add(quitter);//On ajoute a l'onglet menuOutils
			quitter.setActionCommand(QUITTER);//On set la commande l'action QUITTER
			quitter.addActionListener(this);//On ecoute cet item
			menuBar.add(menuOutils);
		//Menu ?
		JMenu menuHelp = new JMenu("?");
		menuHelp.setFont(font);
		menuHelp.setForeground(buttBlue);
			//Premier item aide
			JMenuItem aide = new JMenuItem("Help");
			menuHelp.add(aide);
			//SEPARATION
			menuHelp.addSeparator();// separe d'un trait les deux items du menuStatut
			aide.setActionCommand(AIDE);
			aide.addActionListener(this);
			//2nd item apropos
			JMenuItem apropos = new JMenuItem("About");
			menuHelp.add(apropos);
			apropos.setActionCommand(APROPOS);
			apropos.addActionListener(this);
			menuBar.add(menuHelp);
		//Menubar border
		menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderBlue));
		contentPane.add(menuBar,BorderLayout.NORTH);//On ajoute la bar du menu dans le contentPane
			
		
		// Realisation des objets de la fenetre
	
		// Creation des panneaux
		//PANNEAU ZONE DE CHAT
		JPanel panneauChat = new JPanel(new BorderLayout()); //Panneau pour les messages du chat
		panneauChat.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, borderBlue));
		panneauChat.setBackground(backField);

		@SuppressWarnings("deprecation")
		int H = new Date().getHours(); 
		@SuppressWarnings("deprecation")
		int M = new Date().getMinutes();
		
		chat.addElement(H+":"+M+" - "+ "Welcome ");

		panneauChat.setLayout(new BorderLayout(0, 0));
		
		chatScrollPane = new JScrollPane(this.chatList);
		chatScrollPane.setAutoscrolls(true);
		chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panneauChat.add(chatScrollPane, BorderLayout.CENTER);
		
		chatList = new JList<String>(chat);
		chatList.setCellRenderer(new CustomCellRenderer());
		chatList.setFont(fontUser);
		chatScrollPane.setViewportView(chatList);
		chatList.setBackground(backField);
		chatList.setForeground(buttBlue);
		chatScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, borderBlue));
		chatList.setAlignmentY(Component.TOP_ALIGNMENT);
		chatScrollPane.getVerticalScrollBar().setBackground(backField);
		
		// PANNEAU ZONE DE TEXT
		JPanel panneauTextUtilisateur = new JPanel(); //Panneau pour que l'utilisateur tape son texte
		panneauTextUtilisateur.setLayout(new BorderLayout(0, 0));
		panneauTextUtilisateur.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, borderBlue));
		panneauTextUtilisateur.setBackground(backField);
				//Ceci est la zone ou l'utilisateur ecrira le message avant de l'envoyer
				TextUtilisateur = new JTextField();
				TextUtilisateur.addFocusListener(this);
				TextUtilisateur.addKeyListener(this);
				TextUtilisateur.setForeground(Color.white);
				TextUtilisateur.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, borderBlue));
				TextUtilisateur.setBackground(backField);
				panneauTextUtilisateur.add(TextUtilisateur, BorderLayout.CENTER);
				//Creation de panneau vide autour de la zone de texte		
				JPanel panelNORD = new JPanel();
				panelNORD.setBackground(backBlueLight);
				panneauTextUtilisateur.add(panelNORD, BorderLayout.NORTH);	
				JPanel panelSUD = new JPanel();
				panelSUD.setBackground(backBlueLight);
				panneauTextUtilisateur.add(panelSUD, BorderLayout.SOUTH);		
				JPanel panelEST = new JPanel();
				panelEST.setBackground(backBlueLight);
				panneauTextUtilisateur.add(panelEST, BorderLayout.EAST);		
				JPanel panelOUEST = new JPanel();
				panelOUEST.setBackground(backBlueLight);
				panneauTextUtilisateur.add(panelOUEST, BorderLayout.WEST);		
		
		//PANNEAU DE LA LISTE DES PERSONNES CONNECTEES AVEC BOUTTON CHAT/KICK
		JPanel panneauListe = new JPanel(); //Panneau avec la liste des personnes connectees et le bouton kick
		panneauListe.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, borderBlue));
		panneauListe.setBackground(backField);
		GridBagLayout gbl_panneauListe = new GridBagLayout();
		gbl_panneauListe.columnWeights = new double[]{1.0};
		panneauListe.setLayout(gbl_panneauListe);
		
		//Panneau avec la liste des connectes
		JList<String> usersJList = new JList<String>(users);
		usersJList.setSelectionBackground(backField);
		usersJList.setSelectionForeground(buttBlue);
		usersJList.setFont(fontChat);
		JScrollPane userListScrollPane = new JScrollPane(usersJList);
		//usersJList.setVerticalTextPosition(SwingConstants.TOP);
		//usersJList.setVerticalAlignment(SwingConstants.TOP);
		//usersJList.setHorizontalAlignment(SwingConstants.LEFT);
		usersJList.setBackground(backField);
		usersJList.setForeground(buttBlue);
		usersJList.setAlignmentY(Component.TOP_ALIGNMENT);
		userListScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderBlue));
		userListScrollPane.getVerticalScrollBar().setBackground(backField);
		GridBagConstraints gbcL = new GridBagConstraints();
		gbcL.fill = GridBagConstraints.BOTH;
		gbcL.gridx=0;
		gbcL.weightx=0;
		gbcL.weighty=0.86;
		gbcL.gridy=0;
		panneauListe.add(userListScrollPane,gbcL);
		JButton butChat = new JButton("CHAT");
		butChat.setForeground(Color.WHITE);
		butChat.setBackground(buttBlue);
		butChat.setFocusPainted(false);
		butChat.setBorderPainted(false);
		GridBagConstraints gbcK = new GridBagConstraints();
		gbcK.gridx=0;
		gbcK.weightx=0;
		gbcK.weighty=0.14;
		gbcK.gridy=1;
		butChat.setActionCommand(CHAT);
		butChat.addActionListener(this);
		panneauListe.add(butChat,gbcK);
		
		//PANNEAU FINAL AVEC TOUTS LES AUTRES PANNEAUX
		JPanel panneauFinal = new JPanel(); //Panneau qui regroupe touts les autres panneaux
		
	
		//Ajout des panneaux dans le panneau final
		GridBagLayout gbl_panneauFinal = new GridBagLayout();
		gbl_panneauFinal.columnWeights = new double[]{0.0, 0.0};
		panneauFinal.setLayout(gbl_panneauFinal);
		GridBagConstraints gbcC = new GridBagConstraints();
		gbcC.gridx=0;
		gbcC.gridy=0;
		gbcC.fill=GridBagConstraints.BOTH;
		gbcC.weightx = 0.8;
		gbcC.weighty = 0.9;
		panneauFinal.add(panneauChat,gbcC);
		
		
		GridBagConstraints gbcLi = new GridBagConstraints();
		gbcLi.gridx=1;
		gbcLi.gridy=0;
		gbcLi.fill=GridBagConstraints.BOTH;
		gbcLi.weightx = 0.2;
		gbcLi.weighty = 1;
		gbcLi.gridheight=2;
		panneauFinal.add(panneauListe,gbcLi);
		GridBagConstraints gbcTF = new GridBagConstraints();
		gbcTF.gridx=0;
		gbcTF.gridy=1;
		gbcTF.fill=GridBagConstraints.BOTH;
		gbcTF.gridwidth = 1;
		gbcTF.weightx = 1;
		gbcTF.weighty = 0.1;
		panneauFinal.add(panneauTextUtilisateur,gbcTF);
		
		contentPane.add(panneauFinal);
	}
	
	public void addListener(ChatListener listener){
		m_listeners.add(listener);
		listener.askInitialization();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(QUITTER.equals(cmd))
		{
			int retour = JOptionPane.showConfirmDialog(getParent(), "Etes vous sur de vouloir quitter ?");
			if(retour==0){System.exit(0);}
			else if(retour==1){}
			else{}
			
		}
		else if(DECO.equals(cmd))
		{
			int retour = JOptionPane.showConfirmDialog(getParent(), "Etes vous sur de vouloir vous deconecter ?");
			if(retour==0){
				for(ChatListener l : m_listeners){
					l.askDeconnection();
				}
			}
			else if(retour==1){}
			else{}
		}
		else if(APROPOS.equals(cmd))
		{
			ImageIcon icon = new ImageIcon(ConnexionWindow.class.getResource("/logo_isen.png"));
			JOptionPane.showMessageDialog(
                    null,
                    "Version de l'IHM 1.0 \n"
        					+ "Chef de projet : Thibault SAROBERT\n"
        					+ "Architecte : Olivier ROMAN\n"
        					+ "Testeur/Valideur : Maxime MORREAU\n"
        					+ "Developeur : Pablo ORTEGA",
                    "A Propos", JOptionPane.INFORMATION_MESSAGE,
                    icon);
		}
		else if(AIDE.equals(cmd))
		{
			
			JOptionPane.showMessageDialog(getParent(), "Version de l'IHM 1.0 \n"
					+ "Chef de projet : Thibault SAROBERT\n"
					+ "Architecte : Olivier ROMAN\n"
					+ "Testeur/Valideur : Maxime MORREAU\n"
					+ "Developeur : Pablo ORTEGA");
		}
		else if(CHAT.equals(cmd))
		{
			ArrayList<String> listeChat = new ArrayList<String>();
			for(int i=0; i<users.size(); ++i){
				String[] parseNom = users.get(i).split(" - ");
				String pseudoChat = parseNom[0].substring(2);
				listeChat.add(pseudoChat);
			}
			@SuppressWarnings("unused")
			String quiChat = (String) JOptionPane.showInputDialog(this, 
			        "Avec qui voulez-vous lancer une conversation privée ?",
			        "Chat Perso",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        listeChat.toArray(), 
				    listeChat.get(0));
			
				       
		}
		else if(LIGNE.equals(cmd)){
			menuStatut.setForeground(Color.GREEN);
			for(ChatListener l : m_listeners){
				l.StatusChanged(2);
			}
		}
		else if(OCCUPE.equals(cmd)){
			menuStatut.setForeground(Color.RED);
			for(ChatListener l : m_listeners){
				l.StatusChanged(1);
			}
		}
		else if(ABSENT.equals(cmd)){
			menuStatut.setForeground(Color.ORANGE);
			for(ChatListener l : m_listeners){
				l.StatusChanged(0);
			}
		}
	}
	
	public void addMessage(String msg){
		@SuppressWarnings("deprecation")
		int H = new Date().getHours(); 
		@SuppressWarnings("deprecation")
		int M = new Date().getMinutes();
		
		int index = msg.indexOf(" : ");
		String msgPseudo = msg.substring(0, index);
		String msgMsg = msg.substring(index+3);
		chatList.ensureIndexIsVisible(chat.size()-1);
		chat.addElement(H+":"+M+" - "+msgPseudo+"\n"+"· "+msgMsg);
	}
	
	private void sendMessage(String msg){
		for(ChatListener l : m_listeners){
			l.sendMessage(msg);
		}
	}
	
	public void addUser(String nom){
		users.addElement(statutLogo+" "+nom+ " - "+statut);
	}
	
	public void supprUser(String nom){
		for(int i=0; i<users.size(); ++i){
			if(users.get(i).contains(nom)){
				users.remove(i);
			}
		}
	}
	
	public void changeStatus(String nom, int status){
		for(int i=0; i<users.size(); ++i){
			String[] parseNom = users.get(i).split(" - ");
			String pseudoPars = parseNom[0].substring(2);
			if(pseudoPars.equals(nom)){
				String[] tmp  = users.get(i).split(" - ");
				switch(status){
				case 0:
					users.set(i, tmp[0]+" - Absent");
					break;
				case 1:
					users.set(i, tmp[0]+" - Occupé");
					break;
				case 2:
					users.set(i, tmp[0]+" - En Ligne");
					break;
				}
			}
		}
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		TextUtilisateur.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, buttBlue));
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		TextUtilisateur.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderBlue));
		
	}
	

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER){
			if(TextUtilisateur.getText().isEmpty()){}
			else{
				this.sendMessage(TextUtilisateur.getText());
				TextUtilisateur.setText("");
			}
        }
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
