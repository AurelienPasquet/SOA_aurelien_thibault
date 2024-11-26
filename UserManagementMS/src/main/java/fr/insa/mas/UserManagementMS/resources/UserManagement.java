package fr.insa.mas.UserManagementMS.resources;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.mas.UserManagementMS.model.User;

@RestController
@RequestMapping("/user")
public class UserManagement {

	@GetMapping("/create")
	public User createUser(
			@RequestParam int id, 
			@RequestParam String firstname,
			@RequestParam String lastname,
			@RequestParam String login,
			@RequestParam String pwd,
			@RequestParam String role
			) {
		
		User user = new User(id, firstname, lastname, login, pwd, role);
		
		return user;
	}
	
}
