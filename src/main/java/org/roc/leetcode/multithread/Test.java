package org.roc.leetcode.multithread;

public class Test {

    public volatile int inc = 0;

    public static void main(String[] args) {
        final Test test = new Test();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    test.increase();
                }
            }).start();
        }

        //保证前面的线程都执行完
        while (Thread.activeCount() > 1) {

            Thread.yield();
        }
        System.out.println(test.inc);
    }

    public void increase() {
        inc++;
    }
}