package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record Phone(String value) {
    public Phone {
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
