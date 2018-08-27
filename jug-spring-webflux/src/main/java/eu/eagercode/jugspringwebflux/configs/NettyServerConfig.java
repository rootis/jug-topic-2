package eu.eagercode.jugspringwebflux.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.ipc.netty.http.server.HttpServer;

import javax.annotation.PostConstruct;

@Configuration
public class NettyServerConfig {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    private final RouterFunction<ServerResponse> routerFunction;

    @Autowired
    public NettyServerConfig(RouterFunction<ServerResponse> routerFunction) {
        this.routerFunction = routerFunction;
    }

    @PostConstruct
    public void start() {
        HttpHandler httpHandler = WebHttpHandlerBuilder
                .webHandler(RouterFunctions.toWebHandler(routerFunction))
                .build();
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer.create(HOST, PORT).newHandler(adapter).block();
    }
}
