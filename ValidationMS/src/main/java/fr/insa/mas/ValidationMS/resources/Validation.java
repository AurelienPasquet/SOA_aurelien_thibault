package fr.insa.mas.ValidationMS.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.mas.ValidationMS.model.mission;

@RestController
@RequestMapping("/validate")
public class Validation {

	@GetMapping("/{id}")
	public mission validate(@PathVariable("id") int id) {
		
		List<mission> listMission=Arrays.asList(
				new mission(0, "Besoin d'aide !!!!","J'ai envie de nutella, faites mes courses svp!",0, 0, 0,"Envoyée"),
				new mission(1, "AAAAAAAAAAAAAAAAAh","Ca va en fait",0, 0, 0,"Terminée"));

		if (listMission.get(id).status == "Envoyée") {
			listMission.get(id).setStatus("Validée");
			}
		return listMission.get(id);
	}
	
}
