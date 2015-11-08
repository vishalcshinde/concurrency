package com.vishal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class SemahporeTest {

	Semaphore producerSemaphore = new Semaphore(1);
	Semaphore consumerSemaphore = new Semaphore(1);
	AtomicInteger count = new AtomicInteger(0);

	@Test
	public void testProducerConsumer() throws Exception {
		Runnable t1 = () -> {
			try {
				for (int i = 0; i < 100; i++) {
					System.out.println("Producer -> " + count);
					while (count.get() >= 2) {
						consumerSemaphore.acquire();
					}
					work();
					add();
					producerSemaphore.release();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
		Runnable t2 = () -> {
			try {
				for (int i = 0; i < 100; i++) {

					System.out.println("Consumer -> " + count);
					while (count.get() <= 0) {
						producerSemaphore.acquire();
					}
					work();
					remove();
					consumerSemaphore.release();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		ExecutorService s = Executors.newFixedThreadPool(2);
		Future<?> f = s.submit(t1);
		Future<?> f2 = s.submit(t2);
		f.get();
		f2.get();
		System.out.println("End -> " + count);
	}

	private void remove() {
		count.getAndDecrement();
	}

	private void add() {
		count.getAndIncrement();
	}

	private void work() throws InterruptedException {
		Thread.sleep(100);
	}

}
