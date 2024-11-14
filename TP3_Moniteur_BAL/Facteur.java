package TP3_Moniteur_BAL;

public class Facteur extends Thread
{
    private String lettreADeposer = null;
    private BoiteAuxLettres bal = new BoiteAuxLettres();

    public Facteur(BoiteAuxLettres bal)
    {
        this.bal = bal;
    }

    public void run()
    {
        try
        {
            while (lettreADeposer == null || !lettreADeposer.equals("Q"))
            {
                while (!bal.estDisponible())
                {
                    sleep(1000);
                }
                System.out.println("Entrez la lettre à déposer : ");
                lettreADeposer = System.console().readLine();
                bal.deposer(lettreADeposer);
                System.out.println("Depot de la lettre : " + lettreADeposer);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
