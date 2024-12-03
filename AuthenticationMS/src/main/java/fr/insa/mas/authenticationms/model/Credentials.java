package fr.insa.mas.authenticationms.model;

public class Credentials {
	
	private int id;
	private String login;
	private String password;
	
	public Credentials(int id, String login, String password) {
		this.id = id;
		this.login = login;
		this.password = password;
	}
	
	public Credentials() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}
}
