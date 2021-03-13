package com.micronaut.udemy.broker;

import com.micronaut.udemy.broker.error.CustomError;
import com.micronaut.udemy.broker.model.Quote;
import com.micronaut.udemy.broker.persistence.jpa.QuotesRepository;
import com.micronaut.udemy.broker.persistence.model.QuoteDto;
import com.micronaut.udemy.broker.persistence.model.QuoteEntity;
import com.micronaut.udemy.broker.persistence.model.SymbolEntity;
import com.micronaut.udemy.broker.store.InMemoryStore;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("quotes")
public class QuotesController {

    private final InMemoryStore store;
    private final QuotesRepository quotesRepository;

    public QuotesController(InMemoryStore store, QuotesRepository quotesRepository) {
        this.store = store;
        this.quotesRepository = quotesRepository;
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

    @Get("jpa")
    public List<QuoteEntity> getQuoteViaJpa() {
        return quotesRepository.findAll();
    }

    @Get("jpa/ordered")
    public List<QuoteDto> getOrderedQuotes(@QueryValue(defaultValue = "desc") String order) {
        return "desc".equalsIgnoreCase(order) ? quotesRepository.listOrderByVolumeDesc() : quotesRepository.listOrderByVolume();
    }

    @Get("jpa/volume/{volume}")
    public List<QuoteDto> volumeFilter(@PathVariable BigDecimal volume) {
        return quotesRepository.findByVolumeGreaterThan(volume);
    }

    @Get("jpa/pagination")
    public List<QuoteDto> volumeFilterPagination(
            @QueryValue(defaultValue = "0") Integer page,
            @QueryValue(defaultValue = "0") BigDecimal volume) {
        return quotesRepository.findByVolumeGreaterThan(volume, Pageable.from(page, 2));
    }

    @Get("jpa/pagination/{page}")
    public List<QuoteDto> allPagination(@PathVariable int page) {
        return quotesRepository.list(Pageable.from(page)).getContent();
    }

    @Get("jpa/{symbol}")
    public HttpResponse<Object> getQuoteViaJpa(@PathVariable  String symbol) {
        Optional<QuoteEntity> quote = quotesRepository.findBySymbol(new SymbolEntity(symbol));
        if(quote.isEmpty()) {
            return HttpResponse.notFound(CustomError.builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.getReason())
                    .message("quoteNotFound")
                    .path("/quotes/jpa/" + symbol)
                    .build());
        }
        return HttpResponse.ok(quote.get());
    }
}
