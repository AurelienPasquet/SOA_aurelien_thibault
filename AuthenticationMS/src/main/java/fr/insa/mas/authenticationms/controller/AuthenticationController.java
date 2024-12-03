package fr.insa.mas.authenticationms.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.hc.core5.http.HttpStatus;
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
				return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
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
	public ResponseEntity<Boolean> checkLoginExists(@RequestParam("login") String login) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet result = null;

		try {
			// Establish database connection
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			// Query to check if the login exists
			String query = "SELECT COUNT(*) AS count FROM credentials WHERE user_login = ?;";
			stmt = con.prepareStatement(query);
			stmt.setString(1, login);

			result = stmt.executeQuery();

			if (result.next()) {
				int count = result.getInt("count");
				return ResponseEntity.ok(count > 0); // Return true if login exists, otherwise false
			} else {
				return ResponseEntity.ok(false); // Return false if no result is found
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null); // Return 500 on database error
		} finally {
			// Clean up resources
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
				return ResponseEntity.status(HttpStatus.SC_CREATED).body(credentials);
			} else {
				return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
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
	public ResponseEntity<Integer> validateCredentials(@RequestBody Credentials request) {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet result = null;

	    try {
	        // Validate input
	        if (request.getLogin() == null || request.getPassword() == null) {
	            return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
	        }

	        // Establish database connection
	        con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

	        // Query to validate credentials and fetch user_id
	        String query = "SELECT user_id FROM credentials WHERE user_login = ? AND user_pwd = ?;";
	        stmt = con.prepareStatement(query);
	        stmt.setString(1, request.getLogin());
	        stmt.setString(2, request.getPassword());

	        result = stmt.executeQuery();

	        if (result.next()) {
	            int userId = result.getInt("user_id");
	            return ResponseEntity.ok(userId); // Return user_id if credentials are valid
	        } else {
	            return ResponseEntity.ok(0); // Return 0 if no match is found
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null); // Return 500 on database error
	    } finally {
	        // Clean up resources
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
				return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
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
				return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
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
