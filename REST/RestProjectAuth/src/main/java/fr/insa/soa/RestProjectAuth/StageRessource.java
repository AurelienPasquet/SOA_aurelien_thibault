package fr.insa.soa.RestProjectAuth;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("stage")
public class StageRessource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Stage getStage(/*int idEtudiant*/) {
		Stage stage = new Stage();
		stage.setEvaluation(16);
		stage.setCompetences("SOA,Rest");
		stage.setAnnee(2024);
		return stage;
	}
	

}
