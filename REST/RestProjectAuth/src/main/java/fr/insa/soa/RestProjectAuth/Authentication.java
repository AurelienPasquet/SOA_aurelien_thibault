package fr.insa.soa.RestProjectAuth;

import java.util.Arrays;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("auth")
public class Authentication {
	@GET
	@Path("/{login}/{passwd}")
	@Produces(MediaType.TEXT_PLAIN)
	public boolean authentication(@PathParam("login") String login , @PathParam("passwd") String passwd) {
		
		List<credentials> listUsers=Arrays.asList(
				new credentials("Thibault","0000",0),
				new credentials("Aure","1111",1));
		boolean inList = false;
		
		for (int i=0;i<listUsers.size();i++) {
			credentials user = listUsers.get(i);
			if (user.getLogin().equals(login) && user.getPasswd().equals(passwd)) {
				inList = true;
			}
		}
		return inList;
	}
	
}

