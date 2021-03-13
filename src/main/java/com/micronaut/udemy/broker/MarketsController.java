package com.micronaut.udemy.broker;

import com.micronaut.udemy.broker.model.Symbol;
import com.micronaut.udemy.broker.store.InMemoryStore;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("markets")
public class MarketsController {

    private final InMemoryStore store;

    public MarketsController(InMemoryStore store) {
        this.store = store;
    }

    @Get
    public List<Symbol> all() {
        return store.getAllSymbols();
    }

}
