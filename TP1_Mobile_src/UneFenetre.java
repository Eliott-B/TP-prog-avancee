import java.awt.*;
import javax.swing.*;

class UneFenetre extends JFrame 
{
    UnMobile sonMobile1;
    UnMobile sonMobile2;
    UnMobile sonMobile3;
    private final int LARG=400, HAUT=400;
    private final int LARG_MOBILE=400, HAUT_MOBILE=100;
    private final int NBRLIG=3, NBRCOL=1;
    
    public UneFenetre()
    {
        super("le Mobile");
        Container leConteneur = getContentPane();
        leConteneur.setLayout (new GridLayout(NBRLIG, NBRCOL));

        // JButton bouton = new JButton("Stop/Resume");
        // bouton.addActionListener(this);
        // leConteneur.add(bouton);

        sonMobile1 = new UnMobile(LARG_MOBILE, HAUT_MOBILE);
        leConteneur.add(sonMobile1);

        sonMobile2 = new UnMobile(LARG_MOBILE, HAUT_MOBILE);
        leConteneur.add(sonMobile2);

        sonMobile3 = new UnMobile(LARG_MOBILE, HAUT_MOBILE);
        leConteneur.add(sonMobile3);

        setSize(LARG, HAUT);
        setVisible(true);

        Thread laTache1 = new Thread(sonMobile1);
        Thread laTache2 = new Thread(sonMobile2);
        Thread laTache3 = new Thread(sonMobile3);
        laTache1.start();
        laTache2.start();
        laTache3.start();
    } 
}
