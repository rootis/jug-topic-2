package eu.eagercode.jugwebflux.repositories;

import eu.eagercode.jugwebflux.model.Topic;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class TopicRepository {

    public List<Topic> findAll() {
        return Arrays.asList(
                new Topic("First topic", "First topic description."),
                new Topic("Second topic", "Second topic description."),
                new Topic("Third topic", "Third topic description."),
                new Topic("Fourth topic", "Fourth topic description."),
                new Topic("Fifth topic", "Fifth topic description.")
        );
    }

    public Flux<Topic> findAllFlux() {
        return Flux.fromIterable(findAll());
    }
}
