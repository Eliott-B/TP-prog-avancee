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
            if (lettreRetire == null)
            {
                Main.setLettreLabel("Boite aux lettres vide");
            }
            else
            {
                Main.setLettreLabel("Lettre récupéré : " + lettreRetire);
            }
        }
        catch (Exception e)
        {
            Main.setLettreLabel(e.getMessage());
        }
    }

}
