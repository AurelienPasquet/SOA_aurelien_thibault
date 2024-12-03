package fr.insa.mas.commentmanagementms.controller;

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

import fr.insa.mas.commentmanagementms.model.Comment;
import jakarta.ws.rs.core.MediaType;

@RestController
public class CommentController {

	String urlDB = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_049";
	String loginDB = "projet_gei_049";
	String pwdDB = "Oezohsh2";

	@GetMapping(value = "/comments/{id}")
	public ResponseEntity<Comment> getComment(@PathVariable int id) {
		Comment comment = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet result = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "SELECT * FROM comments WHERE comment_id = ?;";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, id);

			result = stmt.executeQuery();

			if (result.next()) {
				comment = new Comment();
				comment.setId(result.getInt("comment_id"));
				comment.setTitle(result.getString("comment_title"));
				comment.setBody(result.getString("comment_body"));
				comment.setAuthor_id(result.getInt("comment_author_id"));
				comment.setReceiver_id(result.getInt("comment_receiver_id"));
				comment.setGrade(result.getInt("comment_grade"));
			}

			if (comment == null) {
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

		return ResponseEntity.ok(comment);
	}

	@PostMapping(value = "/comments", consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet generatedKeys = null;

	    try {
	        con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

	        String query = "INSERT INTO comments (comment_author_id, comment_receiver_id, comment_grade, comment_title, comment_body) " +
	                       "VALUES (?, ?, ?, ?, ?);";
	        stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

	        stmt.setInt(1, comment.getAuthor_id());
	        stmt.setInt(2, comment.getReceiver_id());
	        stmt.setInt(3, comment.getGrade());
	        stmt.setString(4, comment.getTitle());
	        stmt.setString(5, comment.getBody());

	        int affectedRows = stmt.executeUpdate();

	        if (affectedRows > 0) {
	            generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                comment.setId(generatedKeys.getInt(1));
	                System.out.println("Comment successfully added with ID: " + comment.getId());
	                return ResponseEntity.status(HttpStatus.SC_CREATED).body(comment);
	            } else {
	                throw new SQLException("Failed to retrieve generated comment ID.");
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).build();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
	    } finally {
	        try {
	            if (generatedKeys != null) generatedKeys.close();
	            if (stmt != null) stmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	@PutMapping(value = "/comments", consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "UPDATE comments SET " + "comment_author_id = ?, " + "comment_receiver_id = ?, "
					+ "comment_grade = ?, " + "comment_title = ?, " + "comment_body = ? " + "WHERE comment_id = ?;";
			stmt = con.prepareStatement(query);

			stmt.setInt(1, comment.getAuthor_id());
			stmt.setInt(2, comment.getReceiver_id());
			stmt.setInt(3, comment.getGrade());
			stmt.setString(4, comment.getTitle());
			stmt.setString(5, comment.getBody());
			stmt.setInt(6, comment.getId());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Comment successfully updated.");
				return ResponseEntity.ok(comment);
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

	@DeleteMapping(value = "/comments/{id}")
	public ResponseEntity<Void> deleteComment(@PathVariable int id) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DriverManager.getConnection(urlDB, loginDB, pwdDB);

			String query = "DELETE FROM comments WHERE comment_id = ?;";
			stmt = con.prepareStatement(query);

			stmt.setInt(1, id);

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Comment successfully deleted.");
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
