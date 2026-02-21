package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.CarPurchaseRequest;
import pl.zajavka.domain.CarToBuy;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Invoice;
import pl.zajavka.domain.Salesman;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CarPurchaseService {

    private final CarService carService;
    private final SalesmanService salesmanService;
    private final CustomerService customerService;

    public List<CarToBuy> availableCars() {
        return carService.findAvailableCars();
    }

    public List<Salesman> availableSalesmen() {
        return salesmanService.findAvailable();
    }

    @Transactional
    public Invoice purchase(final CarPurchaseRequest request) {
        return existingCustomerEmailExists(request.getExistingCustomerEmail())
            ? processNextTimeToBuyCustomer(request)
            : processFirstTimeToBuyCustomer(request);
    }

    private boolean existingCustomerEmailExists(String email) {
        return Objects.nonNull(email) && !email.isBlank();
    }

    private Invoice processFirstTimeToBuyCustomer(CarPurchaseRequest request) {
        CarToBuy car = carService.findCarToBuy(request.getCarVin());
        Salesman salesman = salesmanService.findSalesman(request.getSalesmanPesel());
        Invoice invoice = buildInvoice(car, salesman);

        Customer customer = buildCustomer(request, invoice);
        customerService.issueInvoice(customer);
        return invoice;
    }

    private Invoice processNextTimeToBuyCustomer(CarPurchaseRequest request) {
        Customer existingCustomer = customerService.findCustomer(request.getExistingCustomerEmail());
        CarToBuy car = carService.findCarToBuy(request.getCarVin());
        Salesman salesman = salesmanService.findSalesman(request.getSalesmanPesel());
        Invoice invoice = buildInvoice(car, salesman);
        Set<Invoice> existingInvoices = existingCustomer.getInvoices();
        existingInvoices.add(invoice);
        customerService.issueInvoice(existingCustomer.withInvoices(existingInvoices));
        return invoice;
    }

    private Customer buildCustomer(CarPurchaseRequest inputData, Invoice invoice) {
        return Customer.builder()
            .name(inputData.getCustomerName())
            .surname(inputData.getCustomerSurname())
            .phone(inputData.getCustomerPhone())
            .email(inputData.getCustomerEmail())
            .address(Address.builder()
                .country(inputData.getCustomerAddressCountry())
                .city(inputData.getCustomerAddressCity())
                .postalCode(inputData.getCustomerAddressPostalCode())
                .address(inputData.getCustomerAddressStreet())
                .build())
            .invoices(Set.of(invoice))
            .build();
    }

    private Invoice buildInvoice(CarToBuy car, Salesman salesman) {
        return Invoice.builder()
            .invoiceNumber(UUID.randomUUID().toString())
            .dateTime(OffsetDateTime.of(2025, 10, 1, 12, 0, 0, 0, ZoneOffset.UTC))
            .car(car)
            .salesman(salesman)
            .build();
    }
}
