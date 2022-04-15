package info4.gl.dm.coopcycle.service.mapper;

import info4.gl.dm.coopcycle.domain.Cooperative;
import info4.gl.dm.coopcycle.service.dto.CooperativeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cooperative} and its DTO {@link CooperativeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CooperativeMapper extends EntityMapper<CooperativeDTO, Cooperative> {}
