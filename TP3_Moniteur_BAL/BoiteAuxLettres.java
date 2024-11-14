package TP3_Moniteur_BAL;

public class BoiteAuxLettres
{
    private String lettre;
    private boolean disponible = true;

    public BoiteAuxLettres()
    {

    }

    public synchronized void deposer(String contenu) throws Exception
    {
        if (disponible)
        {
            lettre = contenu;
            disponible = false;
        }
        else
        {
            throw new Exception("Boite aux lettres non disponible");
        }
    }

    public synchronized String retirer()
    {
        String copieLettre = lettre;
        lettre = null;
        disponible = true;
        return copieLettre;
    }

    public boolean estDisponible()
    {
        return disponible;
    }
}
