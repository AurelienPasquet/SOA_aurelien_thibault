package fr.insa.soap;

import java.net.MalformedURLException;

import javax.xml.ws.Endpoint;

public class AccountApplication {
	
	public static String host = "localhost";
	public static short port = 8089;
	
	public void startService() {
		String url = "http://"+host+":"+port+"/";
		Endpoint.publish(url, new AccountWS());
	}
	
	public static void main(String[] args) throws MalformedURLException {
		
		new AccountApplication().startService();
		System.out.println("Service a démarré");
	}
}
