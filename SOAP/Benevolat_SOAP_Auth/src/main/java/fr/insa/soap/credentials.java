package fr.insa.soap;

public class credentials {
	private String login;
	private String passwd;
	public int id;
	
	public credentials(String login, String passwd, int id) {
		super();
		this.login = login;
		this.passwd = passwd;
		this.id = id;
	}
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
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	


}
