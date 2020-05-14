package org.roc.leetcode.multithread;

import java.util.concurrent.Semaphore;

class H2O {

    Semaphore semaphoreHydrogen = new Semaphore(2);
    Semaphore semaphoreOxygen = new Semaphore(0);

    public H2O() {

    }

    public static void main(String[] args) throws InterruptedException {
        //测试用例字符串
        String test = "HOHOHHOOHOHHHHHOHHHOH";

        //生成结果字符串
        StringBuffer result = new StringBuffer();

        //注意：创建的Runnable任务，无法启动线程，必须依托其他类或线程启动
        //创建生成氧气任务
        Runnable releaseHydrogen = () -> result.append("H");

        //创建生成氧气任务
        Runnable releaseOxygen = () -> result.append("O");

        //保存线程数组
        Thread threads[] = new Thread[test.length()];

        H2O h2o = new H2O();
        for (int i = 0; i < test.length(); ++i) {
            Thread thread = null;
            //根据获得的字符调用相应的氧气或氢气线程
            if (test.charAt(i) == 'O') {
                thread = new OGenerator(h2o, releaseOxygen);
            } else if (test.charAt(i) == 'H') {
                thread = new HGenerator(h2o, releaseHydrogen);
            }
            //开始线程
            thread.start();
            //保存到线程数组
            threads[i] = thread;
        }

        //等侍所有线程执行完
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        //输出结果串
        System.out.println(result.toString());
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        semaphoreHydrogen.acquire();
        releaseHydrogen.run();
        semaphoreOxygen.release();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        semaphoreOxygen.acquire(2);
        releaseOxygen.run();
        semaphoreHydrogen.release(2);
    }
}

class HGenerator extends Thread {

    H2O h2O;
    Runnable releaseHydrogen;

    public HGenerator(H2O h2O, Runnable releaseHydrogen) {
        this.h2O = h2O;
        this.releaseHydrogen = releaseHydrogen;
    }

    @Override
    public void run() {
        try {
            h2O.hydrogen(releaseHydrogen);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class OGenerator extends Thread {

    H2O h2O;
    Runnable releaseOxygen;

    public OGenerator(H2O h2O, Runnable releaseOxygen) {
        this.h2O = h2O;
        this.releaseOxygen = releaseOxygen;
    }

    @Override
    public void run() {
        try {
            h2O.oxygen(releaseOxygen);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}