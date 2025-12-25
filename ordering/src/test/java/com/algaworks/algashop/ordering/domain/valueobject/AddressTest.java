package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressTest {

    private ZipCode zip() {
        return new ZipCode("12345");
    }

    @Test
    void shouldCreateWhenAllFieldsValid() {
        Address a = AddressTestDataBuilder.defaultAddress();

        Assertions.assertThat(a.street()).isEqualTo("Bourbon Street");
        Assertions.assertThat(a.zipCode()).isEqualTo(zip());
    }

    @Test
    void shouldNotCreateWhenStreetIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().street(null).build());
    }

    @Test
    void shouldNotCreateWhenStreetIsBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().street("   ").build());
    }

    @Test
    void shouldNotCreateWhenNeighborhoodIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().neighborhood(null).build());
    }

    @Test
    void shouldNotCreateWhenNeighborhoodIsBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().neighborhood("   ").build());
    }

    @Test
    void shouldNotCreateWhenCityIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().city(null).state("ST").build());
    }

    @Test
    void shouldNotCreateWhenCityIsBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().city("   ").zipCode(zip()).build());
    }

    @Test
    void shouldNotCreateWhenNumberIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().number(null).build());
    }

    @Test
    void shouldNotCreateWhenNumberIsBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().number("   ").build());
    }

    @Test
    void shouldNotCreateWhenStateIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().state(null).build());
    }

    @Test
    void shouldNotCreateWhenStateIsBlank() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().state("   ").build());
    }

    @Test
    void shouldNotCreateWhenZipCodeIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> AddressTestDataBuilder.addressBuilder().zipCode(null).build());
    }

    @Test
    void shouldAllowComplementNullOrBlank() {
        Address a1 = AddressTestDataBuilder.addressBuilder()
                .complement(null)
                .build();

        Address a2 = AddressTestDataBuilder.addressBuilder()
                .complement("   ")
                .build();

        Assertions.assertThat(a1.complement()).isNull();
        Assertions.assertThat(a2.complement()).isEqualTo("   ");
    }
}
