package fr.insa.mas.usermanagementms.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	@GetMapping(value="/users/{id}")
	public User getUser(@PathVariable int id) {
		
		User user = new User();
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			String query = "SELECT * FROM users WHERE user_id = " +id+ ";";
			ResultSet result = stmt.executeQuery(query);
			
			if (result.next()) {
				user.setId(result.getInt("user_id"));
				user.setFirstname(result.getString("user_firstname"));
				user.setLastname(result.getString("user_lastname"));
				user.setRole(result.getString("user_role"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return user;
	}
	
	@PostMapping(value="/users", consumes = MediaType.APPLICATION_JSON)
	public User createUser(@RequestBody User user) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "INSERT INTO users (user_firstname, user_lastname, user_role) VALUES ('"
					     + user.getFirstname() + "', '"
					     + user.getLastname() + "', '"
					     + user.getRole()
					     + "');";
			
			System.out.println(query);
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("User successfully added");
			} else {
				System.out.println("Failed to add user");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}
	
	@PutMapping(value="/users", consumes = MediaType.APPLICATION_JSON)
	public User updateUser(@RequestBody User user) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "UPDATE users SET "
						 + "user_firstname = '" + user.getFirstname() + "', "
						 + "user_lastname = '" + user.getLastname() + "', "
						 + "user_role = '" + user.getRole() + "'"
						 + "WHERE user_id = " + user.getId() + ";";
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("User successfully updated");
			} else {
				System.out.println("Failed to update user");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}
	
	@DeleteMapping(value="/users/{id}")
	public void deleteUser(@PathVariable int id) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "DELETE FROM users WHERE user_id = " + id + ";";
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("User successfully deleted");
			} else {
				System.out.println("Failed to delete user");
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
