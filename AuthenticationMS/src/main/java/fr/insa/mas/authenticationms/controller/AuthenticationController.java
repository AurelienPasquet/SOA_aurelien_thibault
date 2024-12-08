package fr.insa.mas.authenticationms.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.mas.authenticationms.model.Credentials;
import jakarta.ws.rs.core.MediaType;

@RestController
public class AuthenticationController {

	String urlDB = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_049";
	String loginDB = "projet_gei_049";
	String pwdDB = "Oezohsh2";

	@GetMapping(value = "/credentials/{id}")
	public ResponseEntity<Credentials> getCredentials(@PathVariable int id) {
		Credentials credentials = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet result = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "SELECT * FROM credentials WHERE user_id = ?;";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, id);

			result = stmt.executeQuery();

			if (result.next()) {
				credentials = new Credentials();
				credentials.setId(result.getInt("user_id"));
				credentials.setLogin(result.getString("user_login"));
				credentials.setPassword(result.getString("user_pwd"));
			}

			if (credentials == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			try {
				if (result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ResponseEntity.ok(credentials);
	}
	
	@GetMapping(value = "/credentials")
	public ResponseEntity<List<Credentials>> getCredentials(
	        @RequestParam(value = "user_id", required = false) Integer userId,
	        @RequestParam(value = "login", required = false) String login,
	        @RequestParam(value = "password", required = false) String password
	) {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet result = null;

	    List<Credentials> credentials = new ArrayList<>();

	    try {
	        con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

	        StringBuilder query = new StringBuilder("SELECT * FROM credentials WHERE 1=1");
	        List<Object> params = new ArrayList<>();

	        if (userId != null) {
	            query.append(" AND user_id = ?");
	            params.add(userId);
	        }
	        if (login != null && !login.isEmpty()) {
	            query.append(" AND user_login = ?");
	            params.add(login);
	        }
	        if (password != null && !password.isEmpty()) {
	            query.append(" AND user_pwd = ?");
	            params.add(password);
	        }

	        stmt = con.prepareStatement(query.toString());

	        for (int i = 0; i < params.size(); i++) {
	            stmt.setObject(i + 1, params.get(i));
	        }

	        result = stmt.executeQuery();

	        while (result.next()) {
	            Credentials credential = new Credentials();
	            credential.setId(result.getInt("user_id"));
	            credential.setLogin(result.getString("user_login"));
	            credential.setPassword(result.getString("user_pwd"));
	            credentials.add(credential);
	        }

	        return ResponseEntity.ok(credentials);

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } finally {
	        try {
	            if (result != null) result.close();
	            if (stmt != null) stmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}


	@PostMapping(value = "/credentials", consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<Credentials> createCredentials(@RequestBody Credentials credentials) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "INSERT INTO credentials (user_id, user_login, user_pwd) VALUES (?, ?, ?);";
			stmt = con.prepareStatement(query);

			stmt.setInt(1, credentials.getId());
			stmt.setString(2, credentials.getLogin());
			stmt.setString(3, credentials.getPassword());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Credentials successfully added.");
				return ResponseEntity.status(HttpStatus.CREATED).body(credentials);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@PostMapping(value = "/credentials/check", consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> validateCredentials(@RequestBody Credentials request) {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet result = null;

	    try {
	        if (request.getLogin() == null || request.getPassword() == null) {
	            return ResponseEntity.badRequest().body("Login et mot de passe requis.");
	        }

	        con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

	        String loginQuery = "SELECT user_id, user_pwd FROM credentials WHERE user_login = ?;";
	        stmt = con.prepareStatement(loginQuery);
	        stmt.setString(1, request.getLogin());

	        result = stmt.executeQuery();

	        if (result.next()) {
	            String correctPassword = result.getString("user_pwd");

	            if (request.getPassword().equals(correctPassword)) {
	                int userId = result.getInt("user_id");
	                return ResponseEntity.ok(String.valueOf(userId));
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mot de passe incorrect.");
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur.");
	    } finally {
	        try {
	            if (result != null) result.close();
	            if (stmt != null) stmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}


	@PutMapping(value = "/credentials", consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<Credentials> updateCredentials(@RequestBody Credentials credentials) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "UPDATE credentials SET user_login = ?, user_pwd = ? WHERE user_id = ?;";
			stmt = con.prepareStatement(query);

			stmt.setString(1, credentials.getLogin());
			stmt.setString(2, credentials.getPassword());
			stmt.setInt(3, credentials.getId());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Credentials successfully updated.");
				return ResponseEntity.ok(credentials);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@DeleteMapping(value = "/credentials/{id}")
	public ResponseEntity<Void> deleteCredentials(@PathVariable int id) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "DELETE FROM credentials WHERE user_id = ?;";
			stmt = con.prepareStatement(query);

			stmt.setInt(1, id);

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Credentials successfully deleted.");
				return ResponseEntity.noContent().build();
			} else {
				System.out.println("No credentials found with ID: " + id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
