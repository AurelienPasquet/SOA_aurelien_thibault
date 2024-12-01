package fr.insa.mas.commentmanagementms.controller;

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

import fr.insa.mas.commentmanagementms.model.Comment;
import jakarta.ws.rs.core.MediaType;

@RestController
public class CommentController {

	String urlDB = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_049";
	String loginDB = "projet_gei_049";
	String pwdDB = "Oezohsh2";
	
	@GetMapping(value="/comments/{id}")
	public Comment getComment(@PathVariable int id) {
		
		Comment comment = new Comment();
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			String query = "SELECT * FROM comments WHERE comment_id = " +id+ ";";
			ResultSet result = stmt.executeQuery(query);
			
			if (result.next()) {
				comment.setId(result.getInt("comment_id"));
				comment.setTitle(result.getString("comment_title"));
				comment.setBody(result.getString("comment_body"));
				comment.setAuthor_id(result.getInt("comment_author_id"));
				comment.setReceiver_id(result.getInt("comment_receiver_id"));
				comment.setGrade(result.getInt("comment_grade"));
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
		
		return comment;
	}
	
	@PostMapping(value="/comments", consumes = MediaType.APPLICATION_JSON)
	public Comment createComment(@RequestBody Comment comment) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "INSERT INTO comments (comment_author_id, comment_receiver_id, comment_grade, comment_title, comment_body) VALUES ('"
					     + comment.getAuthor_id() + "', '"
					     + comment.getReceiver_id() + "', '"
						 + comment.getGrade() + "', '"
						 + comment.getTitle() + "', '"
					     + comment.getBody()
					     + "');";
			
			System.out.println(query);
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("Comment successfully added");
			} else {
				System.out.println("Failed to add comment");
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
		return comment;
	}
	
	@PutMapping(value="/comments", consumes = MediaType.APPLICATION_JSON)
	public Comment updateComment(@RequestBody Comment comment) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "UPDATE comments SET "
						 + "comment_author_id = '" + comment.getAuthor_id() + "', "
						 + "comment_receiver_id = '" + comment.getReceiver_id() + "', "
						 + "comment_grade = '" + comment.getGrade() + "', "
						 + "comment_title = '" + comment.getTitle() + "', "
						 + "comment_body = '" + comment.getBody() + "'"
						 + "WHERE comment_id = " + comment.getId() + ";";
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("Comment successfully updated");
			} else {
				System.out.println("Failed to update comment");
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
		return comment;
	}
	
	@DeleteMapping(value="/comments/{id}")
	public void deleteComment(@PathVariable int id) {
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);
			stmt = con.createStatement();
			
			String query = "DELETE FROM comments WHERE comment_id = " + id + ";";
			
			int result = stmt.executeUpdate(query);
			
			if (result > 0) {
				System.out.println("Comment successfully deleted");
			} else {
				System.out.println("Failed to delete comment");
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
