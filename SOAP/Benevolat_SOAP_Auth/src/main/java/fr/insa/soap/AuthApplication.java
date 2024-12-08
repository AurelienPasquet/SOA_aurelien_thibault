package fr.insa.soap;

import java.net.MalformedURLException;
import javax.xml.ws.Endpoint;

public class AuthApplication {
	public static String host="localhost";
	public static short port = 8081;
	
	public void demarrerService() {
		String url="http://"+host+":"+port+"/";
		Endpoint.publish(url, new AuthWS());
		
	}
	
	public static void main(String [] args) throws MalformedURLException {
		new AuthApplication().demarrerService();
		System.out.println("Service a démarré");
	}

}


