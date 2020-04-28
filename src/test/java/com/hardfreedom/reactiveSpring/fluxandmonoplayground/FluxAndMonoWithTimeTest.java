package com.hardfreedom.reactiveSpring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {

        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200)) //in every 200 ms it will generate long value from 0-->...
                .log();

        infiniteFlux.subscribe((element) -> System.out.println("Value is: " + element));

        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {

        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100)) //in every 200 ms it will generate long value from 0-->...
                .take(3)
                .log();

        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceMap() throws InterruptedException {

        Flux<Integer> infiniteFlux = Flux.interval(Duration.ofMillis(100)) //in every 200 ms it will generate long value from 0-->...
                .map(loong -> new Integer(loong.intValue()))
                .take(3)
                .log();

        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceMap_withDelay() throws InterruptedException {

        Flux<Integer> infiniteFlux = Flux.interval(Duration.ofMillis(100)) //in every 200 ms it will generate long value from 0-->...
                .delayElements(Duration.ofSeconds(1))
                .map(loong -> new Integer(loong.intValue()))
                .take(3)
                .log();

        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }
}
