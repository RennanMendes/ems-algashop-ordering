package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductNameTest {

    @Test
    void shouldGenerateWithValue() {
        ProductName p = new ProductName("Product");
        Assertions.assertThat(p.value()).isEqualTo("Product");
    }

    @Test
    void shouldNotCreateWhenNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ProductName(null));
    }

    @Test
    void shouldNotCreateWhenBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ProductName("   "));
    }
}

