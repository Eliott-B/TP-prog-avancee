import java.awt.*;
import javax.swing.*;

class UneFenetre extends JFrame 
{
    UnMobile sonMobile;
    private final int LARG=400, HAUT=250;
    
    public UneFenetre()
    {
        super("le Mobile");
        Container leConteneur = getContentPane();
        sonMobile = new UnMobile(LARG, HAUT);
        leConteneur.add(sonMobile);
        setSize(LARG, HAUT);
        setVisible(true);
        Thread laTache= new Thread(sonMobile);
        laTache.start();
    }
}
