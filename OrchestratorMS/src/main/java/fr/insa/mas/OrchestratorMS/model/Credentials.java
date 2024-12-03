package fr.insa.mas.orchestratorms.model;

public class Credentials {
	
	private int id;
	private String login;
	private String pwd;
	
	public Credentials(int id, String login, String pwd) {
		this.id = id;
		this.login = login;
		this.pwd = pwd;
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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}

