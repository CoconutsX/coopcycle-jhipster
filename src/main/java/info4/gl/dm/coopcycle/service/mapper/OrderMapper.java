package info4.gl.dm.coopcycle.service.mapper;

import info4.gl.dm.coopcycle.domain.Cooperative;
import info4.gl.dm.coopcycle.domain.Customer;
import info4.gl.dm.coopcycle.domain.Order;
import info4.gl.dm.coopcycle.service.dto.CooperativeDTO;
import info4.gl.dm.coopcycle.service.dto.CustomerDTO;
import info4.gl.dm.coopcycle.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeId")
    OrderDTO toDto(Order s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("cooperativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativeDTO toDtoCooperativeId(Cooperative cooperative);
}
