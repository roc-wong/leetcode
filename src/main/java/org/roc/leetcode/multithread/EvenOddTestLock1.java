package org.roc.leetcode.multithread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author roc
 * @date 2020/05/13
 */
public class EvenOddTestLock1 {

    private int num;
    private ReentrantLock reentrantLock = new ReentrantLock();
    private Condition condition = reentrantLock.newCondition();


    public EvenOddTestLock1(int num) {
        this.num = num;
    }

    public static void main(String[] args) {
        EvenOddTestLock1 evenOddTest = new EvenOddTestLock1(9);

        new Thread(() -> {
            try {
                evenOddTest.printOdd();
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
                evenOddTest.printEven();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Even-Thread").start();
    }


    private void printEven() throws InterruptedException {

        try {
            reentrantLock.lock();
            for (int i = 1; i <= num; i++) {
                System.out.println(Thread.currentThread().getName() + "->" + i +
                        "start");
                if ((i & 1) == 1) {
                    condition.signal();
                } else {
                    System.out.println(i + ", " + Thread.currentThread().getName());
                    condition.await();
                }
            }
            System.out.println("even stop");
        } finally {
            if(reentrantLock.isHeldByCurrentThread()){
                reentrantLock.unlock();
            }
        }

    }

    private void printOdd() throws InterruptedException {

        try {
            reentrantLock.lock();
            for (int i = 1; i <= num; i++) {
                System.out.println(Thread.currentThread().getName() + "->" + i +
                        "start");
                if ((i & 1) == 0) {
                    condition.signal();
                } else {
                    System.out.println(i + ", " + Thread.currentThread().getName());
                    condition.await();
                }
            }
            System.out.println("odd stop");
        } finally {
            if(reentrantLock.isHeldByCurrentThread()){
                reentrantLock.unlock();
            }
        }
    }
}
