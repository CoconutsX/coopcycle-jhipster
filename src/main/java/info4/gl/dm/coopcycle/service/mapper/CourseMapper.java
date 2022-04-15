package info4.gl.dm.coopcycle.service.mapper;

import info4.gl.dm.coopcycle.domain.Course;
import info4.gl.dm.coopcycle.domain.DeliveryPerson;
import info4.gl.dm.coopcycle.domain.Order;
import info4.gl.dm.coopcycle.service.dto.CourseDTO;
import info4.gl.dm.coopcycle.service.dto.DeliveryPersonDTO;
import info4.gl.dm.coopcycle.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    @Mapping(target = "deliveryPerson", source = "deliveryPerson", qualifiedByName = "deliveryPersonId")
    CourseDTO toDto(Course s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    @Named("deliveryPersonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeliveryPersonDTO toDtoDeliveryPersonId(DeliveryPerson deliveryPerson);
}
