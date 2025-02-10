package TP4_Shared.Pi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Creates workers to run the Monte Carlo simulation
 * and aggregates the results.
 */
public class Master {
    public long doRun(int totalCount, int numWorkers) throws InterruptedException, ExecutionException, IOException {
        return doRun(totalCount, numWorkers, false, "");
    }

    public long doRun(int totalCount, int numWorkers, boolean writeOutFile, String fileSubName)
            throws InterruptedException, ExecutionException, IOException {

        long startTime = System.currentTimeMillis();

        // Create a collection of tasks
        List<Callable<Long>> tasks = new ArrayList<Callable<Long>>();
        for (int i = 0; i < numWorkers; ++i) {
            tasks.add(new Worker(totalCount));
        }

        // Run them and receive a collection of Futures
        ExecutorService exec = Executors.newFixedThreadPool(numWorkers);
        List<Future<Long>> results = exec.invokeAll(tasks);
        long total = 0;

        // Assemble the results.
        for (Future<Long> f : results) {
            // Call to get() is an implicit barrier. This will block
            // until result from corresponding worker is ready.
            total += f.get();
        }
        double pi = 4.0 * total / totalCount / numWorkers;

        long stopTime = System.currentTimeMillis();

        System.out.println("Approx value: " + pi);
        double err = Math.abs((pi - Math.PI)) / Math.PI;
        System.out.println("Error: " + err);
        System.out.println("Total: " + totalCount * numWorkers);
        System.out.println("Available processors: " + numWorkers);
        long time = stopTime - startTime;
        System.out.println("Time Duration (ms): " + time + "\n");

        if (writeOutFile) {
            File file = new File("data\\out_Pi_G26_4c_" + fileSubName + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter stream = new FileWriter(file, true);
            stream.write(err + " " + totalCount * numWorkers + " " + numWorkers + " " + time + "\n");
            stream.close();
        }

        exec.shutdown();
        return total;
    }
}
