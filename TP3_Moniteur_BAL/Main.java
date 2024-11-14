package TP3_Moniteur_BAL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
	private BoiteAuxLettres bal = new BoiteAuxLettres();
    private JTextField message;
    private JButton envoieBouton;
	private JButton lireBouton;
	private static JLabel lettreLabel;

    public Main()
	{
        setTitle("Boite aux lettres");
        setSize(300, 150);

        message = new JTextField(20);
        envoieBouton = new JButton("Envoyer");
        lireBouton = new JButton("Lire");
		lettreLabel = new JLabel();

        envoieBouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = message.getText();
                Facteur facteur = new Facteur(bal, text);
				facteur.start();
            }
        });

		lireBouton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Habitant habitant = new Habitant(bal);
				habitant.start();
			}
		});

        setLayout(new FlowLayout());
        add(message);
        add(envoieBouton);
		add(lireBouton);
		add(lettreLabel);
    }

	public static synchronized void setLettreLabel(String message)
	{
		lettreLabel.setText(message);
	}

    public static void main(String[] args)
	{
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}