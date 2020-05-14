package org.roc.leetcode.multithread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

public class ZeroEvenOdd {

    Lock lock = new ReentrantLock();
    private int n;
    private boolean flag = false; //是否零阻塞
    private int count = 0;
    private Condition zero = lock.newCondition();
    private Condition even = lock.newCondition();
    private Condition odd = lock.newCondition();


    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    public static void main(String[] args) {

        ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(10);
        new Thread(() -> {
            try {
                zeroEvenOdd.zero((x) -> {
                    System.out.println(x + " " + Thread.currentThread().getName());
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "zero-thread").start();
        new Thread(() -> {
            try {
                zeroEvenOdd.even((x) -> {
                    System.out.println(x + " " + Thread.currentThread().getName());
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "even-thread").start();
        new Thread(() -> {
            try {
                zeroEvenOdd.odd((x) -> {
                    System.out.println(x + " " + Thread.currentThread().getName());
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "odd-thread").start();


    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try {
            for (int i = 1; i <= n; i++) {
                while (flag) {
                    zero.await();
                }
                printNumber.accept(0);
                count++;
                flag = !flag;
                if (i % 2 == 0) {
                    even.signalAll();//偶数
                } else {
                    odd.signalAll();//奇数
                }

            }
            System.out.println("0解锁");
        } finally {

            lock.unlock();

        }

    }

    /**
     * 偶数
     */
    public void even(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try {
            for (; count < n; ) {
                if (n == 1) {
                    break;
                }
                if (n > 1) {
                    if (n % 2 != 0 && count == n - 1) {
                        break;
                    }
                }
                while (!flag || count % 2 != 0) {
                    even.await();
                }

                printNumber.accept(count);
                flag = !flag;
                zero.signalAll();

            }
        } finally {

            lock.unlock();
            System.out.println("解锁偶数");

        }

    }

    /**
     * 奇数
     */
    public void odd(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try {

            if (count == 1 && n == 1) {
                printNumber.accept(count);

            }
            for (; count < n; ) {
                if (n > 2) {
                    if (n % 2 == 0 && count == n - 1) {
                        break;
                    }
                }
                while (!flag || count % 2 == 0) {
                    odd.await();
                }
                printNumber.accept(count);
                flag = !flag;
                zero.signalAll();
                if (n == 2) {
                    break;
                }
            }

        } finally {
            lock.unlock();
            System.out.println("解锁奇数");
        }
    }
}