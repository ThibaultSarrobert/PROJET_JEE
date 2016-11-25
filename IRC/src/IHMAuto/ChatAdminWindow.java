package IHMAuto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class ChatAdminWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String CHAT = "CHAT";
	private static final String DECO = "DECO";
	private static final String QUITTER = "QUITTER";
	private static final String AIDE = "AIDE";
	private static final String APROPOS = "APROPOS";
	private Color borderBlue = new Color(53,62,80);
	private Color buttBlue = new Color(15,114,176);
	private Color backField = new Color(20,25,34);
	private Color backBlueLight = new Color(40,50,68);
	public int deconection;
	Font font = new Font("Serial", Font.BOLD, 20);
	Font font2 = new Font("Serial", Font.BOLD, 18);
	
	
	public ChatAdminWindow(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200,200);
		setSize(1000,500);
		setResizable(false);
		
		// COntainer qui contient le BorderLayout
		
		Container contentPane = getContentPane();
		
// Rï¿½alisation des onglets du menu
		//Menu Outils
		JMenuBar m = new JMenuBar();
		m.setBackground(backBlueLight);
		//m.setBackground(backBlueLight);
		JMenu menu1 = new JMenu("☼");
		menu1.setForeground(buttBlue);
		menu1.setFont(font);
		JMenuItem deco = new JMenuItem("Se Deconnecter");
		menu1.add(deco);
		deco.setActionCommand(DECO);
		deco.addActionListener(this);
		// separe d'un trait
		menu1.addSeparator();
		JMenuItem quitter = new JMenuItem("Quitter");
		menu1.add(quitter);
		quitter.setActionCommand(QUITTER);
		quitter.addActionListener(this);
		m.add(Box.createHorizontalGlue());
		m.add(menu1);
		//Menu ?
		JMenu menu2 = new JMenu("?");
		menu2.setFont(font2);
		menu2.setForeground(buttBlue);
		JMenuItem aide = new JMenuItem("Aide");
		menu2.add(aide);
		aide.setActionCommand(AIDE);
		aide.addActionListener(this);
		JMenuItem apropos = new JMenuItem("A Propos");
		menu2.add(apropos);
		apropos.setActionCommand(APROPOS);
		apropos.addActionListener(this);
		m.add(menu2);
		contentPane.add(m,BorderLayout.NORTH);
		//setJMenuBar(m);
			
		
// Rï¿½alisation des objets de la fenetre
	
		// Creation des panneaux
		JPanel panneauChat = new JPanel(); //Panneau pour les messages du chat
		JPanel panneauTextUtilisateur = new JPanel(); //Panneau pour que l'utilisateur tape son texte
		JPanel panneauListe = new JPanel(); //Panneau avec la liste des personnes connectees et le bouton kick
		JPanel panneauFinal = new JPanel(); //Panneau qui regroupe touts les autres panneaux
		//Surligne les contours des panneaux pour les differencier
		m.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderBlue));
		panneauChat.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, borderBlue));
		panneauTextUtilisateur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, borderBlue));
		panneauListe.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, borderBlue));
		//COuleur de fond
		panneauChat.setBackground(backField);
		panneauListe.setBackground(backField);
		panneauTextUtilisateur.setBackground(backField);
		panneauChat.setLayout(new BorderLayout());
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
		panneauTextUtilisateur.setLayout(new BorderLayout(0, 0));
	
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
		//Ceci est la zone ou l'utilisateur ecrira le message avant de l'envoyer
		JTextField TextUtilisateur = new JTextField();
		panneauTextUtilisateur.add(TextUtilisateur, BorderLayout.CENTER);
		TextUtilisateur.setForeground(new Color(255, 255, 255));
		TextUtilisateur.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, borderBlue));
		TextUtilisateur.setBackground(backField);
		
		JPanel panel = new JPanel();
		panel.setBackground(backBlueLight);
		panneauTextUtilisateur.add(panel, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(backBlueLight);
		panneauTextUtilisateur.add(panel_1, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(backBlueLight);
		panneauTextUtilisateur.add(panel_2, BorderLayout.EAST);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(backBlueLight);
		panneauTextUtilisateur.add(panel_3, BorderLayout.WEST);
		
		
		
		
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
			        "Qui voulez-vous expulser ?",
			        "Kick",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        listeKick, 
			        listeKick[0]);
			
				       
		}
	}
}
