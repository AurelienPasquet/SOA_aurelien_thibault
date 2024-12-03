package fr.insa.mas.orchestratorms.model;

public class SignUpRequest {
    
	private String firstname;
    private String lastname;
    private String role;
    private String validatorName;
	private String login;
    private String password;
    
    public SignUpRequest(String firstname, String lastname, String role, String validatorName, String login, String password) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.role = role;
		this.validatorName = validatorName;
		this.login = login;
		this.password = password;
	}
    
    public SignUpRequest() {}

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getValidatorName() {return validatorName;}
	public void setValidatorName(String validatorName) {this.validatorName = validatorName;}

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
