package info4.gl.dm.coopcycle.service.mapper;

import info4.gl.dm.coopcycle.domain.DeliveryPerson;
import info4.gl.dm.coopcycle.service.dto.DeliveryPersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryPerson} and its DTO {@link DeliveryPersonDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeliveryPersonMapper extends EntityMapper<DeliveryPersonDTO, DeliveryPerson> {}
