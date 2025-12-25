package com.algaworks.algashop.ordering.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode HALF_EVEN = RoundingMode.HALF_EVEN;
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal value) {
        Objects.requireNonNull(value);
        this.value = value.setScale(2, HALF_EVEN);
        if (this.value.signum() == -1) {
            throw new IllegalArgumentException();
        }
    }

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money add(Money money) {
        return new Money(this.value.add(money.value));
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);
        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }
        return new Money(this.value.multiply(new BigDecimal(quantity.value())));
    }

    public Money divide(Money other) {
        return new Money(this.value.divide(other.value, HALF_EVEN));
    }

    @Override
    public int compareTo(Money o) {
        return this.value.compareTo(o.value);
    }
}
