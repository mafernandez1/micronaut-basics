package com.micronaut.udemy.broker;

import com.micronaut.udemy.broker.error.CustomError;
import com.micronaut.udemy.broker.model.Quote;
import com.micronaut.udemy.broker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("quotes")
public class QuotesController {

    private final InMemoryStore store;

    public QuotesController(InMemoryStore store) {
        this.store = store;
    }

    @Get("/{symbol}")
    public HttpResponse<Object> getQuote(@PathVariable  String symbol) {
        Optional<Quote> quote = store.fetchQuote(symbol);
        if(quote.isEmpty()) {
            return HttpResponse.notFound(CustomError.builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.getReason())
                    .message("quoteNotFound")
                    .path("/quotes/" + symbol)
                    .build());
        }
        return HttpResponse.ok(quote.get());
    }
}
