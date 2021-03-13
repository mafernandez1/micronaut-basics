package com.micronaut.udemy.broker;

import com.micronaut.udemy.broker.model.Symbol;
import com.micronaut.udemy.broker.persistence.jpa.SymbolRepository;
import com.micronaut.udemy.broker.persistence.model.SymbolEntity;
import com.micronaut.udemy.broker.store.InMemoryStore;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("markets")
public class MarketsController {

    private final InMemoryStore store;
    private final SymbolRepository symbolRepository;

    public MarketsController(InMemoryStore store, SymbolRepository symbolRepository) {
        this.store = store;
        this.symbolRepository = symbolRepository;
    }

    @Get
    public List<Symbol> all() {
        return store.getAllSymbols();
    }

    @Get("jpa")
    public Single<List<SymbolEntity>> allViaJpa() {
        return Single.just(symbolRepository.findAll());
    }

}
