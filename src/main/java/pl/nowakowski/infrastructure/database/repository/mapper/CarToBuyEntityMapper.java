package pl.nowakowski.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.nowakowski.domain.CarToBuy;
import pl.nowakowski.infrastructure.database.entity.CarToBuyEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarToBuyEntityMapper {

    @Mapping(target = "invoice", ignore = true)
    CarToBuy mapFromEntity(CarToBuyEntity entity);

    // REMOVED: @Mapping(target = "carToBuyId", ignore = true)
    // The carToBuyId must be preserved for updates to work correctly.
    // When carToBuyId is null, Hibernate performs INSERT (new record).
    // When carToBuyId is set, Hibernate performs UPDATE (existing record).
    @Mapping(target = "invoice", ignore = true)      // Invoice is set separately
    CarToBuyEntity mapToEntity(CarToBuy car);
}
