package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarServiceMechanicProcessingUnitDTO {

    private String mechanicPesel;
    private String carVin;
    private String partSerialNumber;
    private Integer partQuantity;
    private String serviceCode;
    private Integer hours;
    private String mechanicComment;
    private Boolean done;

    public static CarServiceMechanicProcessingUnitDTO buildDefault() {
        return CarServiceMechanicProcessingUnitDTO.builder()
            .partQuantity(9)
            .hours(1)
            .mechanicComment("Uff, tough")
            .done(false)
            .build();
    }
}
