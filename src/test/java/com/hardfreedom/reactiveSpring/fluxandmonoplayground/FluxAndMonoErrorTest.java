package com.hardfreedom.reactiveSpring.fluxandmonoplayground;

import com.hardfreedom.reactiveSpring.fluxandmonoplayground.exception.CustomException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandling() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D")) //this one will never happen because previously error occurs
                .onErrorResume((e) -> { //because I added this onErrorResume, on error it will sout and create new flux instead of error.
                    System.out.println("Exception is: " + e);
                    return Flux.just("default", "default1");
                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
//                .expectError(RuntimeException.class)
//                .verify();
                .expectNext("default", "default1")//even though error occurred, I have handled it.
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorReturn() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D")) //this one will never happen because previously error occurs
                .onErrorReturn("default");

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")//even though error occurred, I have handled it.
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorMap() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D")) //this one will never happen because previously error occurs
                .onErrorMap((e) -> new CustomException(e)); //we can change exception from one type to another type

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)//even though error occurred, I have handled it.
                .verify();
    }

    @Test
    public void fluxErrorHandling_onErrorMap_withRetry() { //if it catches error, it will try again and in case of error it will fail then

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D")) //this one will never happen because previously error occurs
                .onErrorMap((e) -> new CustomException(e)) //we can change exception from one type to another type
                .retry(2);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)//even though error occurred, I have handled it.
                .verify();
    }
}
