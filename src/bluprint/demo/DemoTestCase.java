package bluprint.demo;

import org.junit.Assert;

import bluprint.Bluprint;
import bluprint.Goal;
import bluprint.Id;
import bluprint.PrimaryActor;
import bluprint.Scenario;
import bluprint.demo.AsyncTaskDemo.CallBack;

public class DemoTestCase extends Bluprint {

	@Scenario("Test Addition")
	@Id("123")
	@PrimaryActor("user of the demo api")
	@Goal("Perform an addition")
	public void simpleTest() {
		given("A = 5");
		int a = 5;

		given("B = 5");
		int b = 5;

		when("A + B");

		int c = a + b;

		then("it should return 10", () -> {
			Assert.assertEquals(10, c);
		});
	}

	@Scenario("Asynchronous scenario")
	public void test2() {
		given("an asynchronous task");

		long startDate = System.currentTimeMillis();
		AsyncTaskDemo asyncTaskDemo = new AsyncTaskDemo();

		// Waits for async.done() to be invoked before continuing
		when("started for 3 seconds", (async) -> {

			// Running Asynchronous Task
			asyncTaskDemo.start(3, new CallBack() {

				@Override
				public void taskComplete(final String data) {

					then("it should receive data inside the callback",
								() -> {
									Assert.assertNotNull(data);
								});

					// Mark the task as complete
					async.done();

				}
			});

		});

		then("its execution time should be > 3s", () -> {
			long deltaT = System.currentTimeMillis() - startDate;
			Assert.assertEquals(3000, deltaT, 200);
		});

	}
}
