package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.CarPurchaseDTO;
import pl.zajavka.api.dto.CarToBuyDTO;
import pl.zajavka.domain.CarPurchaseRequest;
import pl.zajavka.domain.CarToBuy;

@Mapper(componentModel = "spring")
public interface CarPurchaseMapper {

     CarPurchaseRequest map(final CarPurchaseDTO dto);

    CarToBuyDTO map(CarToBuy car);
}
