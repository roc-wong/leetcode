package org.roc.leetcode.multithread;

import java.util.concurrent.Semaphore;

/**
 * @author roc
 * @since 2020/5/13 17:51
 */
public class EvenOddTest1 {

    private int num;

    private Semaphore oddSemaphore = new Semaphore(1);
    private Semaphore evenSemaphore = new Semaphore(0);

    public EvenOddTest1(int num) {
        this.num = num;
    }

    public static void main(String[] args) throws InterruptedException {
        EvenOddTest1 evenOddTest = new EvenOddTest1(10);
        new Thread(() -> {
            evenOddTest.odd();
        }, "Odd-Thread").start();
        Thread.sleep(1000);
        new Thread(() -> {
            evenOddTest.even();
        }, "Even-Thread").start();
    }


    private void even() {
        for (int i = 2; i <= num; i += 2) {
            try {
                evenSemaphore.acquire(1);
                System.out.println(i);
                oddSemaphore.release(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void odd() {
        for (int i = 1; i <= num; i += 2) {
            try {
                oddSemaphore.acquire(1);
                System.out.println(i);
                evenSemaphore.release(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
