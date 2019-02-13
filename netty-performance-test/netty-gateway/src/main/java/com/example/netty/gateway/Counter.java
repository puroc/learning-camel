package com.example.netty.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class Counter {

    private static Logger log = LoggerFactory.getLogger(Counter.class);

    private static final Counter COUNTER = new Counter();

    private Counter() {

    }

    public static Counter getInstance() {
        return COUNTER;
    }

    private AtomicLong num = new AtomicLong();
    private AtomicLong num2 = new AtomicLong();
    private AtomicLong num3 = new AtomicLong();
    private AtomicLong readNum = new AtomicLong();

    private Timer timer = new Timer();

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.error("num:" + num.get()+",num2:" + num2.get()+",num3:" + num3.get()+",readNum:"+readNum.get());
                num.set(0);
                num2.set(0);
                num3.set(0);
                readNum.set(0);
            }
        }, 1000, 1000);
    }

    public void add() {
        num.incrementAndGet();
    }

    public void add2() {
        num2.incrementAndGet();
    }

    public void add3() {
        num3.incrementAndGet();
    }

    public void addReadNum(){
        readNum.incrementAndGet();
    }


}
