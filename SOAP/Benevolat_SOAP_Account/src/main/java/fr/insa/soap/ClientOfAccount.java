package fr.insa.soap;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import fr.insa.soap.wsdltojava.Account;
import fr.insa.soap.wsdltojava.AccountWS;

public class ClientOfAccount {
	
	public static void main(String[] args) throws MalformedURLException {
		
		// L'adresse du service Web
		final String adresse = "http://localhost:8089/account";
		
		// Création de l'URL
		final URL url = URI.create(adresse).toURL();
		
		// Instanciation de l'image du service
		final Account service = new Account(url);
		
		// Création du proxy (en utilisant le portType) pour l'appel du service
		final AccountWS port = service.getPort(AccountWS.class);
		
		int tibo_id = 1;
		String tibo_firstname = "Tibo";
		String tibo_lastname = "Oui";
		String tibo_role = "bg";
		
		
		
		// Appel de la méthode compare via le port
		System.out.println("Détails de l'utilisateur Tibo: " + port.create(tibo_id, tibo_firstname, tibo_lastname, tibo_role).getRole());
	}
}
