package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.MechanicDTO;
import pl.zajavka.domain.Mechanic;

@Mapper(componentModel = "spring")
public interface MechanicMapper {

    MechanicDTO map(final Mechanic mechanic);
}
