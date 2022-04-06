package org.itstack.naive.chat;

import io.netty.channel.Channel;
import org.itstack.naive.chat.application.UserService;
import org.itstack.naive.chat.socket.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 启动类
 */
@SpringBootApplication
@Configuration
@ImportResource(locations = {"classpath:spring-config.xml"})
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(Application.class);

    @Resource
    private NettyServer nettyServer;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("NettyServer启动服务开始 port：7397");
        //需要返回值，使用submit方法
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(nettyServer);
        Channel channel = future.get();
        if (null == channel) throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            logger.info("NettyServer启动服务 ...");
            Thread.sleep(500);
        }

        logger.info("NettyServer启动服务完成 {}", channel.localAddress());
    }

}
