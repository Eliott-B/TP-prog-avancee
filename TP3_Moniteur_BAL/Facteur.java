package TP3_Moniteur_BAL;

public class Facteur extends Thread
{
    private String lettreADeposer = null;
    private BoiteAuxLettres bal = new BoiteAuxLettres();

    public Facteur(BoiteAuxLettres bal, String lettre)
    {
        this.bal = bal;
        this.lettreADeposer = lettre;
    }

    public void run()
    {
        try
        {
            bal.deposer(lettreADeposer);
            Main.setLettreLabel("Lettre déposé : " + lettreADeposer);
        } catch (Exception e) {
            Main.setLettreLabel(e.getMessage());
        }
    }

}
