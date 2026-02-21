package pl.zajavka.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.zajavka.api.dto.mapper.CarMapper;
import pl.zajavka.business.CarPurchaseService;

@Controller
@RequiredArgsConstructor
public class SalesmanController {

    private static final String SALESMAN = "/salesman";

    private final CarPurchaseService carPurchaseService;
    private final CarMapper carMapper;

    @GetMapping(value = SALESMAN)
    public String homePage(Model model) {
        var availableCars = carPurchaseService.availableCars().stream()
                .map(carMapper::map)
                .toList();

        model.addAttribute("availableCarDTOs", availableCars);

        return "salesman_portal";
    }
}