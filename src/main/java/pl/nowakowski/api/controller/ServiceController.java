package pl.nowakowski.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.nowakowski.api.dto.CarServiceCustomerRequestDTO;
import pl.nowakowski.api.dto.mapper.CarServiceRequestMapper;
import pl.nowakowski.business.CarServiceRequestService;
import pl.nowakowski.domain.CarServiceRequest;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

@Controller
@AllArgsConstructor
public class ServiceController {

    private static final String SERVICE_NEW = "/service/new";
    private static final String SERVICE_REQUEST = "/service/request";
    
    // VIN pattern: 17 characters, capital letters (excluding I, O, Q) and digits
    private static final Pattern VIN_PATTERN = Pattern.compile("^[A-HJ-NPR-Z0-9]{17}$");

    private final CarServiceRequestService carServiceRequestService;
    private final CarServiceRequestMapper carServiceRequestMapper;

    @GetMapping(value = SERVICE_NEW)
    public ModelAndView carServicePage() {
        Map<String, ?> model = Map.of(
            "carServiceRequestDTO", CarServiceCustomerRequestDTO.buildDefault()
        );
        return new ModelAndView("car_service_request", model);
    }

    @PostMapping(value = SERVICE_REQUEST)
    public String makeServiceRequest(
        @Valid @ModelAttribute("carServiceRequestDTO") CarServiceCustomerRequestDTO carServiceCustomerRequestDTO,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        // Validate VIN based on whether it's a new or existing customer
        String vin;
        if (carServiceCustomerRequestDTO.isNewCarCandidate()) {
            // New customer - validate carVin
            vin = carServiceCustomerRequestDTO.getCarVin();
            if (vin == null || vin.isBlank()) {
                redirectAttributes.addFlashAttribute("errorMessage", "VIN is required for new customers");
                return "redirect:/service/new";
            }
            if (!VIN_PATTERN.matcher(vin).matches()) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "VIN must be exactly 17 characters containing only capital letters (excluding I, O, Q) and digits");
                return "redirect:/service/new";
            }
        } else {
            // Existing customer - validate existingCarVin
            vin = carServiceCustomerRequestDTO.getExistingCarVin();
            if (vin == null || vin.isBlank()) {
                redirectAttributes.addFlashAttribute("errorMessage", "VIN is required for existing customers");
                return "redirect:/service/new";
            }
            if (!VIN_PATTERN.matcher(vin).matches()) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "VIN must be exactly 17 characters containing only capital letters (excluding I, O, Q) and digits");
                return "redirect:/service/new";
            }
        }
        
        try {
            // Generate service request number before saving
            String serviceRequestNumber = generateCarServiceRequestNumber();
            
            CarServiceRequest serviceRequest = carServiceRequestMapper.map(carServiceCustomerRequestDTO);
            carServiceRequestService.makeServiceRequest(serviceRequest);
            
            // Add service request number to redirect attributes
            redirectAttributes.addFlashAttribute("serviceRequestNumber", serviceRequestNumber);
            
            return "redirect:/service/request/done";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error creating service request: " + e.getMessage());
            return "redirect:/service/new";
        }
    }
    
    @GetMapping(value = "/service/request/done")
    public ModelAndView serviceRequestDonePage() {
        return new ModelAndView("car_service_request_done");
    }
    
    private String generateCarServiceRequestNumber() {
        OffsetDateTime when = OffsetDateTime.now(ZoneId.of("Europe/Warsaw"));
        return "%s.%s.%s-%s.%s.%s.%s".formatted(
            when.getYear(),
            when.getMonthValue(),
            when.getDayOfMonth(),
            when.getHour(),
            when.getMinute(),
            when.getSecond(),
            randomInt(10, 100)
        );
    }
    
    @SuppressWarnings("SameParameterValue")
    private int randomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }
}
