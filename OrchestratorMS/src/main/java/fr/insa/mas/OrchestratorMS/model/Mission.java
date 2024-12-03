package fr.insa.mas.orchestratorms.model;

public class Mission {

	private int id;
	private String title;
	private String body;
	private int volunteer_id;
	private int asker_id;
	private int validator_id;
	private String status;
	
	public Mission(int id, String title, String body, int volunteer_id, int asker_id, int validator_id, String status) {
		super();
		this.id = id;
		this.title = title;
		this.body = body;
		this.volunteer_id = volunteer_id;
		this.asker_id = asker_id;
		this.validator_id = validator_id;
		this.status = status;
	}
	
	public Mission() {}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public int getVolunteer_id() {
		return volunteer_id;
	}

	public void setVolunteer_id(int volunteer_id) {
		this.volunteer_id = volunteer_id;
	}

	public int getAsker_id() {
		return asker_id;
	}

	public void setAsker_id(int asker_id) {
		this.asker_id = asker_id;
	}

	public int getValidator_id() {
		return validator_id;
	}

	public void setValidator_id(int validator_id) {
		this.validator_id = validator_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
