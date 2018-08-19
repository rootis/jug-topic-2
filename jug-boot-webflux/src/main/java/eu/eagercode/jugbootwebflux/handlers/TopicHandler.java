package eu.eagercode.jugbootwebflux.handlers;

import eu.eagercode.jugbootwebflux.model.Topic;
import eu.eagercode.jugbootwebflux.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TopicHandler {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicHandler(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<Topic> topics = topicRepository.findAllFlux();
        return ServerResponse.ok().body(topics, Topic.class);
    }
}
