package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class BirthDateTest {

    @Test
    void shouldGenerateWithValue() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1991, 7, 5));
        Assertions.assertThat(birthDate.value()).isEqualTo(LocalDate.of(1991, 7, 5));
    }

    @Test
    void shouldNotAddValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new BirthDate(LocalDate.now().plusDays(1)));
    }

    @Test
    void shouldCalculateAge() {
        BirthDate birthDate = new BirthDate(LocalDate.now().minusYears(25));

        Assertions.assertThat(birthDate.age()).isEqualTo(25);
    }

}