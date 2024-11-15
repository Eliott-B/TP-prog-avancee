package TP3_Moniteur_BAL;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BoiteAuxLettres
{
    private BlockingQueue<String> lettreTampon;

    public BoiteAuxLettres(int nbLettres)
    {
        lettreTampon = new ArrayBlockingQueue<String>(nbLettres);
    }

    public boolean deposer(String contenu) throws InterruptedException
    {
        return lettreTampon.offer(contenu, 1, java.util.concurrent.TimeUnit.SECONDS);
    }

    public String retirer() throws InterruptedException
    {
        return lettreTampon.poll(1, java.util.concurrent.TimeUnit.SECONDS);
    }

    public int getNbLettres()
    {
        return lettreTampon.size();
    }
}
