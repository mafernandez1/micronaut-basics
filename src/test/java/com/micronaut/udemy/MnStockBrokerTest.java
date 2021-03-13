package com.micronaut.udemy;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
class MnStockBrokerTest extends BaseTestController{

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testHelloWorld() {
        String result = client.toBlocking().retrieve("/hello");

        Assertions.assertEquals("Hello from service", result);
    }

}
