package ba.bitcamp.server;

import java.io.PrintStream;

/**
 * This class has three responses: ok(HTTP 200), error(HTTP 404) and serverError(HTTP 500).
 * @author gordansajevic
 *
 */

public class Response {
	
	/**
	 * This is private method that writes same output for three other methods.
	 * @param write
	 * @param content
	 */
	
	private static void sendContent(PrintStream write, String content){
		write.println("Content-Type: text/html");
		write.println();
		write.println(content);
	}
	
	/**
	 * Method that writes output of there is no error.
	 * @param write
	 * @param content
	 */
	
	public static void ok(PrintStream write, String content){
		write.println("HTTP/1.1 200 OK");
		sendContent(write, content);
	}
	
	/**
	 * Method that writes error message for case 404(not found)
	 * @param write
	 * @param content
	 */
	
	public static void error(PrintStream write, String content){
		write.println("HTTP/1.1 404 Not Found");
		sendContent(write, content);
	}
	
	/**
	 * Method that writes error message for case 500(server error)
	 * @param write
	 * @param content
	 */
	
	public static void serverError(PrintStream write, String content){
		write.println("HTTP/1.1 500 Internal Server Error");
		sendContent(write, content);
	}

}