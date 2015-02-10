package ba.bitcamp.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import ba.bitcamp.logger.Logger;

public class Connection implements Runnable{

	private Socket client;
	
	public Connection(Socket client)
	{
		this.client = client;
	}
	
	@Override
	public void run() 
	{
		try {
			InputStream in = client.getInputStream();
			OutputStream out = client.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			PrintStream writer = new PrintStream(out);
			while((line = br.readLine()) != null)
			{
				if(line.contains("GET"))
				{
					break;
				}
			}
			System.out.println("Dobili: " + line);
			if (!line.contains("GET"))
			{
				Logger.log("warning", "Not GET request.");
				Response.error(writer, "Invalid request");
				client.close();
				return;
			}
			String fileName = getFileName(line);
			String html = "";
			FileInputStream fis = new FileInputStream("/Users/gordansajevic/Documents/workspace/BitServer/html/index" + fileName);
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(fis));
			String fileLine = "";
			while((fileLine=fileReader.readLine()) != null)
			{
				html += fileLine;
			}
			Response.ok(writer, html);
			System.out.println();
			client.close();
			
		} catch (IOException e) {
			Logger.log("error", e.getMessage());
			try
			{
				client.close();
			} catch (IOException e1)
			{
				Logger.log("warning", e1.getMessage());
			}
			e.printStackTrace();
		}
		
	}
	
	private String getFileName(String request)
	{
		String[] parts = request.split(" ");
		String fileName = null;
		for(int i=0; i<parts.length; i++)
		{
			if(parts[i].equals("GET"))
			{
				fileName = parts[i+1];
				break;
			}
		}
		if (fileName == null)
		{
			return "index.html";
		}
		if(!fileName.contains("."))
		{
			fileName += ".html";
		}
		return fileName;
	}

}
