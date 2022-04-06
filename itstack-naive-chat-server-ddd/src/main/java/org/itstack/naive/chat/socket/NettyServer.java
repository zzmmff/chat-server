package org.itstack.naive.chat.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.itstack.naive.chat.application.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

/**
 */
@Service("nettyServer")
public class NettyServer implements Callable<Channel> {

    //实现callable接口

    private Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Resource
    private UserService userService;


    //两个线程组
    /**
     *  parentGroup bossGroup 用于监听用于监听客户端连接，
     *  专门负责与客户端创建连接，并把连接注册到workerGroup的Selector中。
     *
     *  childGroup workerGroup用于处理每一个连接发生的读写事件。
     *
     * */

    private final EventLoopGroup parentGroup = new NioEventLoopGroup(2);
    private final EventLoopGroup childGroup = new NioEventLoopGroup();
    private Channel channel;

    //注入
    public NettyServer(UserService userService){
        this.userService = userService;
    }

    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup)
                    //服务端通道实现类型
                    //NioServerSocketChannel 异步非阻塞服务端

                    /**
                     * NioSocketChannel： 异步非阻塞的客户端 TCP Socket 连接。
                     *
                     * NioServerSocketChannel： 异步非阻塞的服务器端 TCP Socket 连接。
                     *
                     * 常用的就是这两个通道类型，因为是异步非阻塞的。所以是首选。
                     * OioSocketChannel： 同步阻塞的客户端 TCP Socket 连接。
                     *
                     * OioServerSocketChannel： 同步阻塞的服务器端 TCP Socket 连接。
                     * */
                    .channel(NioServerSocketChannel.class)
                    //设置的是服务端用于接收进来的连接
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //初始化通道对象
                    .childHandler(new MyChannelInitializer(userService));

            //绑定端口号
            channelFuture = b.bind(new InetSocketAddress(7397)).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            logger.error("socket server start error", e.getMessage());
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                logger.info("socket server start done. ");
            } else {
                logger.error("socket server start error. ");
            }
        }
        return channel;
    }

    public void destroy() {
        if (null == channel) return;
        channel.close();
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();
    }

    public Channel channel() {
        return channel;
    }

}
