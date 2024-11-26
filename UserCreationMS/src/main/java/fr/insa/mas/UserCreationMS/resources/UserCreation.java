package fr.insa.mas.UserCreationMS.resources;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.mas.UserCreationMS.model.User;

@RestController
@RequestMapping("/createUser")
public class UserCreation {

	@PostMapping("/{id}")
	public User createUser(@PathVariable("id") int id) {
		
		User user = new User(id, "Tibo", "Oui", "loguine", "12345", "BG");
		
		return user;
	}
	
}
