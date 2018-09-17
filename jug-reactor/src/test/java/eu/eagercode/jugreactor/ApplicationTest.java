package eu.eagercode.jugreactor;

import java.util.ArrayList;
import java.util.stream.IntStream;
import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;

public class ApplicationTest {

    private List<String> words = Arrays.asList("First", "Second", "Third", "Forth", "Fifth", "Sixth",
        "Seventh", "Eighth", "Ninth", "Tenth");

    @Test
    public void subscribe() {
        List<String> manyWords = new ArrayList<>();

        // This one
        IntStream.range(1, 1001).forEach(i -> words.forEach(w -> manyWords.add(w + i)));

        /* ... or the old one..?
        for (int i = 1; i <= 1000; i++) {
            for (int j = 1; j <= 10; j++) {
                manyWords.add(words.get(j) + i);
            }
        }
        */

        Flux<String> few = Flux.just("Hello", "World");
        Flux<String> more = Flux.fromIterable(manyWords);

        more.subscribe(System.out::println);
        System.out.println("---");
        few.subscribe(System.out::println);
    }

    @Test
    public void map() {
        Flux.just(1, 2, 3, 4)
            .log()
            .map(i -> i * 2)
            .subscribe(System.out::println);
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
                .delaySubscription(Duration.ofMillis(0)));

        flux.log().subscribe(System.out::println);
    }

    @Test
    public void delayBlockLast() {
        Flux<String> flux = Mono.just("Zero").concatWith(
                Flux.fromIterable(words)
                .delaySubscription(Duration.ofMillis(3000)));

        flux.subscribe(System.out::println);
        System.out.println("---");
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
                .delayElements(Duration.ofMillis(200));

        Flux.first(b, a).toIterable().forEach(System.out::println);
    }

    @Test
    public void differentSpeed() {
        Flux<String> a = Flux.just("1. Slow", "1. Motion", "1. Words")
                .delayElements(Duration.ofMillis(200));
        a.subscribe(System.out::println);
        Flux<String> b = Flux.just("2. Very", "2. Fast", "2. Flux")
                .delayElements(Duration.ofMillis(600));
        b.subscribe(System.out::println);

        a.blockLast();
        b.blockLast();
    }

    @Test
    public void connectableFlux() {
        ConnectableFlux<Object> publish = Flux.create(emitter ->{
            while (true) {
                emitter.next(System.currentTimeMillis());
            }
        }).sample(Duration.ofSeconds(1)).publish();

        publish.subscribe((o) -> System.out.println("First subscriber: " + o));
        publish.connect();
        publish.subscribe((o) -> System.out.println("Second subscriber: " + o));
        publish.connect();
    }

    @Test
    public void coldStream() {
        Flux<String> source = Flux.fromIterable(words)
            .doOnNext(System.out::println)
            .filter(s -> s.startsWith("F"))
            .map(String::toUpperCase);

        source.subscribe(w -> System.out.println("Subscriber 1: " + w));
        System.out.println("---");
        source.subscribe(w -> System.out.println("Subscriber 2: " + w));
    }

    @Test
    public void hotStream() {
        UnicastProcessor<String> hotSource = UnicastProcessor.create();

        Flux<String> hotFlux = hotSource.publish()
            .autoConnect()
            .map(String::toUpperCase);

        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));

        hotSource.onNext("ram");
        hotSource.onNext("sam");

        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));

        hotSource.onNext("dam");
        hotSource.onNext("lam");
        hotSource.onComplete();
    }

    @Test
    public void parallel() {
        Flux<String> source = Flux.fromIterable(words)
            .doOnNext(System.out::println)
            .filter(s -> s.startsWith("F"))
            .map(String::toUpperCase);

        source.subscribeOn(Schedulers.parallel(), true).subscribe(w -> System.out.println("1__SUBSCRIBER: " + w));
        System.out.println("---");
        source.subscribe(w -> System.out.println("2__SUBSCRIBER: " + w));
        System.out.println("---");
        source.subscribeOn(Schedulers.parallel(), true).subscribe(w -> System.out.println("3__SUBSCRIBER: " + w));
    }
}