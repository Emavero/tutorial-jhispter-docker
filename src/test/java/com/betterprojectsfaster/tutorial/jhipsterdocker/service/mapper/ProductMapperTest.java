package com.betterprojectsfaster.tutorial.jhipsterdocker.service.mapper;

import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ProductAsserts.*;
import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ProductTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductSample1();
        var actual = productMapper.toEntity(productMapper.toDto(expected));
        assertProductAllPropertiesEquals(expected, actual);
    }
}
