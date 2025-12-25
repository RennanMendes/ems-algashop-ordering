package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class CustomerIdTest {

    @Test
    void shouldGenerateWithProvidedValue() {
        UUID id = UUID.randomUUID();
        CustomerId customerId = new CustomerId(id);
        Assertions.assertThat(customerId.value()).isEqualTo(id);
        Assertions.assertThat(customerId).hasToString(id.toString());
    }

    @Test
    void shouldGenerateWhenNoArg() {
        CustomerId customerId = new CustomerId();
        Assertions.assertThat(customerId.value()).isNotNull();
    }

    @Test
    void shouldNotCreateWhenNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new CustomerId(null));
    }
}
