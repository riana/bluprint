package bluprint;

import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

import org.junit.Assert;
import org.junit.Test;

public abstract class Bluprint {

	Scenario currentScenario;

	@FunctionalInterface
	public interface AsyncContext {

		public void run(Locker async);

	}

	@FunctionalInterface
	public interface Context {

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

	}

	@Test
	public void runTests() throws Exception {

		Method[] methods = getClass().getMethods();
		// TODO publish scenario & story status
		for (Method method : methods) {
			Scenario scn = method.getAnnotation(Scenario.class);
			if (scn != null) {
				System.out
						.println("Scenario : " + scn.name() + " #" + scn.id());
			}
		}
		for (Method method : methods) {
			Scenario scn = method.getAnnotation(Scenario.class);
			if (scn != null) {
				this.currentScenario = scn;
				System.out.println("Starting scenario: " + scn.name());
				try {
					method.invoke(this);
					System.out.println("Test complete!");
					System.out.println();
				} catch (Exception ae) {
					ae.getCause().printStackTrace();
					System.err.println("Test [FAILED]");
					Assert.fail(ae.getCause().getMessage());
				}

			}
		}

	}

	protected static void given(final String string) {
		System.out.println("\tGiven " + string);
	}

	protected static void when(final String string) {
		System.out.println("\t> When " + string);
	}

	protected static void when(final String string, final AsyncContext object) {
		System.out.println("\t> When " + string);
		Locker locker = new Locker();
		locker.lock();
		object.run(locker);
		locker.waitEnd();
	}

	protected static void then(final String string, final AsyncContext object) {
		Locker locker = new Locker();
		locker.lock();
		object.run(locker);
		System.out.println("\t=> " + string);

		locker.waitEnd();
	}

	protected static void then(final String string, final Context object) {
		object.run();
		System.out.println("\t=> " + string);

	}

}
