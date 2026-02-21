package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.zajavka.api.dto.CarHistoryDTO;
import pl.zajavka.api.dto.CarToBuyDTO;
import pl.zajavka.api.dto.CarToServiceDTO;
import pl.zajavka.domain.CarHistory;
import pl.zajavka.domain.CarToBuy;
import pl.zajavka.domain.CarToService;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper extends OffsetDateTimeMapper {

    CarToBuyDTO map(final CarToBuy car);

    CarToServiceDTO map(final CarToService car);

    @Mapping(source = "carServiceRequests", target = "carServiceRequests", qualifiedByName = "mapServiceRequests")
    CarHistoryDTO map(CarHistory carHistory);

    @SuppressWarnings("unused")
    @Named("mapServiceRequests")
    default List<CarHistoryDTO.ServiceRequestDTO> mapServiceRequests(
        List<CarHistory.CarServiceRequest> requests
    ) {
        return requests.stream().map(this::mapServiceRequest).toList();
    }

    @Mapping(source = "receivedDateTime", target = "receivedDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    @Mapping(source = "completedDateTime", target = "completedDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    CarHistoryDTO.ServiceRequestDTO mapServiceRequest(CarHistory.CarServiceRequest carServiceRequest);
}
