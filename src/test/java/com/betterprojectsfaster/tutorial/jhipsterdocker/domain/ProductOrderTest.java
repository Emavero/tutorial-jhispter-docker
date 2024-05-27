package com.betterprojectsfaster.tutorial.jhipsterdocker.domain;

import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ProductOrderTestSamples.*;
import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ProductTestSamples.*;
import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.betterprojectsfaster.tutorial.jhipsterdocker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOrder.class);
        ProductOrder productOrder1 = getProductOrderSample1();
        ProductOrder productOrder2 = new ProductOrder();
        assertThat(productOrder1).isNotEqualTo(productOrder2);

        productOrder2.setId(productOrder1.getId());
        assertThat(productOrder1).isEqualTo(productOrder2);

        productOrder2 = getProductOrderSample2();
        assertThat(productOrder1).isNotEqualTo(productOrder2);
    }

    @Test
    void productTest() throws Exception {
        ProductOrder productOrder = getProductOrderRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productOrder.setProduct(productBack);
        assertThat(productOrder.getProduct()).isEqualTo(productBack);

        productOrder.product(null);
        assertThat(productOrder.getProduct()).isNull();
    }

    @Test
    void overallOrderTest() throws Exception {
        ProductOrder productOrder = getProductOrderRandomSampleGenerator();
        ShoppingOrder shoppingOrderBack = getShoppingOrderRandomSampleGenerator();

        productOrder.setOverallOrder(shoppingOrderBack);
        assertThat(productOrder.getOverallOrder()).isEqualTo(shoppingOrderBack);

        productOrder.overallOrder(null);
        assertThat(productOrder.getOverallOrder()).isNull();
    }
}
