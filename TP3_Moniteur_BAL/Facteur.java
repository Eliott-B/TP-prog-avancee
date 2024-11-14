package TP3_Moniteur_BAL;

public class Facteur extends Thread
{
    private final String[] lettresADeposer = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "*"};
    private BoiteAuxLettres bal;

    public Facteur(BoiteAuxLettres bal)
    {
        this.bal = bal;
    }

    public void run()
    {
        try
        {
            for (String lettreADeposer : lettresADeposer)
            {
                Thread.sleep(1000); // 1 seconde
                bal.deposer(lettreADeposer);
                System.out.println("Lettre déposé : " + lettreADeposer);
            }
            System.out.println("Fin du dépôt de lettres");
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            System.out.println("[" + Thread.currentThread().getName() +  "] je m'arrête");
        }
    }

}
