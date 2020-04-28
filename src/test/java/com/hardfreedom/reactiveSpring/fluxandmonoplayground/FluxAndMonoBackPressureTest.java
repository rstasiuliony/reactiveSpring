package com.hardfreedom.reactiveSpring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest { //Backpressure is when subscriber controls the data flow from publisher.

    @Test
    public void backPressureTest() {

        Flux<Integer> finiteFlux = Flux.range(1, 10) //creates flux with 10 items
                .log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .thenRequest(1) //with thenRequest I can control how many elements I want to get.
                .expectNext(1) //flux value that I get
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() {

        Flux<Integer> finiteFlux = Flux.range(1, 10) //creates flux with 10 items
                .log();

        finiteFlux.subscribe((element) -> System.out.println("Element is: " + element)
                , (exception) -> System.err.println("Exception is: " + exception)
                , () -> System.out.println("Done")
                , (subscription) -> subscription.request(2));
    }

    @Test
    public void backPressure_cancel() {

        Flux<Integer> finiteFlux = Flux.range(1, 10) //creates flux with 10 items
                .log();

        finiteFlux.subscribe((element) -> System.out.println("Element is: " + element)
                , (exception) -> System.err.println("Exception is: " + exception)
                , () -> System.out.println("Done")
                , (subscription) -> subscription.cancel());
    }

    @Test
    public void customized_backPressure() {

        Flux<Integer> finiteFlux = Flux.range(1, 10) //creates flux with 10 items
                .log();

        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("Value received is: " + value);
                if (value == 4) {
                    cancel();
                }
            }
        });
    }
}
