package org.roc.leetcode.multithread;

import java.util.concurrent.ArrayBlockingQueue;

public class H2O1 {

    private Object lock = new Object();

    private ArrayBlockingQueue<Integer> HblockQueue = new ArrayBlockingQueue<>(2);
    private ArrayBlockingQueue<Integer> OblockQueue = new ArrayBlockingQueue<>(1);


    public H2O1() {
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        synchronized (lock) {
            while (HblockQueue.size() == 2) {
                lock.wait();
            }
            HblockQueue.add(1);
            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();
            lock.notifyAll();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        synchronized (lock) {
            while (OblockQueue.size() == 1 || HblockQueue.size() != 2) {
                lock.wait();
            }
            // releaseOxygen.run() outputs "H". Do not change or remove this line.
            releaseOxygen.run();
            lock.notifyAll();
            OblockQueue.clear();
            HblockQueue.clear();
        }
    }
}  