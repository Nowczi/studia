package pl.zajavka.business.dao;

import pl.zajavka.domain.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceDAO {

    Optional<Service> findByServiceCode(String serviceCode);

    List<Service> findAll();
}
