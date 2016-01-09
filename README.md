# bluprint

Simple and tiny [Behavior driven development](https://fr.wikipedia.org/wiki/Behavior_driven_development) framework for testing Java code.

## The story behind the code
After years of using [JBehave](http://jbehave.org/) writing *story* files and  *step* classes to execute them on large scale projects, I struggled to maintain all of the stories and associated steps. I sometimes even spent more time fixing and factorizing *steps* instead of building value features into my apps. Don't get me wrong, I really enjoyed the [JBehave](http://jbehave.org/) framework.

But last year I mainly worked on Javascript projects with [Jasmine](http://jasmine.github.io/), [Karma](http://karma-runner.github.io/0.13/index.html) and [Mocha](https://mochajs.org/#features) and  I really enjoyed writing tests in the "*describe*, *it*" way. I also found it more productive to have both the textual description and the executing code in the same place.

I tried to reproduce this way of writing test in Java when I worked on Java applications. **Then Bluprint was born.**

Bluprint is built with 3 primary goals :
- Improve Test readability
- Simplicity of use
- Keep the library as small as possible and rely only on JUnit for the core library

## How to use it

### Simple test case

1. Create a test class and extend the *Bluprint* class
2. create a method and annotate it with @Scenario
3. Add given, then, when steps

   Here's the final test case

   ```Java
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
   }
   ```
4. Run as a standard JUnit test. Here's the standard output :

   ```
   Scenario : Test Addition
   id : 123
   Description:
   	As a user of the demo api
   	I want to Perform an addition

   Execution:
   	 Given A = 5
   	 And B = 5
   	 > When A + B
   	 => Then it should return 10
   Success!
   ```

### Asynchronous tests

Asynchronous operations and assertions are supported by *when* and *then* steps.

Implementation is inspired from Javascript BDD frameworks such as [Mocha](https://mochajs.org/#asynchronous-code) and uses [Java 8 functional Interface](http://radar.oreilly.com/2014/08/java-8-functional-interfaces.html).

1. write the steps as usual
2. add *async* to the lambda parameter
3. call *async.done()* when the asynchronous task has finished

By adding the **async** parameter, Bluprint will know that it has to wait for completion (*async.done()*) before executing next step.

```
   when("async action", (async) -> {

      // Perforn async stuffs

      // Tells Bluprint that async stuffs are completed and continue to next step
      async.done();

   });

```

Here's a sample

```Java
public class AsynchronousTestDemo extends Bluprint{

   @Scenario("Asynchronous scenario")
	public void asyncScenario() {

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

					// Mark the async task as complete
					async.done();
				}
			});

		});

		then("its execution time should be 3s +/- 200ms", () -> {
			long deltaT = System.currentTimeMillis() - startDate;
			Assert.assertEquals(3000, deltaT, 200);
		});

	}

}

```
