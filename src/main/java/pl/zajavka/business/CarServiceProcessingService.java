package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.business.dao.ServiceRequestProcessingDAO;
import pl.zajavka.domain.CarServiceProcessingRequest;
import pl.zajavka.domain.CarServiceRequest;
import pl.zajavka.domain.Mechanic;
import pl.zajavka.domain.Part;
import pl.zajavka.domain.Service;
import pl.zajavka.domain.ServiceMechanic;
import pl.zajavka.domain.ServicePart;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class CarServiceProcessingService {

    private final MechanicService mechanicService;
    private final CarService carService;
    private final ServiceCatalogService serviceCatalogService;
    private final PartCatalogService partCatalogService;
    private final CarServiceRequestService carServiceRequestService;
    private final ServiceRequestProcessingDAO serviceRequestProcessingDAO;

    @Transactional
    public void process(CarServiceProcessingRequest request) {
        Mechanic mechanic = mechanicService.findMechanic(request.getMechanicPesel());
        carService.findCarToService(request.getCarVin()).orElseThrow();
        CarServiceRequest serviceRequest = carServiceRequestService.findAnyActiveServiceRequest(request.getCarVin());

        Service service = serviceCatalogService.findService(request.getServiceCode());

        ServiceMechanic serviceMechanic = buildServiceMechanic(request, mechanic, serviceRequest, service);

        if (request.getDone()) {
            serviceRequest = serviceRequest.withCompletedDateTime(OffsetDateTime.of(2029, 3, 2, 10, 9, 12, 0, ZoneOffset.UTC));
        }

        if (request.partNotIncluded()) {
            serviceRequestProcessingDAO.process(serviceRequest, serviceMechanic);
        } else {
            Part part = partCatalogService.findPart(request.getPartSerialNumber());
            ServicePart servicePart = buildServicePart(request, serviceRequest, part);
            serviceRequestProcessingDAO.process(serviceRequest, serviceMechanic, servicePart);
        }
    }

    private ServiceMechanic buildServiceMechanic(
        CarServiceProcessingRequest request,
        Mechanic mechanic,
        CarServiceRequest serviceRequest,
        Service service
    ) {
        return ServiceMechanic.builder()
            .hours(request.getHours())
            .comment(request.getComment())
            .carServiceRequest(serviceRequest)
            .mechanic(mechanic)
            .service(service)
            .build();
    }

    private ServicePart buildServicePart(
        CarServiceProcessingRequest request,
        CarServiceRequest serviceRequest,
        Part part
    ) {
        return ServicePart.builder()
            .quantity(request.getPartQuantity())
            .carServiceRequest(serviceRequest)
            .part(part)
            .build();
    }
}
