package TP3_Moniteur_BAL;

public class Habitant extends Thread
{
    private BoiteAuxLettres bal = new BoiteAuxLettres();
    private String lettreRetire = null;

    public Habitant(BoiteAuxLettres bal)
    {
        this.bal = bal;
    }

    public void run()
    {
        try
        {
            lettreRetire = bal.retirer();
            System.out.println("Lettre récupéré : " + lettreRetire);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
