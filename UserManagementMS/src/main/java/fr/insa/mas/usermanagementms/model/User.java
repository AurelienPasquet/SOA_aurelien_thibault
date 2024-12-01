package fr.insa.mas.usermanagementms.model;

public class User {
	
	private int id;
	private String firstname;
	private String lastname;
	private String role;
	
	public User(String firstname, String lastname, String role) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.role = role;
	}
	
	public User() {}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
		return "User [id=" +this.id+ ", firstname=" +this.firstname+ ", lastname=" +this.lastname+ ", role=" +this.role+ "]";
	}
}
