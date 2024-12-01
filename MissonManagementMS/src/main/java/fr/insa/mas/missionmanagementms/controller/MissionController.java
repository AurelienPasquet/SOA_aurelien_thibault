package fr.insa.mas.missionmanagementms.controller;

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

import fr.insa.mas.missionmanagementms.model.Mission;
import jakarta.ws.rs.core.MediaType;

@RestController
public class MissionController {

	String urlDB = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_049";
	String loginDB = "projet_gei_049";
	String pwdDB = "Oezohsh2";
	
	@GetMapping(value="/missions/{id}")
	public Mission getMission(@PathVariable int id) {
		
		Mission mission = new Mission();
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			String query = "SELECT * FROM missions WHERE mission_id = " +id+ ";";
			ResultSet result = stmt.executeQuery(query);
			
			if (result.next()) {
				mission.setId(result.getInt("mission_id"));
				mission.setTitle(result.getString("mission_title"));
				mission.setBody(result.getString("mission_body"));
				mission.setVolunteer_id(result.getInt("mission_volunteer_id"));
				mission.setAsker_id(result.getInt("mission_asker_id"));
				mission.setValidator_id(result.getInt("mission_validator_id"));
				mission.setStatus(result.getString("mission_status"));
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
		
		return mission;
	}
	
	@PostMapping(value="/missions", consumes = MediaType.APPLICATION_JSON)
	public Mission createMission(@RequestBody Mission mission) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "INSERT INTO missions (mission_title, mission_body, mission_volunteer_id, mission_asker_id, mission_validator_id, mission_status) VALUES ('"
					     + mission.getTitle() + "', '"
					     + mission.getBody() + "', '"
					     + mission.getVolunteer_id() + "', '"
						 + mission.getAsker_id() + "', '"
						 + mission.getValidator_id() + "', '"
						 + mission.getStatus()
					     + "');";
			
			System.out.println(query);
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("Mission successfully added");
			} else {
				System.out.println("Failed to add mission");
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
		return mission;
	}
	
	@PutMapping(value="/missions", consumes = MediaType.APPLICATION_JSON)
	public Mission updateMission(@RequestBody Mission mission) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "UPDATE missions SET "
						 + "mission_title = '" + mission.getTitle() + "', "
						 + "mission_body = '" + mission.getBody() + "', "
						 + "mission_volunteer_id = '" + mission.getVolunteer_id() + "', "
						 + "mission_asker_id = '" + mission.getAsker_id() + "', "
						 + "mission_validator_id = '" + mission.getValidator_id() + "', "
						 + "mission_status = '" + mission.getStatus() + "'"
						 + "WHERE mission_id = " + mission.getId() + ";";
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("Mission successfully updated");
			} else {
				System.out.println("Failed to update Mission");
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
		return mission;
	}
	
	@DeleteMapping(value="/missions/{id}")
	public void deleteMission(@PathVariable int id) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "DELETE FROM missions WHERE mission_id = " + id + ";";
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("Mission successfully deleted");
			} else {
				System.out.println("Failed to delete Mission");
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
