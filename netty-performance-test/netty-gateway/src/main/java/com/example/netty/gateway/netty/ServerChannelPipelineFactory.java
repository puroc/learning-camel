package com.example.netty.gateway.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.camel.component.netty4.NettyConsumer;
import org.apache.camel.component.netty4.ServerInitializerFactory;

public class ServerChannelPipelineFactory extends ServerInitializerFactory {

    private NettyConsumer consumer;

    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast("encoder-SD", new Encoder());
//        channelPipeline.addLast("decoder-SD", new StringDecoder());
        channelPipeline.addLast("decoder-SD", new LengthFieldBasedFrameDecoder(1024,0,4,-4,0));
//        channelPipeline.addLast("decoder-SD", new LineBasedFrameDecoder(1024));
//        channelPipeline.addLast("handler", new ChannelHandler(consumer));
        channelPipeline.addLast("handler", new TestHandler(consumer));

    }

    @Override
    public ServerInitializerFactory createPipelineFactory(NettyConsumer consumer) {
        this.consumer = consumer;
        return this;
    }

}
