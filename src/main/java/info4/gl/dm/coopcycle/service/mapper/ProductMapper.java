package info4.gl.dm.coopcycle.service.mapper;

import info4.gl.dm.coopcycle.domain.Cooperative;
import info4.gl.dm.coopcycle.domain.Order;
import info4.gl.dm.coopcycle.domain.Product;
import info4.gl.dm.coopcycle.service.dto.CooperativeDTO;
import info4.gl.dm.coopcycle.service.dto.OrderDTO;
import info4.gl.dm.coopcycle.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    ProductDTO toDto(Product s);

    @Named("cooperativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativeDTO toDtoCooperativeId(Cooperative cooperative);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
