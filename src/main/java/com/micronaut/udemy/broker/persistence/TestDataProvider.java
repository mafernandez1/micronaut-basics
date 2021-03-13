package com.micronaut.udemy.broker.persistence;

import com.micronaut.udemy.broker.persistence.jpa.QuotesRepository;
import com.micronaut.udemy.broker.persistence.model.QuoteEntity;
import com.micronaut.udemy.broker.persistence.model.SymbolEntity;
import com.micronaut.udemy.broker.persistence.jpa.SymbolRepository;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Used to insert data at startup
 */
@Singleton
@Requires(notEnv = Environment.TEST)
public class TestDataProvider {
    private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
    private final ThreadLocalRandom current = ThreadLocalRandom.current();
    private final SymbolRepository symbolRepository;
    private final QuotesRepository quotesRepository;

    public TestDataProvider(SymbolRepository symbolRepository, QuotesRepository quotesRepository) {
        this.symbolRepository = symbolRepository;
        this.quotesRepository = quotesRepository;
    }

    @EventListener
    public void init(StartupEvent event) {
        if(symbolRepository.findAll().isEmpty()) {
            LOG.info("Inserting testing data since the db is empty.");
            symbolRepository.saveAll(Stream.of("APPL", "AMZN", "FB", "TSLA")
                    .map(SymbolEntity::new)
                    .collect(Collectors.toList()));
        }

        if(quotesRepository.findAll().isEmpty()) {
            LOG.info("Inserting testing quotes data since the db is empty.");
            symbolRepository.findAll().forEach(s -> {
                final QuoteEntity quoteEntity = new QuoteEntity();
                quoteEntity.setSymbol(s);
                quoteEntity.setAsk(randomValue());
                quoteEntity.setBid(randomValue());
                quoteEntity.setVolume(randomValue());
                quoteEntity.setLastPrice(randomValue());

                quotesRepository.save(quoteEntity);
            });
        }
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(current.nextDouble(1, 100));
    }
}
