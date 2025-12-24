package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void shouldGenerateWithValue() {
        Document document = new Document("255-08-0578");
        Assertions.assertThat(document.value()).isEqualTo("255-08-0578");
    }

    @Test
    void shouldNotCreateWhenNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Document(null));
    }

    @Test
    void shouldNotCreateWhenBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Document("  "));
    }
}

