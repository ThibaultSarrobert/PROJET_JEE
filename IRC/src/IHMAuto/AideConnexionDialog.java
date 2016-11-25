package IHMAuto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class AideConnexionDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private Color backField = new Color(20,25,34);

	public AideConnexionDialog() {
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		setTitle("A Propos");
		setResizable(false);
		setSize(345,170);
		setLocation(300,300);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(backField);
		setVisible(true);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JTextArea aideConnexionTexte = new JTextArea();
			aideConnexionTexte.setEditable(false);
			aideConnexionTexte.setForeground(Color.WHITE);
			aideConnexionTexte.setBackground(backField);
			aideConnexionTexte.setText("Votre pseudo doit étre compris entre 3 et  15 charactères\n "
					+ "Si il n'est pas accepté : c'est qu'il est deja utilisé\n");
			contentPanel.add(aideConnexionTexte);
		}
		
		setVisible(true);
	}

}
