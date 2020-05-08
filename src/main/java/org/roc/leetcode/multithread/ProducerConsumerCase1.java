package org.roc.leetcode.multithread;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author roc
 * @date 2020/05/08
 */
public class ProducerConsumerCase1 {

    public static void main(String[] args) throws InterruptedException {

        PublicQueue1<Integer> publicQueue = new PublicQueue1<>();

        Thread producerThread = new Thread(() -> {

            for (int i = 1; i <= 100; i++) {
                publicQueue.put(i);
            }
        }, "Producer");

        Thread consumerThread = new Thread(() -> {
            int sum = 0;
            while (true) {
                sum += publicQueue.get();
                System.out.println("current is " + sum);
            }
        }, "Consumer");

        producerThread.start();

        Thread.sleep(1000);
        consumerThread.start();
    }

}


class PublicQueue1<T> {

    private int maxBufferSize = 50;
    private AtomicLong putIndex = new AtomicLong(0);
    private ReentrantLock reentrantLock = new ReentrantLock();
    private Condition putCondition = reentrantLock.newCondition();
    private Condition getCondition = reentrantLock.newCondition();

    public PublicQueue1() {
        new PublicQueue1(maxBufferSize);

    }

    public PublicQueue1(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    private LinkedHashMap<Long, T> linkedHashMap = new LinkedHashMap<>();

    public void put(T message) {

        try {
            reentrantLock.lock();

            if (linkedHashMap.size() >= maxBufferSize) {
                try {
                    putCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                getCondition.signalAll();
            }
            linkedHashMap.put(putIndex.incrementAndGet(), message);
        } finally {
            reentrantLock.unlock();
        }
    }

    public synchronized T get() {
        try {
            reentrantLock.lock();
            if (linkedHashMap.size() == 0) {
                try {
                    getCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                putCondition.signalAll();
            }
            Map.Entry<Long, T> next = linkedHashMap.entrySet().iterator().next();
            T message = next.getValue();
            linkedHashMap.remove(next.getKey());
            return message;
        } finally {
            reentrantLock.unlock();
        }
    }

}
