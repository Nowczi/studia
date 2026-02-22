package pl.zajavka.api.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarHistoryDTO {

    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN must be exactly 17 characters containing only capital letters (excluding I, O, Q) and digits")
    private String carVin;
    private List<ServiceRequestDTO> carServiceRequests;

    public static CarHistoryDTO buildDefault() {
        return CarHistoryDTO.builder()
            .carVin("empty")
            .carServiceRequests(Collections.emptyList())
            .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceRequestDTO {
        private String carServiceRequestNumber;
        private String receivedDateTime;
        private String completedDateTime;
        private String customerComment;
        private List<ServiceDTO> services;
        private List<PartDTO> parts;
    }
}
