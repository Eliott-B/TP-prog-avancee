import java.awt.*;
import java.util.Random;

import javax.swing.*;

class UnMobile extends JPanel implements Runnable
{
    int saLargeur, saHauteur, sonDebDessin;
	int sonTemps;
    final int sonPas=10, sonCote=40;
	static semaphoreNaire sem = new semaphoreNaire(2);
    
    UnMobile(int telleLargeur, int telleHauteur)
    {
		super();
		saLargeur = telleLargeur;
		saHauteur = telleHauteur;
		setSize(telleLargeur, telleHauteur);
		
		Random random = new Random();
		sonTemps = random.nextInt(60 + 10) + 10;
    }

    public void run()
    {
		int tier = saLargeur/3;

		while (true)
		{
			for (sonDebDessin=0; sonDebDessin < tier - sonPas; sonDebDessin+= sonPas)
			{
				repaint();
				try
				{
					Thread.sleep(sonTemps);
				}
				catch (InterruptedException telleExcp)
				{
					telleExcp.printStackTrace();
				}
			}

			// sem.syncWait();
			synchronized(JPanel.class){
				for (sonDebDessin=tier-sonPas; sonDebDessin < tier*2 - sonPas; sonDebDessin+= sonPas)
				{
					repaint();
					try
					{
						Thread.sleep(sonTemps);
					}
					catch (InterruptedException telleExcp)
					{
						telleExcp.printStackTrace();
					}
				}
			}
			// sem.syncSignal();

			for (sonDebDessin=tier*2-sonPas; sonDebDessin < saLargeur - sonPas; sonDebDessin+= sonPas)
			{
				repaint();
				try
				{
					Thread.sleep(sonTemps);
				}
				catch (InterruptedException telleExcp)
				{
					telleExcp.printStackTrace();
				}
			}

			// RETOUR

			for (sonDebDessin=saLargeur - sonPas; sonDebDessin > tier*2-sonPas; sonDebDessin -= sonPas)
			{
				repaint();
				try
				{
					Thread.sleep(sonTemps);
				}
				catch (InterruptedException telleExcp)
				{
					telleExcp.printStackTrace();
				}
			}
			
			// sem.syncWait();
			synchronized(JPanel.class)
			{
				for (sonDebDessin=tier*2-sonPas; sonDebDessin > tier-sonPas; sonDebDessin -= sonPas)
				{
					repaint();
					try
					{
						Thread.sleep(sonTemps);
					}
					catch (InterruptedException telleExcp)
					{
						telleExcp.printStackTrace();
					}
				}
			}
			// sem.syncSignal();

			for (sonDebDessin=tier-sonPas; sonDebDessin > 0; sonDebDessin -= sonPas)
			{
				repaint();
				try
				{
					Thread.sleep(sonTemps);
				}
				catch (InterruptedException telleExcp)
				{
					telleExcp.printStackTrace();
				}
			}
		}
    }

    public void paintComponent(Graphics telCG)
    {
		super.paintComponent(telCG);
		telCG.fillRect(sonDebDessin, saHauteur/2, sonCote, sonCote);
    }
}