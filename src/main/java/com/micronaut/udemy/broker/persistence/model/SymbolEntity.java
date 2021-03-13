package com.micronaut.udemy.broker.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "symbol")
@Table(name = "symbols")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymbolEntity {
    @Id
    private String value;
}
