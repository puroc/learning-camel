package com.example.netty.gateway.netty;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.netty4.NettyConsumer;
import org.apache.camel.component.netty4.handlers.ServerChannelHandler;
import com.example.netty.gateway.Counter;

import java.net.SocketAddress;

public class TestHandler extends ServerChannelHandler {

    public TestHandler(NettyConsumer consumer) {
        super(consumer);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead0(ctx, msg);
        Counter.getInstance().addReadNum();
    }

    @Override
    protected void beforeProcess(Exchange exchange, ChannelHandlerContext ctx, Object message) {
        super.beforeProcess(exchange, ctx, message);
    }

    @Override
    protected Object getResponseBody(Exchange exchange) throws Exception {
        return super.getResponseBody(exchange);
    }

    @Override
    protected ChannelFutureListener createResponseFutureListener(NettyConsumer consumer, Exchange exchange, SocketAddress remoteAddress) {
        return super.createResponseFutureListener(consumer, exchange, remoteAddress);
    }
}
