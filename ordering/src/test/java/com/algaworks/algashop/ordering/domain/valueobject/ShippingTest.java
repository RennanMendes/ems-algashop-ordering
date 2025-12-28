package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ShippingTest {

    private Shipping.ShippingBuilder baseBuilder() {
        return Shipping.builder()
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusDays(1))
                .address(AddressTestDataBuilder.defaultAddress())
                .recipient(Recipient.builder()
                        .fullName(new FullName("John","Doe"))
                        .document(new Document("111-11-1111"))
                        .phone(new Phone("123-111-9911"))
                        .build());
    }

    @Test
    void shouldCreateWhenAllFieldsValid() {
        Shipping s = baseBuilder().build();

        Assertions.assertThat(s.cost()).isEqualTo(new Money("10.00"));
        Assertions.assertThat(s.expectedDate()).isEqualTo(LocalDate.now().plusDays(1));
        Assertions.assertThat(s.address()).isEqualTo(AddressTestDataBuilder.defaultAddress());
        Assertions.assertThat(s.recipient().fullName()).isEqualTo(new FullName("John","Doe"));
    }

    @Test
    void shouldNotCreateWhenCostIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> baseBuilder().cost(null).build());
    }

    @Test
    void shouldNotCreateWhenExpectedDateIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> baseBuilder().expectedDate(null).build());
    }

    @Test
    void shouldNotCreateWhenRecipientIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> baseBuilder().recipient(null).build());
    }

    @Test
    void shouldNotCreateWhenAddressIsNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> baseBuilder().address(null).build());
    }

    @Test
    void shouldAllowZeroCostAndTodayExpectedDate() {
        Shipping s = baseBuilder()
                .cost(Money.ZERO)
                .expectedDate(LocalDate.now())
                .build();

        Assertions.assertThat(s.cost()).isEqualTo(Money.ZERO);
        Assertions.assertThat(s.expectedDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldAllowPastExpectedDate() {
        // Shipping doesn't validate the expected date range itself
        Shipping s = baseBuilder()
                .expectedDate(LocalDate.now().minusDays(1))
                .build();

        Assertions.assertThat(s.expectedDate()).isEqualTo(LocalDate.now().minusDays(1));
    }

    @Test
    void shouldAllowVerySmallPositiveCost() {
        Shipping s = baseBuilder()
                .cost(new Money("0.01"))
                .build();

        Assertions.assertThat(s.cost()).isEqualTo(new Money("0.01"));
    }

    @Test
    void shouldNotAllowNegativeMoneyWhenCreatingMoney() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Money(new java.math.BigDecimal("-0.01")));
    }
}

