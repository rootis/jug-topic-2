package eu.eagercode.jugspringwebflux.routes;

import eu.eagercode.jugspringwebflux.handlers.TopicHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TopicRoute {

    @Bean
    public RouterFunction<ServerResponse> topicHandlerRoutes(TopicHandler topicHandler) {
        return route(GET("/topics"), topicHandler::getAll);
    }
}
