package info4.gl.dm.coopcycle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import info4.gl.dm.coopcycle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryPersonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryPerson.class);
        DeliveryPerson deliveryPerson1 = new DeliveryPerson();
        deliveryPerson1.setId(1L);
        DeliveryPerson deliveryPerson2 = new DeliveryPerson();
        deliveryPerson2.setId(deliveryPerson1.getId());
        assertThat(deliveryPerson1).isEqualTo(deliveryPerson2);
        deliveryPerson2.setId(2L);
        assertThat(deliveryPerson1).isNotEqualTo(deliveryPerson2);
        deliveryPerson1.setId(null);
        assertThat(deliveryPerson1).isNotEqualTo(deliveryPerson2);
    }
}
