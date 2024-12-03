package fr.insa.mas.usermanagementms.controller;

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
import org.springframework.web.bind.annotation.RestController;

import fr.insa.mas.usermanagementms.model.User;
import jakarta.ws.rs.core.MediaType;

@RestController
public class UserController {

	String urlDB = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_049";
	String loginDB = "projet_gei_049";
	String pwdDB = "Oezohsh2";
	
	@GetMapping(value = "/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable int id) {
	    User user = null;
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet result = null;

	    try {
	        con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

	        String query = "SELECT * FROM users WHERE user_id = ?;";
	        stmt = con.prepareStatement(query);
	        stmt.setInt(1, id);
	        result = stmt.executeQuery();

	        if (result.next()) {
	            user = new User();
	            user.setId(result.getInt("user_id"));
	            user.setFirstname(result.getString("user_firstname"));
	            user.setLastname(result.getString("user_lastname"));
	            user.setRole(result.getString("user_role"));
	        }

	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
	    } finally {
	        try {
	            if (result != null) result.close();
	            if (stmt != null) stmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return ResponseEntity.ok(user);
	}

	
	@PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<User> createUser(@RequestBody User user) {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet generatedKeys = null;

	    try {
	        con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

	        String query = "INSERT INTO users (user_firstname, user_lastname, user_role) VALUES (?, ?, ?);";
	        stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

	        stmt.setString(1, user.getFirstname());
	        stmt.setString(2, user.getLastname());
	        stmt.setString(3, user.getRole());

	        int affectedRows = stmt.executeUpdate();

	        if (affectedRows > 0) {
	            generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int newId = generatedKeys.getInt(1);
	                user.setId(newId);
	                System.out.println("User successfully added with ID: " + newId);
	            } else {
	                return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
	                        .body(null);
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(null);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
	    } finally {
	        try {
	            if (generatedKeys != null) generatedKeys.close();
	            if (stmt != null) stmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return ResponseEntity.status(HttpStatus.SC_CREATED).body(user);
	}


	@PutMapping(value = "/users", consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<User> updateUser(@RequestBody User user) {
	    Connection con = null;
	    PreparedStatement stmt = null;

	    try {
	        con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

	        String query = "UPDATE users SET user_firstname = ?, user_lastname = ?, user_role = ? WHERE user_id = ?;";
	        stmt = con.prepareStatement(query);

	        stmt.setString(1, user.getFirstname());
	        stmt.setString(2, user.getLastname());
	        stmt.setString(3, user.getRole());
	        stmt.setInt(4, user.getId());

	        int affectedRows = stmt.executeUpdate();

	        if (affectedRows > 0) {
	            System.out.println("User successfully updated.");
	            return ResponseEntity.ok(user);
	        } else {
	            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}


	@DeleteMapping(value = "/users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable int id) {
	    Connection con = null;
	    PreparedStatement stmt = null;

	    try {
	        con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

	        String query = "DELETE FROM users WHERE user_id = ?;";
	        stmt = con.prepareStatement(query);

	        stmt.setInt(1, id);

	        int affectedRows = stmt.executeUpdate();

	        if (affectedRows > 0) {
	            System.out.println("User successfully deleted.");
	            return ResponseEntity.noContent().build();
	        } else {
	            System.out.println("No user found with ID: " + id);
	            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}	
}
