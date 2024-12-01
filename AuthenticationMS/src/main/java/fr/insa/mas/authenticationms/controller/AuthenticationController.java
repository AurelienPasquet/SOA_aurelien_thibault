package fr.insa.mas.authenticationms.controller;

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

import fr.insa.mas.authenticationms.model.Credentials;
import jakarta.ws.rs.core.MediaType;

@RestController
public class AuthenticationController {

	String urlDB = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_049";
	String loginDB = "projet_gei_049";
	String pwdDB = "Oezohsh2";
	
	@GetMapping(value="/credentials/{id}")
	public Credentials getCredentials(@PathVariable int id) {
		
		Credentials credentials = new Credentials();
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			String query = "SELECT * FROM credentials WHERE user_id = " +id+ ";";
			ResultSet result = stmt.executeQuery(query);
			
			if (result.next()) {
				credentials.setId(result.getInt("user_id"));
				credentials.setLogin(result.getString("user_login"));
				credentials.setPwd(result.getString("user_pwd"));
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
		
		return credentials;
	}
	
	@PostMapping(value="/credentials", consumes = MediaType.APPLICATION_JSON)
	public Credentials createCredentials(@RequestBody Credentials credentials) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "INSERT INTO credentials (user_id, user_login, user_pwd) VALUES ('"
					     + credentials.getId() + "', '"
					     + credentials.getLogin() + "', '"
					     + credentials.getPwd()
					     + "');";
			
			System.out.println(query);
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("Credentials successfully added");
			} else {
				System.out.println("Failed to add credentials");
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
		return credentials;
	}
	
	@PutMapping(value="/credentials", consumes = MediaType.APPLICATION_JSON)
	public Credentials updateCredentials(@RequestBody Credentials credentials) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "UPDATE credentials SET "
						 + "user_login = '" + credentials.getLogin() + "', "
						 + "user_pwd = '" + credentials.getPwd() + "'"
						 + "WHERE user_id = " + credentials.getId() + ";";
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("Credentials successfully updated");
			} else {
				System.out.println("Failed to update credentials");
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
		return credentials;
	}
	
	@DeleteMapping(value="/credentials/{id}")
	public void deleteCredentials(@PathVariable int id) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "DELETE FROM credentials WHERE user_id = " + id + ";";
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("Credentials successfully deleted");
			} else {
				System.out.println("Failed to delete credentials");
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
