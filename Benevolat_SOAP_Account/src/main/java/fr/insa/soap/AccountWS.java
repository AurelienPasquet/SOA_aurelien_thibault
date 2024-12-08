package fr.insa.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName="account")
public class AccountWS {
	
	@WebMethod(operationName="create")
	public User account(
			@WebParam(name="id") int id,
			@WebParam(name="firstname") String firstname,
			@WebParam(name="lastname") String lastname,
			@WebParam(name="role") String role) {
		return new User(id, firstname, lastname, role);
	}
}
