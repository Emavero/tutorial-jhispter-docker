package com.betterprojectsfaster.tutorial.jhipsterdocker.domain;

import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShipmentTestSamples.*;
import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.betterprojectsfaster.tutorial.jhipsterdocker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shipment.class);
        Shipment shipment1 = getShipmentSample1();
        Shipment shipment2 = new Shipment();
        assertThat(shipment1).isNotEqualTo(shipment2);

        shipment2.setId(shipment1.getId());
        assertThat(shipment1).isEqualTo(shipment2);

        shipment2 = getShipmentSample2();
        assertThat(shipment1).isNotEqualTo(shipment2);
    }

    @Test
    void orderTest() throws Exception {
        Shipment shipment = getShipmentRandomSampleGenerator();
        ShoppingOrder shoppingOrderBack = getShoppingOrderRandomSampleGenerator();

        shipment.setOrder(shoppingOrderBack);
        assertThat(shipment.getOrder()).isEqualTo(shoppingOrderBack);

        shipment.order(null);
        assertThat(shipment.getOrder()).isNull();
    }
}
