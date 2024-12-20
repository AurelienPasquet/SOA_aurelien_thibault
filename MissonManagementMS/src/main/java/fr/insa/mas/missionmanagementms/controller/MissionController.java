package fr.insa.mas.missionmanagementms.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

import fr.insa.mas.missionmanagementms.model.Mission;
import jakarta.ws.rs.core.MediaType;

@RestController
public class MissionController {

	String urlDB = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_049";
	String loginDB = "projet_gei_049";
	String pwdDB = "Oezohsh2";

	@GetMapping(value = "/missions/{id}")
	public ResponseEntity<Mission> getMission(@PathVariable int id) {
		Mission mission = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet result = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "SELECT * FROM missions WHERE mission_id = ?;";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, id);

			result = stmt.executeQuery();

			if (result.next()) {
				mission = new Mission();
				mission.setId(result.getInt("mission_id"));
				mission.setTitle(result.getString("mission_title"));
				mission.setBody(result.getString("mission_body"));
				mission.setVolunteer_id(result.getInt("mission_volunteer_id"));
				mission.setAsker_id(result.getInt("mission_asker_id"));
				mission.setStatus(result.getString("mission_status"));
			}

			if (mission == null) {
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

		return ResponseEntity.ok(mission);
	}
	
	@GetMapping(value = "/missions")
	public ResponseEntity<List<Mission>> getMissions(
	        @RequestParam(value = "id", required = false) Integer missionId,
	        @RequestParam(value = "title", required = false) String title,
	        @RequestParam(value = "body", required = false) String body,
	        @RequestParam(value = "volunteer_id", required = false) Integer volunteerId,
	        @RequestParam(value = "asker_id", required = false) Integer askerId,
	        @RequestParam(value = "status", required = false) String status
	) {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet result = null;

	    List<Mission> missions = new ArrayList<>();

	    try {
	        con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

	        StringBuilder query = new StringBuilder("SELECT * FROM missions WHERE 1=1");
	        List<Object> params = new ArrayList<>();

	        if (missionId != null) {
	            query.append(" AND mission_id = ?");
	            params.add(missionId);
	        }
	        if (title != null && !title.isEmpty()) {
	            query.append(" AND mission_title = ?");
	            params.add(title);
	        }
	        if (body != null && !body.isEmpty()) {
	            query.append(" AND mission_body = ?");
	            params.add(body);
	        }
	        if (volunteerId != null) {
	            query.append(" AND mission_volunteer_id = ?");
	            params.add(volunteerId);
	        }
	        if (askerId != null) {
	            query.append(" AND mission_asker_id = ?");
	            params.add(askerId);
	        }
	        if (status != null && !status.isEmpty()) {
	            query.append(" AND mission_status = ?");
	            params.add(status);
	        }

	        stmt = con.prepareStatement(query.toString());

	        for (int i = 0; i < params.size(); i++) {
	            stmt.setObject(i + 1, params.get(i));
	        }

	        result = stmt.executeQuery();

	        while (result.next()) {
	            Mission mission = new Mission();
	            mission.setId(result.getInt("mission_id"));
	            mission.setTitle(result.getString("mission_title"));
	            mission.setBody(result.getString("mission_body"));
	            mission.setVolunteer_id(result.getInt("mission_volunteer_id"));
	            mission.setAsker_id(result.getInt("mission_asker_id"));
	            mission.setStatus(result.getString("mission_status"));
	            missions.add(mission);
	        }

	        if (missions.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.SC_OK).build();
	        }

	        return ResponseEntity.ok(missions);

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
	}


	@PostMapping(value = "/missions", consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<Mission> createMission(@RequestBody Mission mission) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet generatedKeys = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "INSERT INTO missions (mission_title, mission_body, mission_volunteer_id, mission_asker_id, mission_status) "
					+ "VALUES (?, ?, ?, ?, ?);";
			stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, mission.getTitle());
			stmt.setString(2, mission.getBody());
			stmt.setInt(3, mission.getVolunteer_id());
			stmt.setInt(4, mission.getAsker_id());
			stmt.setString(5, mission.getStatus());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					mission.setId(generatedKeys.getInt(1));
					System.out.println("Mission successfully added with ID: " + mission.getId());
					return ResponseEntity.status(HttpStatus.SC_CREATED).body(mission);
				} else {
					throw new SQLException("Failed to retrieve generated mission ID.");
				}
			} else {
				return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
		} finally {
			try {
				if (generatedKeys != null)
					generatedKeys.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@PutMapping(value = "/missions", consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<Mission> updateMission(@RequestBody Mission mission) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "UPDATE missions SET " + "mission_title = ?, " + "mission_body = ?, "
					+ "mission_volunteer_id = ?, " + "mission_asker_id = ?, " + "mission_status = ? "
					+ "WHERE mission_id = ?;";
			stmt = con.prepareStatement(query);

			stmt.setString(1, mission.getTitle());
			stmt.setString(2, mission.getBody());
			stmt.setInt(3, mission.getVolunteer_id());
			stmt.setInt(4, mission.getAsker_id());
			stmt.setString(5, mission.getStatus());
			stmt.setInt(6, mission.getId());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Mission successfully updated.");
				return ResponseEntity.ok(mission);
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

	@DeleteMapping(value = "/missions/{id}")
	public ResponseEntity<Void> deleteMission(@PathVariable int id) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "DELETE FROM missions WHERE mission_id = ?;";
			stmt = con.prepareStatement(query);

			stmt.setInt(1, id);

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Mission successfully deleted.");
				return ResponseEntity.noContent().build();
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

}
