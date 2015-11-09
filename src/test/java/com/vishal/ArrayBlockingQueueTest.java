package com.vishal;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

public class ArrayBlockingQueueTest {

	ArrayBlockingQueue<Object> producerQueue = new ArrayBlockingQueue<>(1);
	ArrayBlockingQueue<Object> consumerQueue = new ArrayBlockingQueue<>(1);
	int counter = 0;
	@Test
	public void testProducerConsumer() throws Exception {
		Runnable t1 = () -> {
			try {
				for (int i = 0; i < 100; i++) {
					System.out.println("Producer -> " + counter);
					consumerQueue.take();
					work(100);
					add();
					producerQueue.offer("");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
		Runnable t2 = () -> {
			try {
				for (int i = 0; i < 100; i++) {

					System.out.println("Consumer -> " + counter);
					producerQueue.take();
					work(100);
					remove();
					consumerQueue.offer("");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		consumerQueue.put("");
		ExecutorService s = Executors.newFixedThreadPool(2);
		Future<?> f = s.submit(t1);
		Future<?> f2 = s.submit(t2);
		f.get();
		f2.get();
		System.out.println("End -> " + counter);
	}

	private void remove() {
		--counter;
	}

	private void add() {
		++counter;
	}

	private void work(int time) throws InterruptedException {
		Thread.sleep(time);
	}

}
