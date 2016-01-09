package bluprint;

public class ScenarioInfo {

	private String id;
	private String name;
	private String primaryActor;
	private String goal;

	public void setId(final String id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPrimaryActor(final String value) {
		this.primaryActor = value;
	}

	public void setGoal(final String value) {
		this.goal = value;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getPrimaryActor() {
		return this.primaryActor;
	}

	public String getGoal() {
		return this.goal;
	}

}
