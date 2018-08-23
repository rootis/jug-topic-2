package eu.eagercode.jugspringwebflux;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.ipc.netty.http.server.HttpServer;

import java.io.IOException;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class Server {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        new Server().startReactorServer();

        System.out.println("Press ENTER to exit.");
        System.in.read();
    }

    private void startReactorServer() {
        HttpHandler httpHandler = WebHttpHandlerBuilder
                .webHandler(RouterFunctions.toWebHandler(routingFunction()))
                .build();
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer.create(HOST, PORT).newHandler(adapter).block();
    }

    private RouterFunction<ServerResponse> routingFunction() {
        return route(GET("/"), request -> ok().body(fromObject("Status: Up.")));
    }
}
