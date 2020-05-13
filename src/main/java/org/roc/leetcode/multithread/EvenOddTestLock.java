package org.roc.leetcode.multithread;

/**
 * @author roc
 * @date 2020/05/13
 */
public class EvenOddTestLock {

    private int num;
    private Object lock = new Object();


    public EvenOddTestLock(int num) {
        this.num = num;
    }

    public static void main(String[] args) {
        EvenOddTestLock evenOddTestLock = new EvenOddTestLock(9);

        new Thread(() -> {
            try {
                evenOddTestLock.printOdd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Odd-Thread").start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                evenOddTestLock.printEven();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Even-Thread").start();
    }


    private void printEven() throws InterruptedException {

        synchronized (lock) {
            for (int i = 1; i <= num; i++) {
                if ((i & 1) == 1) {
                    lock.notify();
                } else {
                    System.out.println(i + ", " + Thread.currentThread().getName());
                    lock.wait();
                }
            }
        }
        System.out.println("even stop");
    }

    private void printOdd() throws InterruptedException {

        synchronized (lock) {
            for (int i = 1; i <= num; i++) {
                if ((i & 1) == 0) {
                    lock.notify();
                } else {
                    System.out.println(i + ", " + Thread.currentThread().getName());
                    lock.wait();
                }
            }
        }

        System.out.println("odd stop");
    }
}
