package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record Quantity(Integer value) implements Comparable<Quantity> {
    public static final Quantity ZERO = new Quantity(1);

    public Quantity {
        Objects.requireNonNull(value);
        if (value < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Quantity add(Quantity quantity) {
        Objects.requireNonNull(quantity);
        return new Quantity(Integer.sum(this.value, quantity.value));
    }

    @Override
    public int compareTo(Quantity o) {
        return this.value.compareTo(o.value);
    }
}
