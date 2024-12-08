package fr.insa.mas.orchestratorms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import fr.insa.mas.orchestratorms.model.Comment;
import fr.insa.mas.orchestratorms.model.Credentials;
import fr.insa.mas.orchestratorms.model.Mission;
import fr.insa.mas.orchestratorms.model.SignInRequest;
import fr.insa.mas.orchestratorms.model.User;
import jakarta.ws.rs.core.MediaType;
import fr.insa.mas.orchestratorms.model.SignUpRequest;

@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController {

	private RestTemplate restTemplate = new RestTemplate();
	
	private String userServiceUrl = "http://localhost:8081/users";
	private String missionServiceUrl = "http://localhost:8082/missions";
	private String commentServiceUrl = "http://localhost:8083/comments";
    private String authServiceUrl = "http://localhost:8084/credentials";
	
    // ********** SIGNUP **********
	@PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
        // Validate input
        if (request.getFirstname() == null || request.getLastname() == null ||
            request.getRole() == null || request.getValidatorName() == null ||
            request.getLogin() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        if (!request.getRole().equals("Volunteer") &&
            !request.getRole().equals("Requester")) {
            return ResponseEntity.badRequest().body("Invalid role. Must be 'Volunteer' or 'Requester'.");
        }

        // Define URLs for services
        String checkLoginUrl = authServiceUrl + "?login=" + request.getLogin();

        try {
            // Check if login already exists
            ResponseEntity<List<Credentials>> checkLoginResponse = restTemplate.exchange(
            		checkLoginUrl,
            		HttpMethod.GET,
            		null,
            		new ParameterizedTypeReference<List<Credentials>>() {}
            	);
            
            if (checkLoginResponse.getBody() != null && !checkLoginResponse.getBody().isEmpty()) {
                return ResponseEntity.badRequest().body("Login already exists.");
            }

            // Create user
            User user = new User();
            user.setFirstname(request.getFirstname());
            user.setLastname(request.getLastname());
            user.setRole(request.getRole());
            user.setValidatorName(request.getValidatorName());

            ResponseEntity<User> userResponse = restTemplate.postForEntity(userServiceUrl, user, User.class);

            if (userResponse.getStatusCode().is2xxSuccessful() && userResponse.getBody() != null) {
                User createdUser = userResponse.getBody();

                // Create credentials
                Credentials credentials = new Credentials();
                credentials.setId(createdUser.getId());
                credentials.setLogin(request.getLogin());
                credentials.setPassword(request.getPassword());

                ResponseEntity<Credentials> credentialsResponse = restTemplate.postForEntity(authServiceUrl, credentials, Credentials.class);

                if (credentialsResponse.getStatusCode().is2xxSuccessful()) {
                    return ResponseEntity.ok("Account created successfully.");
                } else {
                    // Rollback user creation if credentials creation fails
                    restTemplate.delete(userServiceUrl + "/" + createdUser.getId());
                    return ResponseEntity.status(credentialsResponse.getStatusCode()).body("Failed to create credentials.");
                }
            } else {
                return ResponseEntity.status(userResponse.getStatusCode()).body("Failed to create user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
	
	// ********** SIGNIN **********
	@PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
	    if (request.getLogin() == null || request.getPassword() == null) {
	        return ResponseEntity.badRequest().body("Login et mot de passe requis.");
	    }

	    String authCheckUrl = authServiceUrl + "/check";

	    try {
	        Credentials credentials = new Credentials();
	        credentials.setLogin(request.getLogin());
	        credentials.setPassword(request.getPassword());

	        ResponseEntity<String> credentialsResponse = restTemplate.postForEntity(authCheckUrl, credentials, String.class);

	        int userId = Integer.parseInt(credentialsResponse.getBody());
	        if (userId > 0) {
	            ResponseEntity<User> userResponse = restTemplate.getForEntity(userServiceUrl + "/" + userId, User.class);

	            if (userResponse.getStatusCode().is2xxSuccessful() && userResponse.getBody() != null) {
	                return ResponseEntity.ok(userResponse.getBody());
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
	            }
	        }

	    } catch (HttpClientErrorException.Unauthorized e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mot de passe incorrect.");
	    } catch (HttpClientErrorException.NotFound e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur.");
	    }

	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur d'authentification.");
	}

	
	// ********** GET MISSIONS **********
	@GetMapping("/missions")
	public ResponseEntity<?> getMissions(
	        @RequestParam(value = "volunteer_id", required = false) Integer volunteerId,
	        @RequestParam(value = "asker_id", required = false) Integer askerId,
	        @RequestParam(value = "status", required = false) String status) {
		
	    StringBuilder urlBuilder = new StringBuilder(missionServiceUrl + "?");
	    if (volunteerId != null) {
	        urlBuilder.append("volunteer_id=").append(volunteerId).append("&");
	    }
	    if (askerId != null) {
	        urlBuilder.append("asker_id=").append(askerId).append("&");
	    }
	    if (status != null && !status.isEmpty()) {
	        urlBuilder.append("status=").append(status).append("&");
	    }

	    String url = urlBuilder.toString().replaceAll("&$", "");
	    
	    System.out.println(url);

	    ResponseEntity<Mission[]> response = restTemplate.getForEntity(url, Mission[].class);

	    if (response.getStatusCode().is2xxSuccessful()) {
	        return ResponseEntity.ok(response.getBody());
	    }
	    
	    return ResponseEntity.status(404).body("No missions found!");
	}

	// ********** CREATE MISSION **********
    @PostMapping("/missions")
    public ResponseEntity<?> createMission(@RequestBody Mission mission) {
        ResponseEntity<Mission> response = restTemplate.postForEntity(
                missionServiceUrl, mission, Mission.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(201).body(response.getBody());
        }
        return ResponseEntity.status(500).body("Mission creation failed!");
    }
    
    // ********** UPDATE MISSION **********
    @PutMapping("/missions")
    public ResponseEntity<?> updateMission(@RequestBody Mission mission) {
        restTemplate.put(missionServiceUrl, mission);
        return ResponseEntity.ok("Mission updated successfully!");
    }
    
 // ********** GET COMMENTS **********
    @GetMapping("/comments")
    public ResponseEntity<?> getComments(
            @RequestParam(value = "author_id", required = false) Integer authorId,
            @RequestParam(value = "receiver_id", required = false) Integer receiverId) {

        StringBuilder urlBuilder = new StringBuilder(commentServiceUrl + "?");
        if (authorId != null) {
            urlBuilder.append("author_id=").append(authorId).append("&");
        }
        if (receiverId != null) {
            urlBuilder.append("receiver_id=").append(receiverId).append("&");
        }

        String url = urlBuilder.toString().replaceAll("&$", "");

        ResponseEntity<Comment[]> response = restTemplate.getForEntity(url, Comment[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        }
        return ResponseEntity.status(404).body("No comments found!");
    }

    
    // ********** CREATE COMMENT **********
    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        ResponseEntity<Comment> response = restTemplate.postForEntity(
                commentServiceUrl, comment, Comment.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(201).body(response.getBody());
        }
        return ResponseEntity.status(500).body("Comment creation failed!");
    }
}
