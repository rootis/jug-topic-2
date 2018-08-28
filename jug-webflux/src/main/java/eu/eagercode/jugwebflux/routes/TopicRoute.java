package eu.eagercode.jugwebflux.routes;

import eu.eagercode.jugwebflux.handlers.TopicHandler;
import eu.eagercode.jugwebflux.repositories.TopicRepository;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class TopicRoute {

    public static RouterFunction<ServerResponse> routes() {
        TopicRepository topicRepository = new TopicRepository();
        TopicHandler topicHandler = new TopicHandler(topicRepository);

        return topicHandlerRoutes(topicHandler);
    }

    private static RouterFunction<ServerResponse> topicHandlerRoutes(TopicHandler topicHandler) {
        return route(GET("/topics"), topicHandler::getAll);
    }
}
