package IHMv2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatClientWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String KICK = "KICK";
	private static final String DECO = "DECO";
	private static final String QUITTER = "QUITTER";
	private static final String AIDE = "AIDE";
	private static final String APROPOS = "APROPOS";
	public int deconection;
	public ChatClientWindow(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200,200);
		setSize(1000,500);
		setResizable(false);
		
		// COntainer qui contient le BorderLayout
		
		Container contentPane = getContentPane();
		
// R�alisation des onglets du menu
		//Menu Outils
		JMenuBar m = new JMenuBar();
		JMenu menu1 = new JMenu("Outils");
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
		m.add(menu1);
		//Menu ?
		JMenu menu2 = new JMenu("?");
		JMenuItem aide = new JMenuItem("Aide");
		menu2.add(aide);
		aide.setActionCommand(AIDE);
		aide.addActionListener(this);
		JMenuItem apropos = new JMenuItem("A Propos");
		menu2.add(apropos);
		apropos.setActionCommand(APROPOS);
		apropos.addActionListener(this);
		m.add(menu2);
		setJMenuBar(m);
			
		
// R�alisation des objets de la fenetre
	
		// Creation des panneaux
		JPanel panneauChat = new JPanel(); //Panneau pour les messages du chat
		JPanel panneauTextUtilisateur = new JPanel(); //Panneau pour que l'utilisateur tape son texte
		JPanel panneauListe = new JPanel(); //Panneau avec la liste des personnes connectees et le bouton kick
		JPanel panneauFinal = new JPanel(); //Panneau qui regroupe touts les autres panneaux
		//Surligne les contours des panneaux pour les differencier
		panneauChat.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		panneauTextUtilisateur.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		panneauListe.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));		
		
		
		//Ceci est la zone ou sera ecrit les messages du chat
		JLabel textChat = new JLabel("Pseudo -> Message envoyer\n");
		panneauChat.setLayout(new BorderLayout());
		panneauChat.add(textChat,BorderLayout.WEST);
		
		
		//Les contraintes du GridBagLayout
		//Contraintes
		GridBagConstraints gbc = new GridBagConstraints();
		//Panneau du Text en Layout pour avoir le bouton et la zone de texte
		panneauTextUtilisateur.setLayout(new GridBagLayout());
		panneauListe.setLayout(new GridBagLayout());
		//Ceci est la zone ou l'utilisateur ecrira le message avant de l'envoyer
		JTextField TextUtilisateur = new JTextField();
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.weightx=0.95;
		gbc.weighty=0.9;
		panneauTextUtilisateur.add(TextUtilisateur,gbc);
		//Zone du bouton envoyer
		JButton send = new JButton("ENVOYER");
		gbc.gridx=1;
		gbc.weightx=0.05;
		gbc.weighty=0;
		gbc.gridy=1;
		panneauTextUtilisateur.add(send,gbc);
		
		//Panneau avec la liste des connectes
		JLabel ListeC = new JLabel("Pseudo est ABSENT");
		gbc.gridx=0;
		gbc.weightx=0;
		gbc.weighty=0.95;
		gbc.gridy=0;
		panneauListe.add(ListeC,gbc);
		JButton butKick = new JButton("KICK");
		gbc.gridx=0;
		gbc.weightx=0;
		gbc.weighty=0.05;
		gbc.gridy=1;
		butKick.setActionCommand(KICK);
		butKick.addActionListener(this);
		panneauListe.add(butKick,gbc);
		
		
	
		//Ajout des panneaux dans le panneau final
		panneauFinal.setLayout(new GridBagLayout());
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.weightx = 0.8;
		gbc.weighty = 0.9;
		panneauFinal.add(panneauChat,gbc);
		gbc.gridx=1;
		gbc.gridy=0;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.weightx = 0.2;
		gbc.weighty = 1;
		panneauFinal.add(panneauListe,gbc);
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		panneauFinal.add(panneauTextUtilisateur,gbc);
		
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
			@SuppressWarnings("unused")
			ConnexionWindow deco = new ConnexionWindow();
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
		else if(KICK.equals(cmd))
		{
			final String[] listeKick = { "Qui", "Michel", "Vero", "les PD" };
			String quiKick = (String) JOptionPane.showInputDialog(this, 
			        "Qui voulez-vous expluser ?",
			        "KICK",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        listeKick, 
			        listeKick[0]);
			
				       
		}
	}
}
