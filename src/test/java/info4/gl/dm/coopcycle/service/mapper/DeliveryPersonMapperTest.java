package info4.gl.dm.coopcycle.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryPersonMapperTest {

    private DeliveryPersonMapper deliveryPersonMapper;

    @BeforeEach
    public void setUp() {
        deliveryPersonMapper = new DeliveryPersonMapperImpl();
    }
}
