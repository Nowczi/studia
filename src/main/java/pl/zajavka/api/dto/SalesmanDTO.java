package pl.zajavka.api.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesmanDTO {

    String name;
    String surname;

    @Pattern(regexp = "^[0-9]{2}([02468]1|[13579][012])(0[1-9]|1[0-9]|2[0-9]|3[01])[0-9]{5}$", message = "PESEL must be a valid 11-digit Polish identification number")
    String pesel;
}
