package org.roc.leetcode.multithread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;


/**
 * 假设有这么一个类：
 *
 * class ZeroEvenOdd {   public ZeroEvenOdd(int n) { ... }      // 构造函数 public void zero(printNumber) { ... }  // 仅打印出 0
 * public void even(printNumber) { ... }  // 仅打印出 偶数 public void odd(printNumber) { ... }   // 仅打印出 奇数 }
 * 相同的一个 ZeroEvenOdd 类实例将会传递给三个不同的线程：
 *
 * 线程 A 将调用 zero()，它只输出 0 。 线程 B 将调用 even()，它只输出偶数。 线程 C 将调用 odd()，它只输出奇数。 每个线程都有一个 printNumber
 * 方法来输出一个整数。请修改给出的代码以输出整数序列 010203040506... ，其中序列的长度必须为 2n。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：n = 2 输出："0102" 说明：三条线程异步执行，其中一个调用 zero()，另一个线程调用 even()，最后一个线程调用odd()。正确的输出为 "0102"。 示例 2：
 *
 * 输入：n = 5 输出："0102030405"
 *
 * 来源：力扣（LeetCode） 链接：https://leetcode-cn.com/problems/print-zero-even-odd 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author roc
 * @date 2020/5/13 10:05
 */
public class ZeroEvenOddTest {

    private int num;

    private int count;
    private boolean flag = false; //是否零阻塞

    private ReentrantLock reentrantLock = new ReentrantLock();
    private Condition zeroCondition = reentrantLock.newCondition();
    private Condition oddCondition = reentrantLock.newCondition();
    private Condition evenCondition = reentrantLock.newCondition();


    public ZeroEvenOddTest(int num) {
        this.num = num;
    }

    public static void main(String[] args) {
        int n = 9;
        ZeroEvenOddTest zeroEvenOddTest = new ZeroEvenOddTest(n);

        new Thread(() -> {
            try {
                zeroEvenOddTest.zero(System.out::print);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "Zero-Thread").start();
        new Thread(() -> {
            try {
                zeroEvenOddTest.odd(System.out::println);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Odd-Thread").start();
        new Thread(() -> {
            try {
                zeroEvenOddTest.even(System.out::println);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Even-Thread").start();

    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {

        try {
            reentrantLock.lock();

            for (int i = 1; i <= num; i++) {

                while (flag) {
                    zeroCondition.await();
                }

                printNumber.accept(0);
                count++;
                flag = !flag;

                if (i % 2 == 0) {
                    evenCondition.signal();
                } else {
                    oddCondition.signal();
                }
            }
        } finally {
            reentrantLock.unlock();
            System.out.println("解锁zero");
        }

    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        try {
            reentrantLock.lock();

            for (; count < num; ) {
                if (num == 1) {
                    break;
                }
                if (num > 1) {
                    if (num % 2 != 0 && count == num - 1) {
                        System.out.println("最后一位是奇数，even-thread 终止");
                        break;
                    }
                }

                while (!flag || count % 2 != 0){
                    evenCondition.await();
                }
                printNumber.accept(count);
                flag = !flag;
                zeroCondition.signalAll();
            }
        } finally {
            reentrantLock.unlock();
            System.out.println("解锁偶数");
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        try {
            reentrantLock.lock();

            if (count == 1 && num == 1) {
                printNumber.accept(1);
            }

            for (; count < num; ) {
                if (num > 2) {
                    //循环终止
                    if (num % 2 == 0 && count == num - 1) {
                        System.out.println("最后一位是偶数，old-thread 终止");
                        break;
                    }
                }
                while (!flag || count % 2 == 0) {
                    oddCondition.await();
                }
                printNumber.accept(count);
                flag = !flag;
                zeroCondition.signalAll();
                if (num == 2) {
                    break;
                }
            }
        } finally {
            reentrantLock.unlock();
            System.out.println("解锁奇数");
        }
    }

}