package ba.bitcamp.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ba.bitcamp.logger.Logger;

/**
 * ConnectionListener class
 * This class creates server socket.
 * @author gordansajevic
 *
 */

public class ConnectionListener {

	private static int port = 8080;

	public static void main(String[] args) {

		ExecutorService pool = Executors.newFixedThreadPool(10);
		HashMap<String, String> logs = new HashMap<String, String>();
		logs.put("applicationLog", "applicationLog");
		logs.put("warning", "warning");
		logs.put("error", "error");
		try {
			
			//Initialization of logger
			
			new Logger(logs);
		} catch (FileNotFoundException e1) {
			System.err.println("Could not initialize logger");
			System.exit(1);
		}

		try {
			
			//Creating server socket
			
			ServerSocket server = new ServerSocket(port);
			while (true) {
				
				//Connection between server and client
				
				Socket client = server.accept();
				Logger.log("applicationLog", client.getInetAddress()
						.getHostAddress() + " just connected");

				pool.submit(new Connection(client));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}