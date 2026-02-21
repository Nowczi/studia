package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.CepikVehicleDTO;
import pl.zajavka.domain.CepikVehicle;

@Mapper(componentModel = "spring")
public interface CepikVehicleMapper {


    CepikVehicleDTO map(CepikVehicle cepikVehicle);
}
