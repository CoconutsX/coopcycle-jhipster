package info4.gl.dm.coopcycle.service.mapper;

import info4.gl.dm.coopcycle.domain.Order;
import info4.gl.dm.coopcycle.domain.Payment;
import info4.gl.dm.coopcycle.service.dto.OrderDTO;
import info4.gl.dm.coopcycle.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    PaymentDTO toDto(Payment s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
