package eu.eagercode.jugwebflux.configs;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.ipc.netty.http.server.HttpServer;

import java.util.function.Supplier;

public class NettyServerConfig {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void start(Supplier<RouterFunction<ServerResponse>> routeSupplier) {
        HttpHandler httpHandler = WebHttpHandlerBuilder
                .webHandler(RouterFunctions.toWebHandler(routeSupplier.get()))
                .build();
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer.create(HOST, PORT).newHandler(adapter).block();
    }
}
