package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.ServiceDTO;
import pl.zajavka.domain.Service;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceDTO map(Service service);
}
