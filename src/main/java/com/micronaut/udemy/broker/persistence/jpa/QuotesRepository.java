package com.micronaut.udemy.broker.persistence.jpa;

import com.micronaut.udemy.broker.persistence.model.QuoteDto;
import com.micronaut.udemy.broker.persistence.model.QuoteEntity;
import com.micronaut.udemy.broker.persistence.model.SymbolEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuotesRepository extends CrudRepository<QuoteEntity, Long> {

    @Override
    List<QuoteEntity> findAll();

    List<QuoteDto> listOrderByVolume();

    List<QuoteDto> listOrderByVolumeDesc();

    List<QuoteDto> findByVolumeGreaterThan(BigDecimal volume);

    Optional<QuoteEntity> findBySymbol(SymbolEntity symbolEntity);

    // Pagination
    List<QuoteDto> findByVolumeGreaterThan(BigDecimal volume, Pageable pageable);

    Slice<QuoteDto> list(Pageable pageable);
}
