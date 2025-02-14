import java.io.*;
import java.net.*;

/**
 * Master is a client. It makes requests to numWorkers.
 * 
 */
public class MasterSocket {
	static int maxServer = 12;
	static final int[] tab_port = { 25545, 25546, 25547, 25548, 25549, 25550, 25551, 25552, 25553, 25554, 25555,
			25556 };
	static String[] tab_total_workers = new String[maxServer];
	static final String ip = "127.0.0.1";
	static BufferedReader[] reader = new BufferedReader[maxServer];
	static PrintWriter[] writer = new PrintWriter[maxServer];
	static Socket[] sockets = new Socket[maxServer];

	static String subFileName = "";

	public static void main(String[] args) throws Exception {

		// MC parameters
		int totalCount = 16000000; // total number of throws on a Worker
		int total = 0; // total number of throws inside quarter of disk
		double pi;

		int numWorkers = maxServer;
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("#########################################");
		System.out.println("# Computation of PI by MC method        #");
		System.out.println("#########################################");

		totalCount = Integer.parseInt(args[0]);
		if (args.length > 1)
			numWorkers = Integer.parseInt(args[1]);
		if (args.length > 2)
			subFileName = args[2];

		// for (int i = 0; i < numWorkers; i++) {
		// System.out.println("Enter worker" + i + " port : ");
		// try {
		// s = bufferRead.readLine();
		// System.out.println("You select " + s);
		// } catch (IOException ioE) {
		// ioE.printStackTrace();
		// }
		// }

		// create worker's socket
		for (int i = 0; i < numWorkers; i++) {
			sockets[i] = new Socket(ip, tab_port[i]);
			System.out.println("SOCKET = " + sockets[i]);

			reader[i] = new BufferedReader(new InputStreamReader(sockets[i].getInputStream()));
			writer[i] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockets[i].getOutputStream())), true);
		}

		String message_to_send;
		message_to_send = String.valueOf(totalCount);

		String message_repeat = "y";

		long stopTime, startTime;

		while (message_repeat.equals("y")) {

			startTime = System.currentTimeMillis();
			// initialize workers
			for (int i = 0; i < numWorkers; i++) {
				writer[i].println(message_to_send); // send a message to each worker
			}

			// listen to workers's message
			for (int i = 0; i < numWorkers; i++) {
				tab_total_workers[i] = reader[i].readLine(); // read message from server
				System.out.println("Client sent: " + tab_total_workers[i]);
			}

			// compute PI with the result of each workers
			for (int i = 0; i < numWorkers; i++) {
				total += Integer.parseInt(tab_total_workers[i]);
			}
			pi = 4.0 * total / totalCount;

			stopTime = System.currentTimeMillis();

			System.out.println("\nPi : " + pi);
			double err = Math.abs((pi - Math.PI)) / Math.PI;
			System.out.println("Error: " + err + "\n");

			System.out.println("Ntot: " + totalCount * numWorkers);
			System.out.println("Available processors: " + numWorkers);
			long time = stopTime - startTime;
			System.out.println("Time Duration (ms): " + time + "\n");

			System.out.println(err + " " + totalCount * numWorkers + " " + numWorkers + " " + time);

			File file = new File("TP4_Suite\\data\\out_MasterSocket_G26_4c_" + subFileName + ".txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter stream = new FileWriter(file, true);
			stream.write(err + " " + totalCount * numWorkers + " " + numWorkers + " " + time + "\n");
			stream.close();

			// System.out.println("\n Repeat computation (y/N): ");
			// try {
			message_repeat = "n";
			System.out.println(message_repeat);
			// } catch (IOException ioE) {
			// ioE.printStackTrace();
			// }

			total = 0; // reset total
		}

		for (int i = 0; i < numWorkers; i++) {
			System.out.println("END"); // Send ending message
			writer[i].println("END");
			reader[i].close();
			writer[i].close();
			sockets[i].close();
		}
	}
}