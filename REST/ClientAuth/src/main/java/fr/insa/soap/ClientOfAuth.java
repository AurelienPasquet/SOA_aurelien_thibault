
package fr.insa.soap;

import java.net.MalformedURLException;

import java.net.URI;
import java.net.URL;

import fr.insa.soap.wsdltojava.Authentication;
import fr.insa.soap.wsdltojava.AuthWS;

public class ClientOfAuth{
    
	public static void main(String[] args) throws MalformedURLException {
   	 
    	// L'adresse du service Web
    	final String adresse = "http://localhost:8080/RestProject/webapi/auth";
   	 
    	// Création de l'URL
    	final URL url = URI.create(adresse).toURL();
   	 
    	// Instanciation de l'image du service
    	final Authentication service = new Authentication(url);
   	 
    	// Création du proxy (en utilisant le portType) pour l'appel du service
    	final AuthWS port = service.getPort(AuthWS.class);
   	 
    	String login = "Bg";
    	String passwd = "Aure";
    	// Appel de la méthode compare via le port
    	System.out.println("Le résultat du process d'authentification est "+port.auth(login, passwd));
   	 
	}
}
