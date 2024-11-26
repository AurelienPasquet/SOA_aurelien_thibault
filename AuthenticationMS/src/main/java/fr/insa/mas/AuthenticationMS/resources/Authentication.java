package fr.insa.mas.AuthenticationMS.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.mas.AuthenticationMS.model.credentials;

@RestController
@RequestMapping("/auth")
public class Authentication {

	@GetMapping("/{idStudent}")
	public boolean authentication(@PathVariable("login") String login , @PathVariable("passwd") String passwd) {
		
		List<credentials> listUsers=Arrays.asList(
				new credentials("Toto","Titi",0),
				new credentials("Bg","Aure",1));
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
