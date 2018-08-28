package eu.eagercode.jugwebflux.handlers;

import eu.eagercode.jugwebflux.model.Topic;
import eu.eagercode.jugwebflux.repositories.TopicRepository;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TopicHandler {

    private final TopicRepository topicRepository;

    public TopicHandler(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<Topic> topics = topicRepository.findAllFlux();
        return ServerResponse.ok().body(topics, Topic.class);
    }
}
