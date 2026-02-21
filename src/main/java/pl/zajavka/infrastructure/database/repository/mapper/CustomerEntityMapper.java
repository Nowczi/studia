package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.CarServiceRequest;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Invoice;
import pl.zajavka.infrastructure.database.entity.CarServiceRequestEntity;
import pl.zajavka.infrastructure.database.entity.CustomerEntity;
import pl.zajavka.infrastructure.database.entity.InvoiceEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerEntityMapper {

    @Mapping(target = "address.customer", ignore = true)
    @Mapping(source = "carServiceRequests", target = "carServiceRequests", qualifiedByName = "mapCarServiceRequests")
    @Mapping(source = "invoices", target = "invoices", qualifiedByName = "mapInvoices")
    Customer mapFromEntity(CustomerEntity entity);

    @Named("mapInvoices")
    @SuppressWarnings("unused")
    default Set<Invoice> mapInvoices(Set<InvoiceEntity> invoiceEntities) {
        if (invoiceEntities == null) {
            // Return mutable HashSet instead of immutable Collections.emptySet()
            return new HashSet<>();
        }
        return invoiceEntities.stream().map(this::mapFromEntity).collect(Collectors.toSet());
    }

    @Named("mapCarServiceRequests")
    @SuppressWarnings("unused")
    default Set<CarServiceRequest> mapCarServiceRequests(Set<CarServiceRequestEntity> entities) {
        if (entities == null) {
            // Return mutable HashSet instead of immutable Collections.emptySet()
            return new HashSet<>();
        }
        return entities.stream().map(this::mapFromEntity).collect(Collectors.toSet());
    }

    @Mapping(target = "car", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "serviceMechanics", ignore = true)
    @Mapping(target = "serviceParts", ignore = true)
    CarServiceRequest mapFromEntity(CarServiceRequestEntity entity);

    @Mapping(target = "car", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "salesman", ignore = true)
    Invoice mapFromEntity(InvoiceEntity entity);

    // When mapping to entity, ignore collections and ensure address is mapped
    @Mapping(target = "carServiceRequests", ignore = true)
    @Mapping(target = "invoices", ignore = true)
    @Mapping(target = "address.customer", ignore = true)
    CustomerEntity mapToEntity(Customer customer);
}
