package IHMAuto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


public class ChatPersoWindow extends JFrame implements ActionListener, FocusListener,KeyListener{
	public static final Color borderBlue = new Color(53,62,80);
	public static final Color buttBlue = new Color(15,114,176);
	public static final Color backField = new Color(20,25,34);
	public static final Color backBlueLight = new Color(40,50,68);
	private static final String BAN = "BAN";
	private static final String SEND = "SEND";
	private DefaultListModel<String> chat = new DefaultListModel<String>();
	private JList<String> chatList = null;
	JScrollPane chatScrollPane;
	Font font = new Font("Monospaced", Font.BOLD, 27);
	Font font2 = new Font("Serial", Font.BOLD, 18);
	Font fontChat = new Font("Serial", Font.PLAIN, 16);
	Font fontUser = new Font("Serial", Font.PLAIN, 20);
	JTextField TextUtilisateur;

	private static final long serialVersionUID = 1L;
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
	         
	         
	         if(index%2 == 1){
        		 background = ChatClientWindow.backBlueLight;
        	 }else{
        		 background = ChatClientWindow.backField;
        	 }
	         // check if this cell is selected
	         if (isSelected) {
	             foreground = Color.red;
	         }
	         else {
	             foreground = Color.white;
	         }
	         
	         setBackground(background);
	         setForeground(foreground);

	         return this;
		}
		
	}

	public ChatPersoWindow(String pseudo) {
		setIconImage(new ImageIcon(this.getClass().getResource("/logo_appli.jpg")).getImage());
		setTitle("Chating with : "+pseudo);
		setBackground(Color.BLACK);
		getContentPane().setBackground(Color.BLUE);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);//Action lorsque l'on ferme la fenetre
		setLocation(600,100); //Localisation sur l'ecran a l'ouverture
		setSize(400,700); //Taille de la fenetre
		setResizable(false);//Ne peut etre ajuster sur la taille
				
		Container contentPane = getContentPane();// COntainer qui contient le BorderLayout
		
		
		JMenuBar menuBar = new JMenuBar(); //Bar appele menuBar
		menuBar.setBackground(backBlueLight);//Fond de couleur backBlueLight
		//On creer un panel pour laisser un vide avant le texte du pseudo
		JPanel panelVide1 = new JPanel();
		panelVide1.setMaximumSize(new Dimension(600, 0));
		menuBar.add(panelVide1);
		//On creer un label pour le texte 
		String statut = "Away";
		JLabel pseudoLabelBar = new JLabel("  "+pseudo+" ");//Label pour le texte dans la bar
		pseudoLabelBar.setFont(font);//On met la font defini par font
		pseudoLabelBar.setForeground(buttBlue);//On met le texte en couleur buttBlue
		menuBar.add(pseudoLabelBar);//On l'insere dans la barre 
		JLabel statutLabelBar = new JLabel(" is "+statut);
		statutLabelBar.setFont(fontChat);//On met la font defini par font
		statutLabelBar.setForeground(buttBlue);//On met le texte en couleur buttBlue
		menuBar.add(statutLabelBar);//On l'insere dans la barre 
		//AJOUT DE LA GLUE
		menuBar.add(Box.createHorizontalGlue());//On met de la glue pour que les menus soient a droite
		JMenu banMenu = new JMenu("*  ");
		banMenu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
            	int retour = JOptionPane.showConfirmDialog(getParent(), "Are you sure to ban "+pseudo+" ?\n"
            			+ "You will not be able to chat to him");
    			if(retour==0){}
    			else if(retour==1){}
    			else{}
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });
		banMenu.setFont(fontChat);//On met la font defini par font
		banMenu.setForeground(Color.red);//On met le texte en couleur buttBlue
		menuBar.add(banMenu);//On l'insere dans la barre 
		menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderBlue));
		contentPane.add(menuBar,BorderLayout.NORTH);//On ajoute la bar du menu dans le contentPane
		
		
		// Realisation des objets de la fenetre
		
			// Creation des panneaux
			//PANNEAU ZONE DE CHAT
			JPanel panneauChat = new JPanel(new BorderLayout()); //Panneau pour les messages du chat
			panneauChat.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, borderBlue));
			panneauChat.setBackground(backField);
			
			chatList = new JList<String>(chat);
			chatScrollPane = new JScrollPane(this.chatList);
			chatScrollPane.setAutoscrolls(true);
			chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			panneauChat.add(chatScrollPane, BorderLayout.CENTER);
			

			chatList = new JList<String>(chat);
			chatList.setAutoscrolls(true);
			chatList.setCellRenderer(new CustomCellRenderer());
			chatList.setFont(fontUser);
			chatScrollPane.setViewportView(chatList);
			chatList.setBackground(backField);
			chatList.setForeground(Color.WHITE);
			chatScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, borderBlue));
			chatList.setAlignmentY(Component.TOP_ALIGNMENT);
			chatScrollPane.getVerticalScrollBar().setBackground(backField);
			
			@SuppressWarnings("deprecation")
			int H = new Date().getHours(); 
			@SuppressWarnings("deprecation")
			int M = new Date().getMinutes();
			
			chat.addElement("   "+H+":"+M+" - "+ "You are chating with "+pseudo);
			chat.addElement(" you can banish him in the red menu above");
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
					JPanel panelEST = new JPanel(new BorderLayout());
					panelEST.setBackground(backBlueLight);
					JButton butSend = new JButton("SEND");
					butSend.setActionCommand(SEND);
					butSend.addActionListener(this);
					butSend.setForeground(Color.WHITE);
					butSend.setBackground(buttBlue);
					butSend.setFocusPainted(false);
					butSend.setBorderPainted(false);
					panelEST.add(butSend,BorderLayout.CENTER);
					panneauTextUtilisateur.add(panelEST, BorderLayout.EAST);		
					JPanel panelOUEST = new JPanel();
					panelOUEST.setBackground(backBlueLight);
					panneauTextUtilisateur.add(panelOUEST, BorderLayout.WEST);
			
			
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
			
			GridBagConstraints gbcTF = new GridBagConstraints();
			gbcTF.gridx=0;
			gbcTF.gridy=1;
			gbcTF.fill=GridBagConstraints.BOTH;
			gbcTF.gridwidth = 1;
			gbcTF.weightx = 1;
			gbcTF.weighty = 0.1;
			panneauFinal.add(panneauTextUtilisateur,gbcTF);
			
			contentPane.add(panneauFinal);
		this.setVisible(true);
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER){
			if(TextUtilisateur.getText().isEmpty()){}
			else if(TextUtilisateur.getText().length()>255){
				JOptionPane.showMessageDialog(getParent(), "Maximum size reached (255 characters)","Error",JOptionPane.ERROR_MESSAGE);
			}
			else{
				
				chat.addElement("  coucou");
				//this.sendMessage(TextUtilisateur.getText());
				TextUtilisateur.setText("");
			}
		
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
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
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(SEND.equals(cmd)){
				if(TextUtilisateur.getText().isEmpty()){}
				else if(TextUtilisateur.getText().length()>255){
					JOptionPane.showMessageDialog(getParent(), "Maximum size reached (255 characters)","Error",JOptionPane.ERROR_MESSAGE);
				}
				else{
					
					chat.addElement("  coucou");
					//this.sendMessage(TextUtilisateur.getText());
					TextUtilisateur.setText("");
				}
		}
	}

}
