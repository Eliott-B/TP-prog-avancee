package TP3_Moniteur_BAL;

public class Main
{
    public static void main(String[] args)
	{
		BoiteAuxLettres bal = new BoiteAuxLettres(5);
		Facteur facteur = new Facteur(bal);
		Habitant habitant = new Habitant(bal);

		facteur.start();
		habitant.start();
    }
}