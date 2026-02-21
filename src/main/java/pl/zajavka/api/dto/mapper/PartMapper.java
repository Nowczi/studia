package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.PartDTO;
import pl.zajavka.domain.Part;

@Mapper(componentModel = "spring")
public interface PartMapper {

    PartDTO map(Part part);
}
