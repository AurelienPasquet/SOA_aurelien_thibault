package fr.insa.mas.MissionRetrieveMS.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.insa.mas.MissionRetrieveMS.model.mission;

@RestController
@RequestMapping("/missionRetrieve")
public class missionRetrieve {

	@GetMapping("/{id}")
	public mission retrieve(@PathVariable("id") int id) {
		
		List<mission> listMission=Arrays.asList(
				new mission(0, "Besoin d'aide !!!!","J'ai envie de nutella, faites mes courses svp!",0, 0, 0,""),
				new mission(1, "AAAAAAAAAAAAAAAAAh","Ca va en fait",0, 0, 0,""));

		
		return listMission.get(id);
	}
	
}
