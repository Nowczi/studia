package pl.zajavka.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.zajavka.api.dto.CarToBuyDTO;
import pl.zajavka.api.dto.mapper.CarMapper;
import pl.zajavka.business.CarPurchaseService;
import pl.zajavka.business.CarService;

@Controller
@RequiredArgsConstructor
public class SalesmanController {

    private static final String SALESMAN = "/salesman";
    private static final String SALESMAN_CAR_ADD = "/salesman/car/add";

    private final CarPurchaseService carPurchaseService;
    private final CarService carService;
    private final CarMapper carMapper;

    @GetMapping(value = SALESMAN)
    public String homePage(Model model) {
        var availableCars = carPurchaseService.availableCars().stream()
            .map(carMapper::map)
            .toList();

        model.addAttribute("availableCarDTOs", availableCars);

        return "salesman_portal";
    }
    
    @GetMapping(value = SALESMAN_CAR_ADD)
    public String addCarPage(Model model) {
        model.addAttribute("carToBuyDTO", CarToBuyDTO.builder().build());
        return "add_car";
    }
    
    @PostMapping(value = SALESMAN_CAR_ADD)
    public String addCar(
        @Valid @ModelAttribute("carToBuyDTO") CarToBuyDTO carToBuyDTO,
        RedirectAttributes redirectAttributes
    ) {
        // Validate VIN format
        String vin = carToBuyDTO.getVin();
        if (vin == null || !vin.matches("^[A-HJ-NPR-Z0-9]{17}$")) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Invalid VIN format! VIN must be exactly 17 characters containing only capital letters (excluding I, O, Q) and digits. " +
                "Please correct the VIN and try again. Example of valid VIN: 1FT7X2B60FEA74019");
            return "redirect:/salesman/car/add";
        }

        try {
            // Check if car with same VIN already exists
            var existingCar = carService.findOptionalCarToBuy(carToBuyDTO.getVin());
            if (existingCar.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "A car with VIN " + carToBuyDTO.getVin() + " already exists in the inventory.");
                return "redirect:/salesman/car/add";
            }
            
            // Save the new car
            carService.saveCarToBuy(carMapper.map(carToBuyDTO));
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Car " + carToBuyDTO.getBrand() + " " + carToBuyDTO.getModel() + 
                " (VIN: " + carToBuyDTO.getVin() + ") has been successfully added to the inventory.");
            
            return "redirect:/salesman";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error adding car: " + e.getMessage());
            return "redirect:/salesman/car/add";
        }
    }
}
