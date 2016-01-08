package bluprint;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.junit.Assert;
import org.junit.Test;

public abstract class Bluprint {

	Scenario currentScenario;

	List<Reporter> reporters = new ArrayList<Reporter>();

	@FunctionalInterface
	public interface AsyncExecution {

		public void run(Locker async);

	}

	@FunctionalInterface
	public interface Execution {

		public void run();

	}

	public static class Locker {

		Semaphore semaphore = new Semaphore(1);

		public void lock() {
			try {
				this.semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void waitEnd() {
			try {
				this.semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void done() {
			this.semaphore.release();
		}
	}

	public Bluprint() {
		this.reporters.add(new ConsoleReporter());
	}

	@Test
	public void runTests() throws Exception {

		Method[] methods = getClass().getMethods();

		List<ScenarioInfo> scenarios = new ArrayList<ScenarioInfo>();
		for (Method method : methods) {
			Scenario scn = method.getAnnotation(Scenario.class);
			if (scn != null) {
				scenarios.add(buildScenarioInfo(method));
			}
		}

		for (Reporter reporter : this.reporters) {
			reporter.preparingScenarios(scenarios);
		}
		for (Method method : methods) {
			Scenario scn = method.getAnnotation(Scenario.class);
			if (scn != null) {
				this.currentScenario = scn;
				scenarioDidStart(buildScenarioInfo(method));

				try {
					method.invoke(this);
					scenarioDidSuccess(scn);
				} catch (Exception ae) {
					scenarioDidFail(scn, ae.getCause());
					for (Reporter reporter : this.reporters) {
						reporter.executionComplete();
					}
					Assert.fail(ae.getCause().getMessage());
				}

			}
		}
		for (Reporter reporter : this.reporters) {
			reporter.executionComplete();
		}

	}

	private ScenarioInfo buildScenarioInfo(final Method method) {
		Scenario scn = method.getAnnotation(Scenario.class);
		PrimaryActor actor = method.getAnnotation(PrimaryActor.class);
		Goal goal = method.getAnnotation(Goal.class);
		Id id = method.getAnnotation(Id.class);

		ScenarioInfo scenario = new ScenarioInfo();
		scenario.setName(scn.value());

		if (id != null) {
			scenario.setId(id.value());
		}

		if (actor != null) {
			scenario.setPrimaryActor(actor.value());
		}
		if (goal != null) {
			scenario.setGoal(goal.value());
		}
		return scenario;
	}

	protected void given(final String givenText) {
		handleGiven(givenText);
	}

	protected void when(final String whenText) {
		handleWhen(whenText);
	}

	protected void when(final String whenText, final AsyncExecution object) {
		handleWhen(whenText);
		Locker locker = new Locker();
		locker.lock();
		object.run(locker);
		locker.waitEnd();
	}

	protected void then(final String thenText, final AsyncExecution object) {
		handleThen(thenText);
		Locker locker = new Locker();
		locker.lock();
		object.run(locker);
		locker.waitEnd();
	}

	protected void then(final String thenText, final Execution object) {
		handleThen(thenText);
		object.run();
	}

	private void scenarioDidStart(final ScenarioInfo scenarioInfo) {
		for (Reporter reporter : this.reporters) {
			reporter.startScenario(scenarioInfo);
		}
	}

	private void scenarioDidFail(final Scenario scn, final Throwable cause) {
		for (Reporter reporter : this.reporters) {
			reporter.scenarioFailed(cause);
		}
	}

	private void scenarioDidSuccess(final Scenario scn) {
		for (Reporter reporter : this.reporters) {
			reporter.scenarioSucceed();
		}
	}

	private void handleGiven(final String givenText) {
		for (Reporter reporter : this.reporters) {
			reporter.given(givenText);
		}
	}

	private void handleWhen(final String whenText) {
		for (Reporter reporter : this.reporters) {
			reporter.when(whenText);
		}
	}

	private void handleThen(final String thenText) {
		for (Reporter reporter : this.reporters) {
			reporter.then(thenText);
		}
	}
}
