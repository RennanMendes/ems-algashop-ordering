package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FullNameTest {

    @Test
    void shouldGenerateWithTrimmedValues() {
        FullName fullName = new FullName(" John ", " Doe ");
        Assertions.assertThat(fullName.firstName()).isEqualTo("John");
        Assertions.assertThat(fullName.lastName()).isEqualTo("Doe");
        Assertions.assertThat(fullName).hasToString("John Doe");
    }

    @Test
    void shouldNotCreateWhenFirstNameNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new FullName(null, "Last"));
    }

    @Test
    void shouldNotCreateWhenLastNameNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new FullName("First", null));
    }

    @Test
    void shouldNotCreateWhenFirstNameBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FullName("   ", "Last"));
    }

    @Test
    void shouldNotCreateWhenLastNameBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FullName("First", "   "));
    }
}
