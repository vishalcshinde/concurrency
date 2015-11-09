package com.vishal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class ReentrantLockTest {
	final Lock lock = new ReentrantLock();
	final Condition producerCondition = lock.newCondition();
	final Condition consumerCondition = lock.newCondition();
	int count = 0;
	final int producerId = 1;
	final int consumerId = 2;
	int currentId = producerId;

	@Test
	public void testProducerConsumer() throws Exception {
		Runnable producerThread = () -> {
			try {
				for (int i = 0; i < 100; i++) {
					System.out.println("Producer -> " + count);
					lock.lock();
					while(currentId != producerId) {
						producerCondition.await();
					}
					lock.unlock();
					work();
					add();
					lock.lock();
					currentId = consumerId;
					consumerCondition.signalAll();
					lock.unlock();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
		Runnable consumerThread = () -> {
			try {
				for (int i = 0; i < 100; i++) {
					System.out.println("Consumer -> " + count);
					lock.lock();
					while(currentId != consumerId) {
						consumerCondition.await();
					}
					lock.unlock();
					work();
					remove();
					lock.lock();
					currentId = producerId;
					producerCondition.signalAll();
					lock.unlock();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		
		ExecutorService s = Executors.newFixedThreadPool(2);
		Future<?> f = s.submit(producerThread);
		Future<?> f2 = s.submit(consumerThread);
		f.get();
		f2.get();
		System.out.println("End -> " + count);
	}

	private void remove() {
		--count;
	}

	private void add() {
		++count;
	}

	private void work() throws InterruptedException {
		Thread.sleep(100);
	}

}
