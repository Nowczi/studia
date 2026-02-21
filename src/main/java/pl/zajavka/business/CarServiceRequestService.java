package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.business.dao.CarServiceRequestDAO;
import pl.zajavka.domain.CarServiceRequest;
import pl.zajavka.domain.CarToBuy;
import pl.zajavka.domain.CarToService;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Mechanic;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.domain.exception.ProcessingException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@AllArgsConstructor
public class CarServiceRequestService {

    private final MechanicService mechanicService;
    private final CarService carService;
    private final CustomerService customerService;
    private final CarServiceRequestDAO carServiceRequestDAO;

    public List<Mechanic> availableMechanics() {
        return mechanicService.findAvailable();
    }

    public List<CarServiceRequest> availableServiceRequests() {
        return carServiceRequestDAO.findAvailable();
    }

    @Transactional
    public void makeServiceRequest(CarServiceRequest serviceRequest) {
        if (serviceRequest.getCar().shouldExistsInCarToBuy()) {
            saveServiceRequestForExistingCar(serviceRequest);
        } else {
            saveServiceRequestForNewCar(serviceRequest);
        }
    }

    private void saveServiceRequestForExistingCar(CarServiceRequest request) {
        validate(request.getCar().getVin());

        CarToService car = carService.findCarToService(request.getCar().getVin())
            .orElse(findInCarToBuyAndSaveInCarToService(request.getCar()));
        Customer customer = customerService.findCustomer(request.getCustomer().getEmail());

        CarServiceRequest carServiceRequest = buildCarServiceRequest(request, car, customer);
        Set<CarServiceRequest> existingCarServiceRequests = customer.getCarServiceRequests();
        existingCarServiceRequests.add(carServiceRequest);
        customerService.saveServiceRequest(customer.withCarServiceRequests(existingCarServiceRequests));
    }

    private void saveServiceRequestForNewCar(CarServiceRequest request) {
        validate(request.getCar().getVin());

        CarToService car = carService.saveCarToService(request.getCar());
        Customer customer = customerService.saveCustomer(request.getCustomer());

        CarServiceRequest carServiceRequest = buildCarServiceRequest(request, car, customer);
        Set<CarServiceRequest> existingCarServiceRequests = customer.getCarServiceRequests();
        existingCarServiceRequests.add(carServiceRequest);
        customerService.saveServiceRequest(customer.withCarServiceRequests(existingCarServiceRequests));
    }

    private void validate(String carVin) {
        Set<CarServiceRequest> serviceRequests = carServiceRequestDAO.findActiveServiceRequestsByCarVin(carVin);
        if (serviceRequests.size() == 1) {
            throw new ProcessingException(
                "There should be only one active service request at a time, car vin: [%s]".formatted(carVin)
            );
        }
    }

    private CarToService findInCarToBuyAndSaveInCarToService(CarToService car) {
        CarToBuy carToBuy = carService.findCarToBuy(car.getVin());
        return carService.saveCarToService(carToBuy);
    }

    private CarServiceRequest buildCarServiceRequest(
        CarServiceRequest request,
        CarToService car,
        Customer customer
    ) {
        OffsetDateTime when = OffsetDateTime.of(2027, 1, 10, 10, 2, 10, 0, ZoneOffset.UTC);
        return CarServiceRequest.builder()
            .carServiceRequestNumber(generateCarServiceRequestNumber(when))
            .receivedDateTime(when)
            .customerComment(request.getCustomerComment())
            .customer(customer)
            .car(car)
            .build();
    }

    private String generateCarServiceRequestNumber(OffsetDateTime when) {
        return "%s.%s.%s-%s.%s.%s.%s".formatted(
            when.getYear(),
            when.getMonth().ordinal(),
            when.getDayOfMonth(),
            when.getHour(),
            when.getMinute(),
            when.getSecond(),
            randomInt(10, 100)
        );
    }

    @SuppressWarnings("SameParameterValue")
    private int randomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    @Transactional
    public CarServiceRequest findAnyActiveServiceRequest(String carVin) {
        Set<CarServiceRequest> serviceRequests = carServiceRequestDAO.findActiveServiceRequestsByCarVin(carVin);
        if (serviceRequests.size() != 1) {
            throw new ProcessingException(
                "There should be only one active service request at a time, car vin: [%s]".formatted(carVin));
        }
        return serviceRequests.stream()
            .findAny()
            .orElseThrow(() -> new NotFoundException("Could not find any service requests, car vin: [%s]".formatted(carVin)));
    }
}
