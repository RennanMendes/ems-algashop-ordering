package com.algaworks.algashop.ordering.domain.valueobject;

/**
 * Test Data Builder para Address.
 * Fornece um Address válido por padrão e um AddressBuilder pré-configurado
 * para variações nos testes.
 */
public class AddressTestDataBuilder {

    private AddressTestDataBuilder() {
    }

    /**
     * Retorna um Address totalmente preenchido e válido.
     */
    public static Address defaultAddress() {
        return addressBuilder().build();
    }

    /**
     * Retorna um Address.AddressBuilder pré-configurado com valores padrão.
     * Permite sobrescrever campos antes de chamar build() nos testes.
     */
    public static Address.AddressBuilder addressBuilder() {
        return Address.builder()
                .street("Bourbon Street")
                .number("1134")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement("Apt. 114");
    }
}

