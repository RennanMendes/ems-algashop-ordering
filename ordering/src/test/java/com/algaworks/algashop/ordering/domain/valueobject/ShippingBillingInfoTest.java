package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShippingBillingInfoTest {

    @Test
    void shouldCreateShippingInfo() {
        FullName fn = new FullName("John", "Doe");
        Document doc = new Document("12345678901");
        Phone phone = new Phone("+5511999999999");
        Address address = Address.builder()
                .street("Street")
                .neighborhood("Neighborhood")
                .city("City")
                .number("123")
                .state("ST")
                .zipCode(new ZipCode("12345"))
                .build();

        ShippingInfo s = new ShippingInfo(fn, doc, phone, address);
        BillingInfo b = new BillingInfo(fn, doc, phone, address);

        Assertions.assertThat(s.fullName()).isEqualTo(fn);
        Assertions.assertThat(b.address()).isEqualTo(address);
    }

    @Test
    void shouldNotCreateWhenAnyIsNull() {
        FullName fn = new FullName("John", "Doe");
        Document doc = new Document("12345678901");
        Phone phone = new Phone("+5511999999999");
        Address address = Address.builder()
                .street("Street")
                .neighborhood("Neighborhood")
                .city("City")
                .number("123")
                .state("ST")
                .zipCode(new ZipCode("12345"))
                .build();

        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ShippingInfo(null, doc, phone, address));

        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BillingInfo(fn, null, phone, address));

        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ShippingInfo(fn, doc, null, address));

        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BillingInfo(fn, doc, phone, null));
    }
}

