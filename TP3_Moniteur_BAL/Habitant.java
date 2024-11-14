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
            while (lettreRetire == null || !lettreRetire.equals("Q"))
            {
                while (bal.estDisponible())
                {
                    sleep(1000);
                }
                lettreRetire = bal.retirer();
                System.out.println("Lettre récupéré : " + lettreRetire);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
