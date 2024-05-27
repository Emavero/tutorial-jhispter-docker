package com.betterprojectsfaster.tutorial.jhipsterdocker.service.mapper;

import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrderAsserts.*;
import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingOrderMapperTest {

    private ShoppingOrderMapper shoppingOrderMapper;

    @BeforeEach
    void setUp() {
        shoppingOrderMapper = new ShoppingOrderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShoppingOrderSample1();
        var actual = shoppingOrderMapper.toEntity(shoppingOrderMapper.toDto(expected));
        assertShoppingOrderAllPropertiesEquals(expected, actual);
    }
}
