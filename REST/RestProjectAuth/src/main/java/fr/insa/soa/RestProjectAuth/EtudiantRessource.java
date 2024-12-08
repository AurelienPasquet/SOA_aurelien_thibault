package fr.insa.soa.RestProjectAuth;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

@Path("etudiant")
public class EtudiantRessource {
	 @GET
	 @Produces(MediaType.APPLICATION_JSON)
	 public Etudiant getEtudiant(@Context UriInfo uriInfo) {
		 Etudiant etudiant=new Etudiant();
		 etudiant.setNom("G");
		 etudiant.setPrenom("T");
		 etudiant.setId(1);
		 etudiant.addLink(getUriforSelf(uriInfo,etudiant), "self", "GET");
		 etudiant.addLink(getUriforStage(uriInfo), "stage", "GET");
		 return etudiant;
	 }
	 
	 private String getUriforStage(UriInfo uriInfo) {
		 String url=uriInfo.getBaseUriBuilder()
				 .path(StageRessource.class)
				 .build()
				 .toString();
		 return url;
	 }
	 
	 private String getUriforSelf(UriInfo uriInfo,Etudiant etudiant) {
		 String url=uriInfo.getBaseUriBuilder()
				 .path(EtudiantRessource.class)
				 .path(Long.toString(etudiant.getId()))
				 .build()
				 .toString();
		 return url;
	 }
	 
	 
	 
	 @GET
	 @Path("/{idEtudiant}")
	 @Produces(MediaType.APPLICATION_JSON)
	 public Etudiant getEtudiant(@PathParam("idEtudiant") int id) {
		 Etudiant etudiant=new Etudiant();
		 etudiant.setNom("G");
		 etudiant.setPrenom("T");
		 return etudiant;
	 }
	 
	 @POST
	 @Path("/{idEtudiant}")
	 @Produces(MediaType.APPLICATION_JSON)
	 public void addEtudiant(Etudiant etudNouv) {
		 etudNouv = new Etudiant();

			etudNouv.setNom("A");

			etudNouv.setPrenom("B");

			

			Binome binome = new Binome();

			binome.setNom("C");

			binome.setPrenom("D");

			

			etudNouv.setBinome(binome);
		 System.out.println("Ajout de l'étudiant "+etudNouv.getNom()+" "+etudNouv.getPrenom()+" réussie!");
		 System.out.println("Son binome est "+etudNouv.getBinome().getPrenom()+" "+etudNouv.getBinome().getNom());
	 }
	 
	 

}
