package com.betterprojectsfaster.tutorial.jhipsterdocker.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingOrderAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShoppingOrderAllPropertiesEquals(ShoppingOrder expected, ShoppingOrder actual) {
        assertShoppingOrderAutoGeneratedPropertiesEquals(expected, actual);
        assertShoppingOrderAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShoppingOrderAllUpdatablePropertiesEquals(ShoppingOrder expected, ShoppingOrder actual) {
        assertShoppingOrderUpdatableFieldsEquals(expected, actual);
        assertShoppingOrderUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShoppingOrderAutoGeneratedPropertiesEquals(ShoppingOrder expected, ShoppingOrder actual) {
        assertThat(expected)
            .as("Verify ShoppingOrder auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShoppingOrderUpdatableFieldsEquals(ShoppingOrder expected, ShoppingOrder actual) {
        assertThat(expected)
            .as("Verify ShoppingOrder relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getTotalAmount()).as("check totalAmount").isEqualTo(actual.getTotalAmount()))
            .satisfies(e -> assertThat(e.getOrdered()).as("check ordered").isEqualTo(actual.getOrdered()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShoppingOrderUpdatableRelationshipsEquals(ShoppingOrder expected, ShoppingOrder actual) {}
}