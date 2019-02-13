/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.netty.gateway.route;

import com.example.netty.gateway.Counter;
import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.concurrent.RejectableThreadPoolExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
//@Component
public class JavaRouter extends RouteBuilder {

    private ExecutorService executorService = createTestExecutorService(ThreadPoolRejectedPolicy.Abort.asRejectedExecutionHandler());

//    private ExecutorService createTestExecutorService(final RejectedExecutionHandler rejectedExecutionHandler) {
//        return new RejectableThreadPoolExecutor(20, 40, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10000), rejectedExecutionHandler);
//    }

    private ExecutorService createTestExecutorService(final RejectedExecutionHandler rejectedExecutionHandler) {
        return new RejectableThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1), rejectedExecutionHandler);
    }

    @Override
    public void configure() throws Exception {

//        this.getContext().setMessageHistory(false);

//        errorHandler(new NoErrorHandlerBuilder());

//        errorHandler(defaultErrorHandler().logRetryAttempted(false).logStackTrace(false).logExhausted(false));
        errorHandler(defaultErrorHandler());

        onException(Throwable.class).handled(true).maximumRedeliveries(0).process(new AsyncProcessor() {
            @Override
            public boolean process(Exchange exchange, AsyncCallback callback) {
                Counter.getInstance().add3();
                System.out.println("xixi");
                callback.done(false);
                return false;
            }

            @Override
            public void process(Exchange exchange) throws Exception {

            }
        });

        from("netty4:tcp://0.0.0.0:{{netty.port}}?sync=false&reuseAddress=true&serverInitializerFactory=#serverPipelineFactory")

                .process(new AsyncProcessor() {


                    @Override
                    public boolean process(Exchange exchange, AsyncCallback callback) {

                        Counter.getInstance().add();
                        //throolpool.execute();
                        callback.done(false);
                        return false;
                    }

                    @Override
                    public void process(Exchange exchange) throws Exception {
                    }
                })

                .threads()
                .executorService(executorService)
                .callerRunsWhenRejected(false)
                .process(new AsyncProcessor() {
                             @Override
                             public void process(Exchange exchange) throws Exception {

                             }

                             @Override
                             public boolean process(Exchange exchange, AsyncCallback callback) {
                                 try {
                                     Thread.sleep(999999);
                                 } catch (InterruptedException e) {
                                     e.printStackTrace();
                                 }

                                 Counter.getInstance().add2();
                                 callback.done(false);
                                 return false;
                             }
                         }
                );

    }

}
