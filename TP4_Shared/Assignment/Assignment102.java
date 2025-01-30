package TP4_Shared.Assignment;

// Estimate the value of Pi using Monte-Carlo Method, using parallel program
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class PiMonteCarlo {
	AtomicInteger nAtomSuccess;
	int nThrows;
	double value;
	int nProcessors;

	class MonteCarlo implements Runnable {
		@Override
		public void run() {
			double x = Math.random();
			double y = Math.random();
			if (x * x + y * y <= 1)
				nAtomSuccess.incrementAndGet();
		}
	}

	public PiMonteCarlo(int i, int iProcessors) {
		this.nAtomSuccess = new AtomicInteger(0);
		this.nThrows = i;
		this.value = 0;
		this.nProcessors = iProcessors;
	}

	public double getPi() {
		// int nProcessors = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newWorkStealingPool(nProcessors);
		for (int i = 1; i <= nThrows; i++) {
			Runnable worker = new MonteCarlo();
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		value = 4.0 * nAtomSuccess.get() / nThrows;
		return value;
	}
}

public class Assignment102 {
	public static void main(String[] args) throws IOException {
		int nTot = Integer.parseInt(args[0]);
		int nbProcesses = Integer.parseInt(args[1]);
		PiMonteCarlo PiVal = new PiMonteCarlo(nTot, nbProcesses);
		long startTime = System.currentTimeMillis();
		double value = PiVal.getPi();
		long stopTime = System.currentTimeMillis();
		System.out.println("Approx value:" + value);
		double err = Math.abs((value - Math.PI)) / Math.PI;
		System.out.println("Error: " + err);
		System.out.println("Total: " + nTot);
		System.out.println("Available processors: " + nbProcesses);
		long time = (stopTime - startTime);
		System.out.println("Time Duration (ms): " + time);

		File file = new File("data\\out_Assignment102_G26_4c_" + nTot + ".txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter stream = new FileWriter(file, true);
		stream.write(err + " " + nTot + " " + nbProcesses + " " + time + "\n");
		stream.close();
	}
}