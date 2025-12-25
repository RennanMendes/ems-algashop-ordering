package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    void shouldCreateWhenValueIsZero() {
        Quantity q = new Quantity(0);
        Assertions.assertThat(q.value()).isEqualTo(0);
    }

    @Test
    void shouldCreateWhenValueIsPositive() {
        Quantity q = new Quantity(5);
        Assertions.assertThat(q.value()).isEqualTo(5);
    }

    @Test
    void shouldNotCreateWhenValueIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Quantity(null));
    }

    @Test
    void shouldNotCreateWhenValueIsNegative() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Quantity(-1));
    }

    @Test
    void shouldAddQuantities() {
        Quantity a = new Quantity(2);
        Quantity b = new Quantity(3);
        Quantity result = a.add(b);
        Assertions.assertThat(result.value()).isEqualTo(5);
    }

    @Test
    void shouldNotAddWhenArgumentNull() {
        Quantity a = new Quantity(2);
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> a.add(null));
    }

    @Test
    void shouldCompareQuantities() {
        Quantity small = new Quantity(1);
        Quantity medium = new Quantity(2);
        Quantity sameAsMedium = new Quantity(2);

        Assertions.assertThat(small.compareTo(medium)).isLessThan(0);
        Assertions.assertThat(medium.compareTo(small)).isGreaterThan(0);
        Assertions.assertThat(medium.compareTo(sameAsMedium)).isEqualTo(0);
    }
}

