package com.micronaut.udemy.broker;

import com.micronaut.udemy.BaseTestController;
import com.micronaut.udemy.broker.model.Symbol;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class MarketsControllerTest extends BaseTestController {

    @Test
    void testReturnListOfMarkets() {
        Symbol[] result = client.toBlocking().retrieve("/markets", Symbol[].class);

        Assertions.assertEquals(7, result.length);
        assertThat(result)
                .extracting(Symbol::getValue)
                .containsExactlyInAnyOrder("APPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA");
    }
}
