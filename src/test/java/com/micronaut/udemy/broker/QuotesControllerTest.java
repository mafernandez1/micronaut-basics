package com.micronaut.udemy.broker;

import com.micronaut.udemy.BaseTestController;
import com.micronaut.udemy.broker.error.CustomError;
import com.micronaut.udemy.broker.model.Quote;
import com.micronaut.udemy.broker.model.Symbol;
import com.micronaut.udemy.broker.store.InMemoryStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class QuotesControllerTest extends BaseTestController {

    @Inject
    InMemoryStore store;

    @Test
    void testReturnQuotePerSymbol() {
        Quote apple = initRandomQuote("APPL");
        store.update(apple);

        Quote appleResult = client.toBlocking().retrieve(HttpRequest.GET("/quotes/" + apple.getSymbol().getValue()), Quote.class);
        Assertions.assertThat(apple).usingRecursiveComparison().isEqualTo(appleResult);

        Quote amazon = initRandomQuote("AMZN");
        store.update(amazon);

        Quote amazonResult = client.toBlocking().retrieve(HttpRequest.GET("/quotes/" + amazon.getSymbol().getValue()), Quote.class);
        Assertions.assertThat(amazon).usingRecursiveComparison().isEqualTo(amazonResult);
    }

    @Test
    void testUnsupportedSymbol() {
        HttpClientResponseException thrown = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(HttpRequest.GET("/quotes/UNSUPPORTED")),
                "Expected GET call to throw, but it didn't");

        assertEquals(HttpStatus.NOT_FOUND.getCode(), thrown.getStatus().getCode());
        final Optional<CustomError> customError = thrown.getResponse().getBody(CustomError.class);
        assertTrue(customError.isPresent());
    }

    private Quote initRandomQuote(String symbol) {
        return Quote.builder()
                .symbol(new Symbol(symbol))
                .bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build();
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }
}