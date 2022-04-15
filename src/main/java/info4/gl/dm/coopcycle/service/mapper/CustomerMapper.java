package info4.gl.dm.coopcycle.service.mapper;

import info4.gl.dm.coopcycle.domain.Customer;
import info4.gl.dm.coopcycle.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {}
