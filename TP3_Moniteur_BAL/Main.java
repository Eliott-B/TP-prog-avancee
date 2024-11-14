package TP3_Moniteur_BAL;

public class Main
{
	public static void main(String[] args)
	{
		BoiteAuxLettres bal = new BoiteAuxLettres();
		Facteur facteur = new Facteur(bal, "Lettre 1");
		Facteur facteur2 = new Facteur(bal, "Lettre 2");
		Habitant habitant = new Habitant(bal);
		Habitant habitant2 = new Habitant(bal);

		facteur.start();
		facteur2.start(); // error bal is not available
		habitant.start();
		habitant2.start(); // get null (no letter available)
	}
}
