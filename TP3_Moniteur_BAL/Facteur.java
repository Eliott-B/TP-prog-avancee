package TP3_Moniteur_BAL;

public class Facteur extends Thread
{
    private String lettreADeposer = null;
    private BoiteAuxLettres bal = new BoiteAuxLettres();

    public Facteur(BoiteAuxLettres bal, String lettre)
    {
        this.bal = bal;
        lettreADeposer = lettre;
    }

    public void run()
    {
        try
        {
            bal.deposer(lettreADeposer);
            System.out.println("Depot de la lettre : " + lettreADeposer);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
