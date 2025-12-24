package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void shouldGenerateWithValue() {
        Email email = new Email("john.doe@gmail.com");
        Assertions.assertThat(email.value()).isEqualTo("john.doe@gmail.com");
    }

    @Test
    void shouldNotCreateWhenNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Email(null));
    }

    @Test
    void shouldNotCreateWhenBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email("   "));
    }

    @Test
    void shouldNotCreateWhenInvalidFormat() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email("invalid"));
    }
}

