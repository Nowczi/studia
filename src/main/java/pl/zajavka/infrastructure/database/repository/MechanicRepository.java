package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.MechanicDAO;
import pl.zajavka.domain.Mechanic;
import pl.zajavka.infrastructure.database.repository.jpa.MechanicJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.MechanicEntityMapper;

import java.util.List;
import java.util.Optional;


@Repository
@AllArgsConstructor
public class MechanicRepository implements MechanicDAO {

    private final MechanicJpaRepository mechanicJpaRepository;
    private final MechanicEntityMapper mechanicEntityMapper;

    @Override
    public List<Mechanic> findAvailable() {
        return mechanicJpaRepository.findAll().stream()
            .map(mechanicEntityMapper::mapFromEntity)
            .toList();
    }

    @Override
    public Optional<Mechanic> findByPesel(String pesel) {
        return mechanicJpaRepository.findByPesel(pesel)
            .map(mechanicEntityMapper::mapFromEntity);
    }
}
