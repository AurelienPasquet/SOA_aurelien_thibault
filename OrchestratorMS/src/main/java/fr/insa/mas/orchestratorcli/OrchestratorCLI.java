package fr.insa.mas.orchestratorcli;

import java.util.Scanner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import fr.insa.mas.orchestratorms.model.Comment;
import fr.insa.mas.orchestratorms.model.Mission;
import fr.insa.mas.orchestratorms.model.SignInRequest;
import fr.insa.mas.orchestratorms.model.SignUpRequest;
import fr.insa.mas.orchestratorms.model.User;

public class OrchestratorCLI {

    private static final String BASE_URL = "http://localhost:8093/orchestrator";
    private static final RestTemplate restTemplate = new RestTemplate();

    private static int currentUserId = -1;
    private static String currentUserRole = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Créer un compte");
            System.out.println("2. S'authentifier");
            if (currentUserId != -1) {
                System.out.println("3. Créer une mission");
                System.out.println("4. Voir les missions disponibles");
                System.out.println("5. Accepter une mission");
                System.out.println("6. Mettre un avis");
                System.out.println("7. Voir les commentaires");
                System.out.println("8. Se déconnecter");
            }
            System.out.println("0. Quitter");

            System.out.print("\nVotre choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); 

            switch (choix) {
                case 1 -> createAccount(scanner);
                case 2 -> authenticate(scanner);
                case 3 -> createMission(scanner);
                case 4 -> viewAvailableMissions();
                case 5 -> acceptMission(scanner);
                case 6 -> leaveComment(scanner);
                case 7 -> viewComments();
                case 8 -> logout();
                case 0 -> System.exit(0);
                default -> System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    private static void createAccount(Scanner scanner) {
        System.out.println("\n--- Création de Compte ---");
        SignUpRequest request = new SignUpRequest();

        System.out.print("Prénom : ");
        request.setFirstname(scanner.nextLine());

        System.out.print("Nom : ");
        request.setLastname(scanner.nextLine());

        String role;
        while (true) {
            System.out.print("Rôle (Volunteer/Requester) : ");
            role = scanner.nextLine().trim();
            if (role.equalsIgnoreCase("Volunteer") || role.equalsIgnoreCase("Requester")) {
                request.setRole(role);
                break;
            } else {
                System.out.println("Rôle invalide. Veuillez entrer 'Volunteer' ou 'Requester'.");
            }
        }

        if (role.equalsIgnoreCase("Requester")) {
            System.out.print("Nom du validateur : ");
            request.setValidatorName(scanner.nextLine());
        } else {
            request.setValidatorName("null");
        }

        System.out.print("Login : ");
        request.setLogin(scanner.nextLine());

        System.out.print("Mot de passe : ");
        request.setPassword(scanner.nextLine());

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/signup", request, String.class);
        System.out.println(response.getBody());
    }


    private static void authenticate(Scanner scanner) {
        System.out.println("\n--- Authentification ---");
        SignInRequest request = new SignInRequest();

        System.out.print("Login : ");
        request.setLogin(scanner.nextLine());

        System.out.print("Mot de passe : ");
        request.setPassword(scanner.nextLine());

        ResponseEntity<User> response = restTemplate.postForEntity(BASE_URL + "/signin", request, User.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            User user = response.getBody();
            currentUserId = user.getId();
            currentUserRole = user.getRole();
            System.out.println("Authentification réussie. Bienvenue " + user.getFirstname() + " !");
        } else {
            System.out.println("Échec de l'authentification. Veuillez vérifier vos identifiants.");
        }
    }

    private static void createMission(Scanner scanner) {
        if (currentUserId == -1) {
            System.out.println("Veuillez vous authentifier d'abord.");
            return;
        }

        System.out.println("\n--- Création de Mission ---");
        Mission mission = new Mission();

        System.out.print("Titre de la mission : ");
        mission.setTitle(scanner.nextLine());

        System.out.print("Description : ");
        mission.setBody(scanner.nextLine());

        if (currentUserRole.equalsIgnoreCase("Volunteer")) {
            mission.setAsker_id(0);
            mission.setVolunteer_id(currentUserId);
        } else {
            mission.setAsker_id(currentUserId);
            mission.setVolunteer_id(0);
        }
        mission.setStatus("en attente");

        ResponseEntity<Mission> response = restTemplate.postForEntity(BASE_URL + "/missions", mission, Mission.class);
        System.out.println(response.getBody() != null ? "Mission créée avec succès !" : "Échec de la création.");
    }

    private static void viewAvailableMissions() {
        if (currentUserId == -1) {
            System.out.println("Veuillez vous authentifier d'abord.");
            return;
        }

        String queryParam = !currentUserRole.equalsIgnoreCase("Volunteer") ? "?asker_id=0&status=en attente" : "?volunteer_id=0&status=en attente";

        ResponseEntity<Mission[]> response = restTemplate.getForEntity(BASE_URL + "/missions" + queryParam, Mission[].class);

        if (response.getBody() != null && response.getBody().length > 0) {
            System.out.println("\n--- Missions Disponibles ---");
            for (Mission mission : response.getBody()) {
                System.out.println("ID: " + mission.getId() + " | Titre: " + mission.getTitle() + " | Status: " + mission.getStatus());
            }
        } else {
            System.out.println("Aucune mission disponible.");
        }
    }

    private static void acceptMission(Scanner scanner) {
        if (currentUserId == -1) {
            System.out.println("Veuillez vous authentifier d'abord.");
            return;
        }

        System.out.print("ID de la mission à accepter : ");
        int missionId = scanner.nextInt();
        scanner.nextLine();

        String getMissionUrl = BASE_URL + "/missions?mission_id=" + missionId;

        try {
            ResponseEntity<Mission[]> response = restTemplate.getForEntity(getMissionUrl, Mission[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
                Mission mission = response.getBody()[0];

                if (currentUserRole.equalsIgnoreCase("Volunteer")) {
                    mission.setVolunteer_id(currentUserId);
                } else if (currentUserRole.equalsIgnoreCase("Requester")) {
                    mission.setAsker_id(currentUserId);
                }

                mission.setStatus("acceptée");

                restTemplate.put(BASE_URL + "/missions", mission);
                System.out.println("Mission acceptée avec succès !");
            } else {
                System.out.println("Mission introuvable.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'acceptation de la mission : " + e.getMessage());
        }
    }


    private static void leaveComment(Scanner scanner) {
        if (!currentUserRole.equalsIgnoreCase("Requester")) {
            System.out.println("Seuls les Requesters peuvent laisser des commentaires.");
            return;
        }

        System.out.println("\n--- Laisser un Commentaire ---");
        Comment comment = new Comment();
        comment.setAuthor_id(currentUserId);

        System.out.print("ID du volontaire : ");
        comment.setReceiver_id(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Titre : ");
        comment.setTitle(scanner.nextLine());

        System.out.print("Commentaire : ");
        comment.setBody(scanner.nextLine());

        System.out.print("Note (1 à 5) : ");
        comment.setGrade(scanner.nextInt());
        scanner.nextLine();

        restTemplate.postForEntity(BASE_URL + "/comments", comment, Comment.class);
        System.out.println("Commentaire ajouté !");
    }

    private static void viewComments() {
        if (currentUserId == -1) {
            System.out.println("Veuillez vous authentifier d'abord.");
            return;
        }

        String roleParam = currentUserRole.equalsIgnoreCase("Volunteer") ? "receiver_id" : "author_id";

        ResponseEntity<Comment[]> response = restTemplate.getForEntity(BASE_URL + "/comments?" + roleParam + "=" + currentUserId, Comment[].class);

        System.out.println("\n--- Mes Commentaires ---");
        for (Comment comment : response.getBody()) {
            System.out.println("Titre: " + comment.getTitle() + " | Note: " + comment.getGrade());
        }
    }

    private static void logout() {
        currentUserId = -1;
        currentUserRole = null;
        System.out.println("Déconnexion réussie !");
    }
}
