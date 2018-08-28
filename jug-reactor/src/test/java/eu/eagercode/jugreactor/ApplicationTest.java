package eu.eagercode.jugreactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApplicationTest {

    private List<String> words = Arrays.asList("First", "Second", "Third", "Forth", "Fifth", "Sixth");

    @Test
    public void subscribe() {
        Flux<String> few = Flux.just("Hello", "World");
        Flux<String> more = Flux.fromIterable(words);

        few.subscribe(System.out::println);
        System.out.println("---");
        more.subscribe(System.out::println);
    }

    @Test
    public void sortLetters() {
        Flux<String> letters = Flux.fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .distinct()
                .sort(Comparator.reverseOrder())
                .zipWith(Flux.range(1, Integer.MAX_VALUE), (s, i) -> String.format("%d. %s", i, s));

        letters.subscribe(System.out::println);
    }

    @Test
    public void countLetters() {
        Flux<String> letters = Flux.fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .sort()
                .groupBy(String::toString)
                .flatMap(group -> Mono.zip(Mono.just(group.key()), group.count()))
                .map(keyAndCount -> String.format("%s - %d", keyAndCount.getT1(), keyAndCount.getT2()));

        letters.subscribe(System.out::println);
    }

    @Test
    public void delay() {
        Flux<String> flux = Mono.just("Zero").concatWith(
                Flux.fromIterable(words)
                .delaySubscription(Duration.ofMillis(500)));

        flux.subscribe(System.out::println);
    }

    @Test
    public void delayBlockLast() {
        Flux<String> flux = Mono.just("Zero").concatWith(
                Flux.fromIterable(words)
                .delaySubscription(Duration.ofMillis(2000)));

        flux.subscribe(System.out::println);
        flux.blockLast();
    }

    @Test
    public void delayToStream() {
        Flux<String> flux = Mono.just("Zero").concatWith(
                Flux.fromIterable(words)
                .delaySubscription(Duration.ofMillis(2000)));

        flux.toStream().forEach(System.out::println);
    }

    @Test
    public void first() {
        Mono<String> a = Mono.just("Slow mono")
                .delaySubscription(Duration.ofMillis(300));
        Flux<String> b = Flux.just("Very", "Fast", "Flux")
                .delayElements(Duration.ofMillis(400));

        Flux.first(b, a).toIterable().forEach(System.out::println);
    }

    @Test
    public void differentSpeed() {
        Flux<String> a = Flux.just("1. Slow", "1. Motion", "1. Words")
                .delayElements(Duration.ofMillis(400));
        a.subscribe(System.out::println);
        Flux<String> b = Flux.just("2. Very", "2. Fast", "2. Flux")
                .delayElements(Duration.ofMillis(600));
        b.subscribe(System.out::println);

        a.blockLast();
        b.blockLast();
    }
}