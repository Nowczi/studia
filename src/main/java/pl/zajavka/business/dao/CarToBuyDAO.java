package pl.zajavka.business.dao;

import pl.zajavka.domain.CarToBuy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CarToBuyDAO {

    Optional<CarToBuy> findCarToBuyByVin(String vin);

    List<CarToBuy> findAvailable();
    
    CarToBuy saveCarToBuy(CarToBuy carToBuy);

    List<CarToBuy> searchAvailableCars(String brand, String model, Integer yearFrom, Integer yearTo, 
                                        String color, BigDecimal priceFrom, BigDecimal priceTo);
}
