package TP4_Shared.Pi;

/**
 * Approximates PI using the Monte Carlo method. Demonstrates
 * use of Callables, Futures, and thread pools.
 */
public class Pi {
	public static void main(String[] args) throws Exception {
		int nTotByWorkers = Integer.parseInt(args[0]);
		int nWorkers = Integer.parseInt(args[1]);
		long total = 0;
		total = new Master().doRun(nTotByWorkers, nWorkers, true);
	}
}
