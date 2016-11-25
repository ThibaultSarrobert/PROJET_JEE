package IHMAuto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

public class AProposDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private Color backField = new Color(20,25,34);

	public AProposDialog() {
		
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
			JTextArea aProposText = new JTextArea();
			aProposText.setEditable(false);
			aProposText.setForeground(Color.WHITE);
			aProposText.setBackground(backField);
			aProposText.setText("Version de l'IHM 1.0 \n"
					+ "Chef de projet : Thibault SAROBERT\n"
					+ "Architecte : Olivier ROMAN\n"
					+ "Testeur/Valideur : Maxime MORREAU\n"
					+ "Developeur : Pablo ORTEGA");
			contentPanel.add(aProposText);
		}
		
		setVisible(true);
	}

}
