package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.CarToBuyDAO;
import pl.zajavka.domain.CarToBuy;
import pl.zajavka.infrastructure.database.repository.jpa.CarToBuyJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CarToBuyEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarToBuyRepository implements CarToBuyDAO {

    private final CarToBuyJpaRepository carToBuyJpaRepository;
    private final CarToBuyEntityMapper carToBuyEntityMapper;

    @Override
    public List<CarToBuy> findAvailable() {
        return carToBuyJpaRepository.findAvailableCars().stream()
            .map(carToBuyEntityMapper::mapFromEntity)
            .toList();
    }

    @Override
    public Optional<CarToBuy> findCarToBuyByVin(String vin) {
        return carToBuyJpaRepository.findByVin(vin)
            .map(carToBuyEntityMapper::mapFromEntity);
    }
}
