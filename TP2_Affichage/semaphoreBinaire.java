public final class semaphoreBinaire extends semaphore
{
	public semaphoreBinaire(int valeurInitiale)
	{
		super((valeurInitiale != 0) ? 1:0);
		//System.out.print(valeurInitiale);
	}

	public final synchronized void syncSignal()
	{
		System.out.println("\nje sors de section critique");
		super.syncSignal();
		//System.out.print(valeur);
		if (valeur>1) valeur = 1;
	}

	public final synchronized void syncWait()
	{
		super.syncWait();
		System.out.println("j’entre en section critique");
	}
}

