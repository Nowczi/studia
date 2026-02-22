package pl.nowakowski.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.nowakowski.api.dto.CarPurchaseDTO;
import pl.nowakowski.api.dto.CarSearchDTO;
import pl.nowakowski.api.dto.CarToBuyDTO;
import pl.nowakowski.api.dto.mapper.CarMapper;
import pl.nowakowski.api.dto.mapper.CarPurchaseMapper;
import pl.nowakowski.business.CarPurchaseService;
import pl.nowakowski.domain.CarPurchaseRequest;
import pl.nowakowski.domain.Invoice;
import pl.nowakowski.domain.Salesman;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class PurchaseController {

    static final String PURCHASE = "/purchase";

    private final CarPurchaseService carPurchaseService;
    private final CarPurchaseMapper carPurchaseMapper;
    private final CarMapper carMapper;

    @GetMapping(value = PURCHASE)
    public ModelAndView carPurchasePage(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) BigDecimal priceFrom,
            @RequestParam(required = false) BigDecimal priceTo) {
        
        Map<String, ?> modelData = prepareCarPurchaseData(brand, model, yearFrom, yearTo, color, priceFrom, priceTo);
        return new ModelAndView("car_purchase", modelData);
    }

    private Map<String, ?> prepareCarPurchaseData(String brand, String model, Integer yearFrom, Integer yearTo,
                                                   String color, BigDecimal priceFrom, BigDecimal priceTo) {
        
        List<CarToBuyDTO> availableCars;
        
        // Check if any search parameters are provided
        boolean hasSearchParams = brand != null || model != null || yearFrom != null || yearTo != null 
                || color != null || priceFrom != null || priceTo != null;
        
        if (hasSearchParams) {
            // Perform search with filters
            availableCars = carPurchaseService.searchAvailableCars(
                    brand, model, yearFrom, yearTo, color, priceFrom, priceTo)
                .stream()
                .map(carMapper::map)
                .toList();
        } else {
            // Get all available cars
            availableCars = carPurchaseService.availableCars().stream()
                .map(carMapper::map)
                .toList();
        }
        
        var availableCarVins = availableCars.stream()
            .map(CarToBuyDTO::getVin)
            .toList();
        var availableSalesmanPesels = carPurchaseService.availableSalesmen().stream()
            .map(Salesman::getPesel)
            .toList();
        
        return Map.of(
            "availableCarDTOs", availableCars,
            "availableCarVins", availableCarVins,
            "availableSalesmanPesels", availableSalesmanPesels,
            "carPurchaseDTO", CarPurchaseDTO.buildDefaultData(),
            "carSearchDTO", new CarSearchDTO(brand, model, yearFrom, yearTo, color, priceFrom, priceTo)
        );
    }

    @PostMapping(value = PURCHASE)
    public String makePurchase(
        @Valid @ModelAttribute("carPurchaseDTO") CarPurchaseDTO carPurchaseDTO,
        ModelMap model
    ) {
        CarPurchaseRequest request = carPurchaseMapper.map(carPurchaseDTO);
        Invoice invoice = carPurchaseService.purchase(request);

        if (existingCustomerEmailExists(carPurchaseDTO.getExistingCustomerEmail())) {
            model.addAttribute("existingCustomerEmail", carPurchaseDTO.getExistingCustomerEmail());
        } else {
            model.addAttribute("customerName", carPurchaseDTO.getCustomerName());
            model.addAttribute("customerSurname", carPurchaseDTO.getCustomerSurname());
        }

        model.addAttribute("invoiceNumber", invoice.getInvoiceNumber());

        return "car_purchase_done";
    }

    private boolean existingCustomerEmailExists(String email) {
        return Objects.nonNull(email) && !email.isBlank();
    }
}
