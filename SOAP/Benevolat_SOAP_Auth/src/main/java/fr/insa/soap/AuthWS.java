package fr.insa.soap;

import java.util.Arrays;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName="authentication")
public class AuthWS {
	@WebMethod(operationName="auth")
	public boolean authentication(@WebParam(name="login") String login , @WebParam(name="passwd") String passwd) {
		
		List<credentials> listUsers=Arrays.asList(
				new credentials("Thibault","0000",0),
				new credentials("AUre","1111",1));
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
