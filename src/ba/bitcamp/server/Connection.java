package ba.bitcamp.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import ba.bitcamp.logger.Logger;

/**
 * This class creates client by accepting client socket in constructor.
 * Class implements Runnable interface.
 * @author gordansajevic
 *
 */

public class Connection implements Runnable {

	private Socket client;
	
	//We have three allowed extensions: htm, js and css
	
	private String[] extensions = {".html", ".js", ".css"};

	/**
	 * Constructor with socket as parameter
	 * @param client
	 */
	
	public Connection(Socket client) {
		
		//Initialization of client socket
		
		this.client = client;
	}

	/**
	 * This method overrides run method from Runnable interface.
	 * In this method we accept GET request and check if file exist.
	 * Then we write file to output stream.
	 */
	
	@Override
	public void run() {

		BufferedReader read = null;
		PrintStream write = null;
		try {
			read = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			write = new PrintStream(client.getOutputStream());
		} catch (IOException e) {
			Logger.log("error", e.getMessage());
			closeClient();
			return;
		}

		String line = null;
		try {
			while ((line = read.readLine()) != null) {
				if (line.contains("GET") || line.isEmpty()) {
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Dobili: " + line);
		if (!line.contains("GET")) {
			Logger.log("warning", "Was not GET request");
			Response.error(write, "Invalid request");
			closeClient();
			return;
		}
		String fileName = getFileName(line);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			Response.error(write, "This is not the page you are looking for");
			Logger.log("warning",
					"Client requested missing file " + e.getMessage());
			closeClient();
			return;
		}
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(
				fis));
		String fileLine = "";
		StringBuilder sb = new StringBuilder();
		try {
			while ((fileLine = fileReader.readLine()) != null) {
				sb.append(fileLine);
			}
		} catch (IOException e) {
			Logger.log("error", e.getMessage());
			Response.serverError(write,
					"A well trained group of monkeys is trying to fix the problem");
			closeClient();
			return;
		}
		Response.ok(write, sb.toString());
		closeClient();

	}

	/**
	 * Method closes client socket, and throws an exception if necessary
	 */
	
	private void closeClient() {
		try {
			client.close();
		} catch (IOException e) {
			Logger.log("warning", e.getMessage());
		}
	}

	/**
	 * Method checks does file have valid extension(html, js or css) and creates base path.
	 * If there is no extension at all, method adds default extension html.
	 * If file name is empty, method adds default name index.html. 
	 * @param request
	 * @return basePath + fileName
	 */
	
	private String getFileName(String request) {
		String[] parts = request.split(" ");
		String fileName = null;
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].equals("GET")) {
				fileName = parts[i + 1];
				break;
			}
		}
		String basePath = "." + File.separator + "html" + File.separator;
		if (fileName == null || fileName.equals("/"))
			return basePath + "index.html";

		if (!fileName.contains(".")) {
			fileName += extensions[0];
		}
		if (fileName.contains(extensions[1])) {
			basePath += "assets" + File.separator + "js" + File.separator;
		}
		if (fileName.contains(extensions[2])) {
			basePath += "assets" + File.separator + "css" + File.separator;
		}
		else
		{
			Logger.log("error", "Wrong extension!");
			try {
				Response.error(new PrintStream(client.getOutputStream()), "Wrong extension!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return basePath + fileName;
	}

}