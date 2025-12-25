package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.validator.FieldValidations;

import java.util.Objects;

public record ProductName(String value) {
    public ProductName {
        Objects.requireNonNull(value);
        FieldValidations.requiresNonBlank(value);
    }
}
