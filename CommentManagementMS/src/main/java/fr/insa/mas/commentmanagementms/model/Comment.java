package fr.insa.mas.commentmanagementms.model;

public class Comment {

	private String title;
	private String body;
	private int id;
	private int author_id;
	private int receiver_id;
	private int grade;
	
	public Comment(String title, String body, int author_id, int receiver_id, int grade) {
		this.title = title;
		this.body = body;
		this.author_id = author_id;
		this.receiver_id = receiver_id;
		this.grade = grade;
	}
	
	public Comment() {}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public int getReceiver_id() {
		return receiver_id;
	}

	public void setReceiver_id(int receiver_id) {
		this.receiver_id = receiver_id;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
	
}
