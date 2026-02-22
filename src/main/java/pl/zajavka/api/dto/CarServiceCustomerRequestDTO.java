package pl.zajavka.api.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarServiceCustomerRequestDTO {

    private String existingCustomerEmail;

    private String customerName;
    private String customerSurname;
    private String customerPhone;
    private String customerEmail;
    private String customerAddressCountry;
    private String customerAddressCity;
    private String customerAddressPostalCode;
    private String customerAddressStreet;

    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN must be exactly 17 characters containing only capital letters (excluding I, O, Q) and digits")
    private String existingCarVin;
    private String existingCarBrand;
    private String existingCarModel;
    private Integer existingCarYear;

    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN must be exactly 17 characters containing only capital letters (excluding I, O, Q) and digits")
    private String carVin;
    private String carBrand;
    private String carModel;
    private Integer carYear;

    private String customerComment;

    public static CarServiceCustomerRequestDTO buildDefault() {
        return CarServiceCustomerRequestDTO.builder()
            .existingCustomerEmail("alf.samoch@gmail.com")
            .existingCarVin("1FT7X2B60FEA74019")
            .customerComment("Olej cieknie mi na stopy")
            .build();
    }

    public boolean isNewCarCandidate() {
        return Objects.isNull(getExistingCustomerEmail())
            || getExistingCustomerEmail().isBlank()
            || Objects.isNull(getExistingCarVin())
            || getExistingCarVin().isBlank();

    }
}
