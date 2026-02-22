package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.CarHistory;
import pl.zajavka.domain.CarToService;
import pl.zajavka.domain.Part;
import pl.zajavka.infrastructure.database.entity.CarToServiceEntity;
import pl.zajavka.infrastructure.database.entity.ServiceMechanicEntity;
import pl.zajavka.infrastructure.database.entity.ServicePartEntity;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarToServiceEntityMapper {

    @Mapping(target = "carServiceRequests", ignore = true)
    CarToService mapFromEntity(CarToServiceEntity entity);

    default CarHistory mapFromEntity(String vin, CarToServiceEntity entity) {
        return CarHistory.builder()
            .carVin(vin)
            .carServiceRequests(entity.getCarServiceRequests().stream()
                .map(request -> CarHistory.CarServiceRequest.builder()
                    .carServiceRequestNumber(request.getCarServiceRequestNumber())
                    .receivedDateTime(request.getReceivedDateTime())
                    .completedDateTime(request.getCompletedDateTime())
                    .customerComment(request.getCustomerComment())
                    // Map each ServiceMechanicEntity to ServiceWork to show mechanic details
                    // This avoids duplication since each entry represents unique work by a mechanic
                    .serviceWorks(request.getServiceMechanics().stream()
                        .map(sm -> CarHistory.ServiceWork.builder()
                            .serviceCode(sm.getService().getServiceCode())
                            .description(sm.getService().getDescription())
                            .price(sm.getService().getPrice())
                            .mechanicName(sm.getMechanic() != null ? sm.getMechanic().getName() : "Unknown")
                            .mechanicSurname(sm.getMechanic() != null ? sm.getMechanic().getSurname() : "")
                            .hours(sm.getHours())
                            .mechanicComment(sm.getComment())
                            .build())
                        .collect(Collectors.toList()))
                    .parts(request.getServiceParts().stream()
                        .map(ServicePartEntity::getPart)
                        .map(service -> Part.builder()
                            .serialNumber(service.getSerialNumber())
                            .description(service.getDescription())
                            .price(service.getPrice())
                            .build())
                        .collect(Collectors.toList()))
                    .build())
                .collect(Collectors.toList()))
            .build();
    }

    CarToServiceEntity mapToEntity(CarToService car);
}
