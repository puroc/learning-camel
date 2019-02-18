package com.example.netty.gateway.processor;

import com.example.netty.gateway.Counter;
import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;

public class CountProcessor implements AsyncProcessor {

    @Override
    public boolean process(Exchange exchange, AsyncCallback callback) {
        Counter.getInstance().add();
        callback.done(false);
        return false;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
    }
}
