package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Mechanic;
import pl.zajavka.infrastructure.database.entity.MechanicEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MechanicEntityMapper {

    @Mapping(target = "serviceMechanics", ignore = true)
    Mechanic mapFromEntity(MechanicEntity entity);
}
