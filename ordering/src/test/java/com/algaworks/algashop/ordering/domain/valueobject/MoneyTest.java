package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class MoneyTest {

    @Test
    void shouldGenerateWithValueAndScale() {
        Money m = new Money(new BigDecimal("10"));
        Assertions.assertThat(m.value()).isEqualTo(new BigDecimal("10.00"));
    }

    @Test
    void shouldNotCreateWhenNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Money((BigDecimal) null));
    }

    @Test
    void shouldNotCreateWhenNegative() {
        BigDecimal negative = new BigDecimal("-0.01");
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Money(negative));
    }

    @Test
    void shouldAddMoney() {
        Money a = new Money(new BigDecimal("1.10"));
        Money b = new Money(new BigDecimal("2.25"));
        Assertions.assertThat(a.add(b).value()).isEqualTo(new BigDecimal("3.35"));
    }

    @Test
    void shouldMultiplyByQuantity() {
        Money a = new Money(new BigDecimal("2.00"));
        Quantity q = new Quantity(3);
        Assertions.assertThat(a.multiply(q).value()).isEqualTo(new BigDecimal("6.00"));
    }

    @Test
    void shouldNotMultiplyWhenQuantityLessThanOne() {
        Money a = new Money(new BigDecimal("2.00"));
        Quantity q = new Quantity(0);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> a.multiply(q));
    }

    @Test
    void shouldDivideMoney() {
        Money a = new Money(new BigDecimal("10.00"));
        Money b = new Money(new BigDecimal("4.00"));
        Assertions.assertThat(a.divide(b).value()).isEqualTo(new BigDecimal("2.50"));
    }

    @Test
    void shouldCompareMoney() {
        Money a = new Money(new BigDecimal("1.00"));
        Money b = new Money(new BigDecimal("2.00"));
        Assertions.assertThat(a.compareTo(b)).isLessThan(0);
        Assertions.assertThat(b.compareTo(a)).isGreaterThan(0);
        Assertions.assertThat(a.compareTo(new Money(new BigDecimal("1.00")))).isZero();
    }
}
