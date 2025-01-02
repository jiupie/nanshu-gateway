package com.ngbs.nanshu.gateway.comet.server;


import com.ngbs.nanshu.gateway.comet.server.ws.NanshuWebSocketBinaryHandler;
import com.ngbs.nanshu.gateway.comet.server.ws.NanshuWebSocketTextHandler;
import com.ngbs.nanshu.gateway.comet.tls.TlsHelper;
import com.ngbs.nanshu.gateway.comet.tls.TlsMode;
import com.ngbs.nanshu.gateway.comet.tls.TlsSystemConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 南顾北衫
 * @date 2024/8/8
 */
public class NettyServer {

    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);


    private final ServerBootstrap serverBootstrap;
    private final NettyServerConfig nettyServerConfig;

    protected volatile SslContext sslContext;
    protected volatile boolean started = false;
    private static final Object daemonLock = new Object();

    private EventLoopGroup boss;
    private EventLoopGroup work;
    private DefaultEventExecutorGroup defaultEventExecutorGroup;

    private NettyConnectManageHandler connectionManageHandler;

    public NettyServer(NettyServerConfig nettyServerConfig) {
        this.nettyServerConfig = nettyServerConfig;
        this.serverBootstrap = new ServerBootstrap();

        //初始化线程池
        initEventLoopGroup();

        //加载 ssl
        loadSslContext();

        loadProcess();
    }

    public String server() {
       return serverBootstrap.config().localAddress().toString();
    }

    private void loadProcess() {
        ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private final AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("login_process_%d", this.threadIndex.incrementAndGet()));
            }
        });

    }

    private void loadSslContext() {
        TlsMode tlsMode = TlsSystemConfig.tlsMode;
        log.info("server is running in TLS {} mode", tlsMode.getName());

        if (tlsMode != TlsMode.DISABLED) {
            try {
                this.sslContext = TlsHelper.buildSslContext(false);
            } catch (IOException | CertificateException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if (started) {
            return;
        }
        synchronized (daemonLock) {
            if(started){
                return;
            }
            this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(nettyServerConfig.getServerWorkerThreads(), new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "NettyServerCodecThread_" + this.threadIndex.incrementAndGet());
                }
            });

            prepareSharableHandlers();
            WebSocketServerProtocolConfig webSocketServerProtocolConfig = WebSocketServerProtocolConfig.newBuilder().handshakeTimeoutMillis(500).websocketPath("/v2").checkStartsWith(true).build();

            LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);

            this.serverBootstrap.group(this.boss, this.work)
                    .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_KEEPALIVE, false)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .localAddress(new InetSocketAddress(this.nettyServerConfig.getListenPort()))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            if (TlsSystemConfig.tlsMode != TlsMode.DISABLED) {
                                ch.pipeline().addFirst().addLast(defaultEventExecutorGroup, sslContext.newHandler(ch.alloc()));
                            }
                            ch.pipeline().addLast(defaultEventExecutorGroup,
                                    loggingHandler,
                                    new HttpServerCodec(),
                                    new HttpObjectAggregator(8192),
                                    new AuthHandler(),
                                    new WebSocketServerProtocolHandler(webSocketServerProtocolConfig), // WebSocket 握手、控制帧处理
                                    new IdleStateHandler(0, 0, nettyServerConfig.getServerChannelMaxIdleTimeSeconds()),
                                    connectionManageHandler,
                                    new NanshuWebSocketTextHandler(),
                                    new NanshuWebSocketBinaryHandler());
                        }
                    });

            try {
                Channel channel = this.serverBootstrap.bind().channel();
                log.info("Gateway Server started on port(s): {} (websocket) ", this.nettyServerConfig.getListenPort());
                channel.closeFuture().sync();
            } catch (InterruptedException e1) {
                throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
            }
        }
    }

    private void prepareSharableHandlers() {
        connectionManageHandler = new NettyConnectManageHandler();
    }

    /**
     * 初始化线程池
     * epoll或者nio
     */
    public void initEventLoopGroup() {
        //初始化线程池
        if (useEpoll()) {
            this.boss = new EpollEventLoopGroup(1, new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyEPOLLBoss_%d", this.threadIndex.incrementAndGet()));
                }
            });
            this.work = new EpollEventLoopGroup(1, new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyEPOLLWork_%d", this.threadIndex.incrementAndGet()));
                }
            });
        } else {
            this.boss = new NioEventLoopGroup(1, new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyNIOBoss_%d", this.threadIndex.incrementAndGet()));
                }
            });

            this.work = new NioEventLoopGroup(1, new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyNIOWork_%d", this.threadIndex.incrementAndGet()));
                }
            });
        }
    }

    /**
     * 是否使用epoll
     */
    private boolean useEpoll() {
        return RemotingUtil.isLinuxPlatform() && nettyServerConfig.isUseEpollNativeSelector() && Epoll.isAvailable();
    }

    public void shutdown() {
        this.boss.shutdownGracefully();

        this.work.shutdownGracefully();

        if (this.defaultEventExecutorGroup != null) {
            this.defaultEventExecutorGroup.shutdownGracefully();
        }
    }

}
