package org.roc.leetcode.multithread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Solution{
    static volatile Map<Integer, Integer> map = new ConcurrentHashMap<>();
    static volatile boolean finishSingal = false;
    public static void main(String[] args) throws InterruptedException {

    	Thread producerThread = new Thread(() -> {

                for(int i=1; i<=100; i++){
                    map.put(i, i);
                }
                finishSingal = true;

        }, "Producer");

        Thread consumerThread = new Thread(() -> {
                int sum = 0;
                while(true){
                    for(Map.Entry<Integer, Integer> entry : map.entrySet()){
                        Integer key = entry.getKey();
                        sum += key;
                        map.remove(key);
                    }
                    if(finishSingal){
                    	break;
                    }
                }
            System.out.println(sum);

        }, "Consumer");
        
        producerThread.start();
        
        Thread.sleep(1000);
        consumerThread.start();
    }
}