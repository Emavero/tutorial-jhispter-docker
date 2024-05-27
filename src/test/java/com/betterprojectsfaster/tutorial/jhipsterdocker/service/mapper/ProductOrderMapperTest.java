package com.betterprojectsfaster.tutorial.jhipsterdocker.service.mapper;

import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ProductOrderAsserts.*;
import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ProductOrderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductOrderMapperTest {

    private ProductOrderMapper productOrderMapper;

    @BeforeEach
    void setUp() {
        productOrderMapper = new ProductOrderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductOrderSample1();
        var actual = productOrderMapper.toEntity(productOrderMapper.toDto(expected));
        assertProductOrderAllPropertiesEquals(expected, actual);
    }
}
