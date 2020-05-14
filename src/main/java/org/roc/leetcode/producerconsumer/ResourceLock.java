package org.roc.leetcode.producerconsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceLock {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private int number = 0;

    public static void main(String[] args) throws Exception {
        ResourceLock resource = new ResourceLock();
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 100; i++) {
            es.submit(new IncreaseThread(resource));
            es.submit(new DecreaseThread(resource));
        }
    }

    public int getNumber() {
        return this.number;
    }

    public void increase() {
        lock.lock();
        try {
            while (number == 1) {
                condition.await();
            }
            number++;
            System.err.println(number + "-" + Thread.currentThread().getId());
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void decrease() {
        lock.lock();
        try {
            while (number == 0) {
                condition.await();
            }
            number--;
            System.err.println(number + "-" + Thread.currentThread().getId());
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    static class IncreaseThread implements Runnable {

        private ResourceLock resource;

        public IncreaseThread(ResourceLock resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (; ; ) {
                resource.increase();
            }
        }
    }

    static class DecreaseThread implements Runnable {

        private ResourceLock resource;

        public DecreaseThread(ResourceLock resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (; ; ) {
                resource.decrease();
            }
        }
    }

}