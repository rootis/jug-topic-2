package eu.eagercode.jugbootwebflux.controllershandlers;

import eu.eagercode.jugbootwebflux.model.Topic;
import eu.eagercode.jugbootwebflux.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/topics-mvc-flux")
public class TopicControllerHandler {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicControllerHandler(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @GetMapping
    public Flux<Topic> getTopics() {
        return topicRepository.findAllFlux();
    }
}
