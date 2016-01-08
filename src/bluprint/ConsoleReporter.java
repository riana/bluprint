package bluprint;

import java.util.List;

public class ConsoleReporter implements Reporter {

	enum State {
		Given, When, Then
	}

	private State previousState = null;

	@Override
	public void preparingScenarios(final List<ScenarioInfo> scenarios) {
		System.out.println("Preparing execution : ");
		for (ScenarioInfo scenarioInfo : scenarios) {
			System.out.println("\t# " + scenarioInfo.getName());
		}

		System.out.println();
	}

	@Override
	public void executionComplete() {
		System.out.println();
		System.out.println("Execution Complete!");
		System.out.println();
	}

	@Override
	public void startScenario(final ScenarioInfo scenario) {
		System.out.println("Scenario : " + scenario.getName());
		if (scenario.getId() != null) {
			System.out.println("id : " + scenario.getId());
		}
		if (scenario.getPrimaryActor() != null && scenario.getGoal() != null) {
			System.out.println("Description:");
			System.out.println("\tAs a " + scenario.getPrimaryActor());
			System.out.println("\tI want to " + scenario.getGoal());
			System.out.println();
		}
		System.out.println("Execution:");
	}

	@Override
	public void scenarioFailed(final Throwable cause) {
		System.err.println("[Failed]");
		cause.printStackTrace();
		System.out.println();
	}

	@Override
	public void scenarioSucceed() {
		System.out.println("Success!");
		System.out.println();
	}

	@Override
	public void given(final String givenText) {
		String prefix = "Given";
		if (this.previousState == State.Given) {
			prefix = "And";
		}
		System.out.println("\t " + prefix + " " + givenText);
		this.previousState = State.Given;
	}

	@Override
	public void when(final String whenText) {
		String prefix = "When";
		if (this.previousState == State.When) {
			prefix = "And";
		}
		System.out.println("\t > " + prefix + " " + whenText);
		this.previousState = State.When;
	}

	@Override
	public void then(final String thenText) {
		String prefix = "Then";
		if (this.previousState == State.Then) {
			prefix = "And";
		}
		System.out.println("\t => " + prefix + " " + thenText);
		this.previousState = State.Then;

	}

}
