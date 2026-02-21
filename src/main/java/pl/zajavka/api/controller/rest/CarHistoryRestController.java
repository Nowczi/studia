package pl.zajavka.api.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.CarHistoryDTO;
import pl.zajavka.api.dto.CarToServiceDTO;
import pl.zajavka.api.dto.mapper.CarMapper;
import pl.zajavka.business.CarService;
import pl.zajavka.domain.CarHistory;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping(CarHistoryRestController.API_CAR)
public class CarHistoryRestController {

    public static final String API_CAR = "/api/car";
    public static final String CAR_VIN_HISTORY = "/{carVin}/history";

    private final CarService carService;
    private final CarMapper carMapper;

    @GetMapping(value = CAR_VIN_HISTORY)
    public ResponseEntity<CarHistoryDTO> carHistory(
            @PathVariable String carVin
    ) {
        if (Objects.isNull(carVin)){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok(carMapper.map(carService.findCarHistoryByVin(carVin)));

    }

}
