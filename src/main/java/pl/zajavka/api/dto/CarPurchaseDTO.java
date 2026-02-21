package pl.zajavka.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.*;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarPurchaseDTO {

    @Email
    private String existingCustomerEmail;

    private String customerName;
    private String customerSurname;
    @Size
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String customerPhone;
    @Email
    private String customerEmail;
    private String customerAddressCountry;
    private String customerAddressCity;
    private String customerAddressPostalCode;
    private String customerAddressStreet;

    private String carVin;
    private String salesmanPesel;

    public static CarPurchaseDTO buildDefaultData() {
        return CarPurchaseDTO.builder()
            .customerName("Alfred")
            .customerSurname("Samochodowy")
            .customerPhone("+48 754 552 234")
            .customerEmail("alf.samoch@gmail.com")
            .customerAddressCountry("Polska")
            .customerAddressCity("Wrocław")
            .customerAddressPostalCode("50-001")
            .customerAddressStreet("Bokserska 15")
            .build();
    }

    public Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        ofNullable(customerName).ifPresent(value -> result.put("customerName", value));
        ofNullable(customerSurname).ifPresent(value -> result.put("customerSurname", value));
        ofNullable(customerPhone).ifPresent(value -> result.put("customerPhone", value));
        ofNullable(customerEmail).ifPresent(value -> result.put("customerEmail", value));
        ofNullable(existingCustomerEmail).ifPresent(value -> result.put("existingCustomerEmail", value));
        ofNullable(customerAddressCountry).ifPresent(value -> result.put("customerAddressCountry", value));
        ofNullable(customerAddressCity).ifPresent(value -> result.put("customerAddressCity", value));
        ofNullable(customerAddressPostalCode).ifPresent(value -> result.put("customerAddressPostalCode", value));
        ofNullable(customerAddressStreet).ifPresent(value -> result.put("customerAddressStreet", value));
        ofNullable(carVin).ifPresent(value -> result.put("carVin", value));
        ofNullable(salesmanPesel).ifPresent(value -> result.put("salesmanPesel", value));
        return result;
    }
}
