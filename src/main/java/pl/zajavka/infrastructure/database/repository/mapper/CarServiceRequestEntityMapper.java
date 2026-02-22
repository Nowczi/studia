package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.CarServiceRequest;
import pl.zajavka.domain.CarToService;
import pl.zajavka.domain.Mechanic;
import pl.zajavka.domain.Part;
import pl.zajavka.domain.Service;
import pl.zajavka.domain.ServiceMechanic;
import pl.zajavka.domain.ServicePart;
import pl.zajavka.infrastructure.database.entity.CarServiceRequestEntity;
import pl.zajavka.infrastructure.database.entity.ServiceMechanicEntity;
import pl.zajavka.infrastructure.database.entity.ServicePartEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarServiceRequestEntityMapper {

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "car", ignore = true)
    @Mapping(target = "serviceMechanics", ignore = true)
    @Mapping(target = "serviceParts", ignore = true)
    CarServiceRequest mapFromEntity(CarServiceRequestEntity entity);

    default CarServiceRequest mapFromEntityWithCar(CarServiceRequestEntity entity) {
        return mapFromEntity(entity)
            .withCar(CarToService.builder()
                .vin(entity.getCar().getVin())
                .build());
    }
    
    /**
     * Maps entity with all details including service mechanics and parts.
     * This is needed for the mechanic work page to show completed work.
     */
    default CarServiceRequest mapFromEntityWithDetails(CarServiceRequestEntity entity) {
        // Map service mechanics
        Set<ServiceMechanic> serviceMechanics = entity.getServiceMechanics().stream()
            .map(sm -> ServiceMechanic.builder()
                .serviceMechanicId(sm.getServiceMechanicId())
                .hours(sm.getHours())
                .comment(sm.getComment())
                .mechanic(sm.getMechanic() != null ? Mechanic.builder()
                    .name(sm.getMechanic().getName())
                    .surname(sm.getMechanic().getSurname())
                    .pesel(sm.getMechanic().getPesel())
                    .build() : null)
                .service(sm.getService() != null ? Service.builder()
                    .serviceCode(sm.getService().getServiceCode())
                    .description(sm.getService().getDescription())
                    .price(sm.getService().getPrice())
                    .build() : null)
                .build())
            .collect(Collectors.toSet());
        
        // Map service parts
        Set<ServicePart> serviceParts = entity.getServiceParts().stream()
            .map(sp -> ServicePart.builder()
                .servicePartId(sp.getServicePartId())
                .quantity(sp.getQuantity())
                .part(sp.getPart() != null ? Part.builder()
                    .serialNumber(sp.getPart().getSerialNumber())
                    .description(sp.getPart().getDescription())
                    .price(sp.getPart().getPrice())
                    .build() : null)
                .build())
            .collect(Collectors.toSet());
        
        return CarServiceRequest.builder()
            .carServiceRequestId(entity.getCarServiceRequestId())
            .carServiceRequestNumber(entity.getCarServiceRequestNumber())
            .receivedDateTime(entity.getReceivedDateTime())
            .completedDateTime(entity.getCompletedDateTime())
            .customerComment(entity.getCustomerComment())
            .car(CarToService.builder()
                .vin(entity.getCar() != null ? entity.getCar().getVin() : null)
                .build())
            .serviceMechanics(serviceMechanics)
            .serviceParts(serviceParts)
            .build();
    }

    @Mapping(target = "customer.address", ignore = true)
    @Mapping(target = "customer.carServiceRequests", ignore = true)
    CarServiceRequestEntity mapToEntity(CarServiceRequest request);
}
