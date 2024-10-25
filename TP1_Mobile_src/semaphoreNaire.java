public final class semaphoreNaire extends semaphore
{
	public semaphoreNaire(int valeurInitiale)
	{
		super(valeurInitiale);
		//System.out.print(valeurInitiale);
	}

	public final synchronized void syncSignal()
	{
		System.out.println("\nje sors de section critique");
		super.syncSignal();
		//System.out.print(valeur);
		// if (valeur>1) valeur = 1;
	}

	public final synchronized void syncWait()
	{
		super.syncWait();
		System.out.println("j’entre en section critique");
	}
}

