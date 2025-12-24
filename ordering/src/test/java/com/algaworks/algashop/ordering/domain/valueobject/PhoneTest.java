package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PhoneTest {

    @Test
    void shouldGenerateWithValue() {
        Phone phone = new Phone("+5511999999999");
        Assertions.assertThat(phone.value()).isEqualTo("+5511999999999");
    }

    @Test
    void shouldNotCreateWhenNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Phone(null));
    }

    @Test
    void shouldNotCreateWhenBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Phone("   "));
    }

}

