package fr.insa.mas.OrchestratorMS.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/bdd")
public class Orchestrator {
	public Connection getConnection() {
		String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/";
		String name_bdd = "projet_gei_049";
		String passwd_bdd = "Oezohsh2";
		Connection con = null;
		try {
			con = DriverManager.getConnection(url,name_bdd,passwd_bdd);
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		return con; 
	}
	
}
