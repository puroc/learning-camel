package com.example.netty.gateway.processor;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionProcessor implements AsyncProcessor {

    private Logger log = LoggerFactory.getLogger(ExceptionProcessor.class);

    @Override
    public boolean process(Exchange exchange, AsyncCallback callback) {
        exchange.getProperty("CamelExceptionCaught");
        Throwable exception = (Throwable)exchange.getProperty("CamelExceptionCaught", Throwable.class);
        log.error("get a exception when process message whose id:"+exchange.getIn().getMessageId(),exception);
        callback.done(false);
        return false;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

    }
}
