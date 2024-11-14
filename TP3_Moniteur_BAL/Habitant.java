package TP3_Moniteur_BAL;

public class Habitant extends Thread
{
    private BoiteAuxLettres bal;
    private String lettreRetire = null;

    public Habitant(BoiteAuxLettres bal)
    {
        this.bal = bal;
    }

    public void run()
    {
        try
        {
            while (true)
            {
                Thread.sleep(1500); // 1.5 secondes
                lettreRetire = bal.retirer();
                if (lettreRetire.equals("*"))
                {
                    System.out.println("Fin de la récupération de lettres");
                    Thread.currentThread().interrupt();
                }
                else
                {
                    System.out.println("Lettre récupéré : " + lettreRetire);
                }
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("[" + Thread.currentThread().getName() +  "] je m'arrête");
        }
    }

}
