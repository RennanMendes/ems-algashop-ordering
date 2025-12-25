package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ZipCodeTest {

    @Test
    void shouldGenerateWithValue() {
        ZipCode zip = new ZipCode("12345");
        Assertions.assertThat(zip.value()).isEqualTo("12345");
    }

    @Test
    void shouldNotCreateWhenNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ZipCode(null));
    }

    @Test
    void shouldNotCreateWhenBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ZipCode("   "));
    }

    @Test
    void shouldNotCreateWhenLengthDifferentThanFive() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ZipCode("1234"));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ZipCode("123456"));
    }
}

