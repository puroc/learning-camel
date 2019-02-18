package com.example.netty.gateway.processor;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;

public class SleepProcessor implements AsyncProcessor {

    @Override
    public boolean process(Exchange exchange, AsyncCallback callback) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callback.done(false);
        return false;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("2");
    }
}
