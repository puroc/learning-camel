package com.example.netty.gateway.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.component.netty4.NettyConsumer;
import org.apache.camel.component.netty4.handlers.ServerChannelHandler;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.camel.component.netty4.NettyConstants.NETTY_CHANNEL_HANDLER_CONTEXT;

/**
 * Netty handler
 *
 * @author <a href="mailto:zhaol@emrubik.com">zhao lei</a>
 * @version $Revision 1.0 $ 2017年9月5日 下午4:47:14
 */
public class ChannelHandler extends ServerChannelHandler {

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 40, 5000, TimeUnit.SECONDS, new LinkedBlockingDeque(10000), new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//            log.info("reject msg !!!");
        }
    });

    /**
     * Camel consumer
     */
    private NettyConsumer consumer;

    /**
     * Create a new instance ChannelHandler
     *
     * @param consumer Netty consumer
     */
    public ChannelHandler(NettyConsumer consumer) {
        super(consumer);
        this.consumer = consumer;
    }

    /**
     * channelActive 建立连接
     *
     * @param ctx
     * @throws Exception TODO 请在每个@param,@return,@throws行尾补充对应的简要说明
     * @see ServerChannelHandler#channelActive(ChannelHandlerContext)
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            Exchange exchange = consumer.getEndpoint().createExchange();
            exchange.getIn().setHeader(Constant.SESSION_EVENT, Constant.MESSAGE_RECEIVED);
            exchange.getIn().setHeader(NETTY_CHANNEL_HANDLER_CONTEXT, ctx);
//            ByteBuf byteBuf = (ByteBuf) msg;
//            byte[] bytes =new byte[byteBuf.capacity()];
//            byteBuf.getBytes(0,bytes);
//            ReferenceCountUtil.release(byteBuf);
//            exchange.getIn().setBody(new String(bytes));
            exchange.getIn().setBody((String)msg);

//        byte[] bs = ReplyProcesser.replyMsg("1899001011111486");
//        ctx.writeAndFlush(bs);


//        threadPoolExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
            //使用同步process方法，即使后面使用线程池，计数永远是1，why？
//		consumer.getProcessor().process(exchange);

            consumer.getAsyncProcessor().process(exchange, new AsyncCallback() {
                @Override
                public void done(boolean doneSync) {
//                    System.out.println("doneSync:" + doneSync);
                }
            });

//        System.out.println("haha");

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
        }


    }

    /**
     * channelInactive 断开连接
     *
     * @param ctx
     * @throws Exception TODO 请在每个@param,@return,@throws行尾补充对应的简要说明
     * @see ServerChannelHandler#channelInactive(ChannelHandlerContext)
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Exchange exchange = consumer.getEndpoint().createExchange();
        exchange.getIn().setHeader(Constant.SESSION_EVENT, Constant.SESSION_CLOSED);
        exchange.getIn().setHeader(NETTY_CHANNEL_HANDLER_CONTEXT, ctx);
        consumer.getProcessor().process(exchange);
    }

    /**
     * exceptionCaught 连接异常
     *
     * @param ctx
     * @param cause
     * @throws Exception TODO 请在每个@param,@return,@throws行尾补充对应的简要说明
     * @see ServerChannelHandler#exceptionCaught(ChannelHandlerContext,
     * Throwable)
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Exchange exchange = consumer.getEndpoint().createExchange();
        exchange.getIn().setHeader(Constant.SESSION_EVENT, Constant.SESSION_EXCEPTION);
        exchange.getIn().setHeader(NETTY_CHANNEL_HANDLER_CONTEXT, ctx);
        consumer.getProcessor().process(exchange);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                ctx.close();
            }
        }
    }

}
