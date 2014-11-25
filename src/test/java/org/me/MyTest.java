package org.me;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyTest {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(2);
		CompletableFuture.supplyAsync(
				() -> {
					log("Running");
					log("Start sleeping");
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					log("Woke up");
					return "42";
				},
				exec
		).thenApplyAsync(
				s -> {
					log("Running");
					log("Start sleeping");
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					log("Woke up");
					return 42.0;
				}
		).thenApply(
				s -> {
					log("Running");
					log("Got value:" + s);
					log("Start sleeping...");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return 42;
				}
		).thenRun(
				exec::shutdown
		);
	}

	private static void log(String str) {
		System.out.println("ThreadName:" + Thread.currentThread().getName() + " ThreadId:" + Thread.currentThread().getId() + " " + str);
	}
}
