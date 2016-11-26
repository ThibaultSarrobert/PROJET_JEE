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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.SwingConstants;

public class ChatClientWindow extends JFrame implements ActionListener, FocusListener,KeyListener {

	private static final long serialVersionUID = 1L;
	private static final String CHAT = "CHAT";
	private static final String DECO = "DECO";
	private static final String QUITTER = "QUITTER";
	private static final String AIDE = "AIDE";
	private static final String APROPOS = "APROPOS";
	private JTextField TextUtilisateur;
	private Color borderBlue = new Color(53,62,80);
	private Color buttBlue = new Color(15,114,176);
	private Color backField = new Color(20,25,34);
	private Color backBlueLight = new Color(40,50,68);
	Font font = new Font("Serial", Font.BOLD, 20);
	Font font2 = new Font("Serial", Font.BOLD, 18);
	
	
	public ChatClientWindow(String pseudo){
		setTitle("Connecté en tant que Client - "+pseudo);
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
		JLabel pseudoLabelBar = new JLabel("Chat General");//Label pour le texte dans la bar
		pseudoLabelBar.setFont(font);//On met la font defini par font
		pseudoLabelBar.setForeground(buttBlue);//On met le texte en couleur buttBlue
		menuBar.add(pseudoLabelBar);//On l'insere dans la barre 
		//AJOUT DE LA GLUE
		menuBar.add(Box.createHorizontalGlue());//On met de la glue pour que les menus soient a droite
				
		//Menu Outils
		JMenu menuOutils = new JMenu("☼");//premiere onglet du menu
		menuOutils.setForeground(buttBlue);//Texte de couleur buttBlue
		menuOutils.setFont(font);//On met la font defini par font
			//Premier Item du menu1
		JMenuItem itemDeco = new JMenuItem("Se Deconnecter");//Item Deconnecter
		menuOutils.add(itemDeco);//On l'ajoute a l'onglet menuOutils
		itemDeco.setActionCommand(DECO);//On set la commande l'action DECO
		itemDeco.addActionListener(this);//On ecoute cet item
		//SEPARATION
		menuOutils.addSeparator();// separe d'un trait les deux items du menuOutils
			//Deuxieme item du menu
		JMenuItem quitter = new JMenuItem("Quitter");//Item quitter
		menuOutils.add(quitter);//On ajoute a l'onglet menuOutils
		quitter.setActionCommand(QUITTER);//On set la commande l'action QUITTER
		quitter.addActionListener(this);//On ecoute cet item
		menuBar.add(menuOutils);
		//Menu ?
		JMenu menuHelp = new JMenu("?");
		menuHelp.setFont(font2);
		menuHelp.setForeground(buttBlue);
		JMenuItem aide = new JMenuItem("Aide");
		menuHelp.add(aide);
		aide.setActionCommand(AIDE);
		aide.addActionListener(this);
		JMenuItem apropos = new JMenuItem("A Propos");
		menuHelp.add(apropos);
		apropos.setActionCommand(APROPOS);
		apropos.addActionListener(this);
		menuBar.add(menuHelp);
		menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderBlue));
		contentPane.add(menuBar,BorderLayout.NORTH);//On ajoute la bar du menu dans le contentPane
			
		
		// Realisation des objets de la fenetre
	
		// Creation des panneaux
		//PANNEAU ZONE DE CHAT
		JPanel panneauChat = new JPanel(); //Panneau pour les messages du chat
		panneauChat.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, borderBlue));
		panneauChat.setBackground(backField);
		
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
		JLabel ListeC = new JLabel("Pseudo est ABSENT");
		ListeC.setVerticalTextPosition(SwingConstants.TOP);
		ListeC.setVerticalAlignment(SwingConstants.TOP);
		ListeC.setHorizontalAlignment(SwingConstants.LEFT);
		ListeC.setAlignmentY(Component.TOP_ALIGNMENT);
		GridBagConstraints gbcL = new GridBagConstraints();
		gbcL.anchor = GridBagConstraints.WEST;
		gbcL.gridx=0;
		gbcL.weightx=0;
		gbcL.weighty=0.95;
		gbcL.gridy=0;
		panneauListe.add(ListeC,gbcL);
		JButton butChat = new JButton("CHAT");
		butChat.setForeground(Color.WHITE);
		butChat.setBackground(buttBlue);
		butChat.setFocusPainted(false);
		butChat.setBorderPainted(false);
		GridBagConstraints gbcK = new GridBagConstraints();
		gbcK.gridx=0;
		gbcK.weightx=0;
		gbcK.weighty=0.05;
		gbcK.gridy=1;
		butChat.setActionCommand(CHAT);
		butChat.addActionListener(this);
		panneauListe.add(butChat,gbcK);
		
		//PANNEAU FINAL AVEC TOUTS LES AUTRES PANNEAUX
		JPanel panneauFinal = new JPanel(); //Panneau qui regroupe touts les autres panneaux
		
	
		//Ajout des panneaux dans le panneau final
		panneauFinal.setLayout(new GridBagLayout());
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
		
		
		setVisible(true);
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
			if(retour==0){setVisible(false);}
			else if(retour==1){}
			else{}
		}
		else if(APROPOS.equals(cmd))
		{
			JOptionPane.showMessageDialog(getParent(), "Version de l'IHM 1.0 \n "
					+ "Chef de projet : Thibault SAROBERT\n"
					+ "Architecte : Olivier ROMAN\n"
					+ "Testeur/Valideur : Maxime MORREAU\n"
					+ "Developeur : Pablo ORTEGA");
		}
		else if(AIDE.equals(cmd))
		{
			JOptionPane.showMessageDialog(getParent(), "Version de l'IHM 1.0 \n "
					+ "Chef de projet : Thibault SAROBERT\n"
					+ "Architecte : Olivier ROMAN\n"
					+ "Testeur/Valideur : Maxime MORREAU\n"
					+ "Developeur : Pablo ORTEGA");
		}
		else if(CHAT.equals(cmd))
		{
			final String[] listeKick = { "Qui", "Michel", "Vero", "les PD" };
			@SuppressWarnings("unused")
			String quiKick = (String) JOptionPane.showInputDialog(this, 
			        "Avec qui voulez-vous lancer une conversation privée ?",
			        "Chat Perso",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        listeKick, 
			        listeKick[0]);
			
				       
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
			TextUtilisateur.setText("");
        	System.out.println("j'ai appuy� sur entr�e");
            //sendMessage();
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
