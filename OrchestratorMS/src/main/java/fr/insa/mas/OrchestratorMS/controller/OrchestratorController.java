package fr.insa.mas.orchestratorms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.insa.mas.orchestratorms.model.Credentials;
import fr.insa.mas.orchestratorms.model.User;
import jakarta.ws.rs.core.MediaType;
import fr.insa.mas.orchestratorms.model.SignUpRequest;

@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController {

	//@Autowired
	private RestTemplate restTemplate = new RestTemplate();

	@PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
        // Validate input
        if (request.getFirstname() == null || request.getLastname() == null ||
            request.getRole() == null || request.getLogin() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        if (!request.getRole().equals("Volunteer") &&
            !request.getRole().equals("Requester") &&
            !request.getRole().equals("Validator")) {
            return ResponseEntity.badRequest().body("Invalid role. Must be 'Volunteer', 'Requester', or 'Validator'.");
        }

        // Define URLs for services
        String userServiceUrl = "http://localhost:8081/users";
        String authServiceUrl = "http://localhost:8084/credentials";
        String checkLoginUrl = "http://localhost:8084/credentials?login=" + request.getLogin();

        try {
            // Check if login already exists
            ResponseEntity<Boolean> checkLoginResponse = restTemplate.getForEntity(checkLoginUrl, Boolean.class);
            if (checkLoginResponse.getBody() != null && checkLoginResponse.getBody()) {
                return ResponseEntity.badRequest().body("Login already exists.");
            }

            // Create user
            User user = new User();
            user.setFirstname(request.getFirstname());
            user.setLastname(request.getLastname());
            user.setRole(request.getRole());

            ResponseEntity<User> userResponse = restTemplate.postForEntity(userServiceUrl, user, User.class);

            if (userResponse.getStatusCode().is2xxSuccessful() && userResponse.getBody() != null) {
                User createdUser = userResponse.getBody();

                // Create credentials
                Credentials credentials = new Credentials();
                credentials.setId(createdUser.getId());
                credentials.setLogin(request.getLogin());
                credentials.setPwd(request.getPassword());

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
}
