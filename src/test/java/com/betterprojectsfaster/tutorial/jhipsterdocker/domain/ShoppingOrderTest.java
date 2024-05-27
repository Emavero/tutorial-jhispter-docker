package com.betterprojectsfaster.tutorial.jhipsterdocker.domain;

import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ProductOrderTestSamples.*;
import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShipmentTestSamples.*;
import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.betterprojectsfaster.tutorial.jhipsterdocker.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ShoppingOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingOrder.class);
        ShoppingOrder shoppingOrder1 = getShoppingOrderSample1();
        ShoppingOrder shoppingOrder2 = new ShoppingOrder();
        assertThat(shoppingOrder1).isNotEqualTo(shoppingOrder2);

        shoppingOrder2.setId(shoppingOrder1.getId());
        assertThat(shoppingOrder1).isEqualTo(shoppingOrder2);

        shoppingOrder2 = getShoppingOrderSample2();
        assertThat(shoppingOrder1).isNotEqualTo(shoppingOrder2);
    }

    @Test
    void ordersTest() throws Exception {
        ShoppingOrder shoppingOrder = getShoppingOrderRandomSampleGenerator();
        ProductOrder productOrderBack = getProductOrderRandomSampleGenerator();

        shoppingOrder.addOrders(productOrderBack);
        assertThat(shoppingOrder.getOrders()).containsOnly(productOrderBack);
        assertThat(productOrderBack.getOverallOrder()).isEqualTo(shoppingOrder);

        shoppingOrder.removeOrders(productOrderBack);
        assertThat(shoppingOrder.getOrders()).doesNotContain(productOrderBack);
        assertThat(productOrderBack.getOverallOrder()).isNull();

        shoppingOrder.orders(new HashSet<>(Set.of(productOrderBack)));
        assertThat(shoppingOrder.getOrders()).containsOnly(productOrderBack);
        assertThat(productOrderBack.getOverallOrder()).isEqualTo(shoppingOrder);

        shoppingOrder.setOrders(new HashSet<>());
        assertThat(shoppingOrder.getOrders()).doesNotContain(productOrderBack);
        assertThat(productOrderBack.getOverallOrder()).isNull();
    }

    @Test
    void shipmentTest() throws Exception {
        ShoppingOrder shoppingOrder = getShoppingOrderRandomSampleGenerator();
        Shipment shipmentBack = getShipmentRandomSampleGenerator();

        shoppingOrder.setShipment(shipmentBack);
        assertThat(shoppingOrder.getShipment()).isEqualTo(shipmentBack);
        assertThat(shipmentBack.getOrder()).isEqualTo(shoppingOrder);

        shoppingOrder.shipment(null);
        assertThat(shoppingOrder.getShipment()).isNull();
        assertThat(shipmentBack.getOrder()).isNull();
    }
}
