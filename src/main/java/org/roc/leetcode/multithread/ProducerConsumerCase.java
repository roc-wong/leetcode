package org.roc.leetcode.multithread;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author roc
 * @date 2020/05/08
 */
public class ProducerConsumerCase {

    public static void main(String[] args) throws InterruptedException {

        PublicQueue<Integer> publicQueue = new PublicQueue<>();

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


class PublicQueue<T> {

    private int maxBufferSize = 50;
    private AtomicLong putIndex = new AtomicLong(0);

    private LinkedHashMap<Long, T> linkedHashMap = new LinkedHashMap<>();

    public synchronized void put(T message) {
        if (linkedHashMap.size() >= maxBufferSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            notifyAll();
        }
        linkedHashMap.put(putIndex.incrementAndGet(), message);
    }

    public synchronized T get() {
        if (linkedHashMap.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            notifyAll();
        }
        Map.Entry<Long, T> next = linkedHashMap.entrySet().iterator().next();
        T message = next.getValue();
        linkedHashMap.remove(next.getKey());
        return message;
    }

}
