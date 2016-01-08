package bluprint;

import java.util.List;

public interface Formatter {

	void preparingScenarios(List<ScenarioInfo> scenarios);

	void executionComplete();

	void startScenario(ScenarioInfo scenario);

	void scenarioFailed(Throwable cause);

	void scenarioSucceed();

	void given(String givenText);

	void when(String whenText);

	void then(String thenText);

}
