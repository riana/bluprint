package bluprint.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncTaskDemo {

	public static interface CallBack {

		public void taskComplete(String data);

	}

	public void start(final int timeSeconds, final CallBack callBack) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.schedule(new Runnable() {

			@Override
			public void run() {
				callBack.taskComplete("Complete");

			}
		}, timeSeconds, TimeUnit.SECONDS);
	}

}
