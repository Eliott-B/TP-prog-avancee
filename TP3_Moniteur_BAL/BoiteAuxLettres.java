package TP3_Moniteur_BAL;

public class BoiteAuxLettres
{
    private String[] lettreTampon;
    private int tete = 0;
    private int queue = 0;
    private int capacite;
    private int charge = 0;

    public BoiteAuxLettres(int nbLettres)
    {
        lettreTampon = new String[nbLettres];
        capacite = nbLettres;
    }

    public synchronized void deposer(String contenu) throws InterruptedException
    {
        while (charge == capacite)
        {
            wait(); // On attend que la bal soit déchargée
        }
        lettreTampon[queue] = contenu;
        queue = (queue + 1) % capacite;
        charge++;
        notifyAll(); // On notifie qu'une lettre a été déposée
        // print(lettreTampon);
    }

    public synchronized String retirer() throws InterruptedException
    {
        while (charge == 0)
        {
            wait(); // On attend qu'une lettre soit déposée
        }
        String lettre = lettreTampon[tete];
        tete = (tete + 1) % capacite;
        charge--;
        notifyAll(); // On notifie qu'une lettre a été retirée
        // print(lettreTampon);
        return lettre;
    }

    // public static void print(String[] array)
    // {
    //     for (String s : array)
    //     {
    //         System.out.print(s + " ");
    //     }
    //     System.out.println();
    // }
}
