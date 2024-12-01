
package fr.insa.mas.ValidationMS.resources;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.mas.ValidationMS.model.Mission;

@RestController
public class ValidationController {

    String urlDB = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_049";
    String loginDB = "projet_gei_049";
    String pwdDB = "Oezohsh2";

    @GetMapping("/validate/{id}")
    public Mission getMission(@PathVariable int id) {
        Mission mission = new Mission();
        Connection con = null;
        Statement stmt = null;

        try {
            con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
            stmt = con.createStatement();
            String query = "SELECT * FROM missions WHERE mission_id = " + id + ";";
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

    @PutMapping("/validate/{id}")
    public Mission validateMission(@PathVariable int id) {
        Mission mission = new Mission();
        Connection con = null;
        Statement stmt = null;

        try {
            con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
            stmt = con.createStatement();
            String selectQuery = "SELECT * FROM missions WHERE mission_id = " + id + ";";
            ResultSet result = stmt.executeQuery(selectQuery);

            if (result.next()) {
                mission.setId(result.getInt("mission_id"));
                mission.setTitle(result.getString("mission_title"));
                mission.setBody(result.getString("mission_body"));
                mission.setVolunteer_id(result.getInt("mission_volunteer_id"));
                mission.setAsker_id(result.getInt("mission_asker_id"));
                mission.setValidator_id(result.getInt("mission_validator_id"));
                mission.setStatus(result.getString("mission_status"));

                if ("Envoyée".equals(mission.getStatus())) {
                    String updateQuery = "UPDATE missions SET mission_status = 'Validée' WHERE mission_id = " + id + ";";
                    stmt.executeUpdate(updateQuery);
                    mission.setStatus("Validée");
                }
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
}

