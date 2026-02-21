package pl.zajavka.business.dao;

import pl.zajavka.domain.CarServiceRequest;

import java.util.List;
import java.util.Set;

public interface CarServiceRequestDAO {
    List<CarServiceRequest> findAvailable();

    Set<CarServiceRequest> findActiveServiceRequestsByCarVin(String carVin);
}
