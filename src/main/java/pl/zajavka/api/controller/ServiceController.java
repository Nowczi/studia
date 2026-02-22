package pl.zajavka.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.zajavka.api.dto.CarServiceCustomerRequestDTO;
import pl.zajavka.api.dto.mapper.CarServiceRequestMapper;
import pl.zajavka.business.CarServiceRequestService;
import pl.zajavka.domain.CarServiceRequest;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Random;

@Controller
@AllArgsConstructor
public class ServiceController {

    private static final String SERVICE_NEW = "/service/new";
    private static final String SERVICE_REQUEST = "/service/request";

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
        RedirectAttributes redirectAttributes
    ) {
        // Generate service request number before saving
        String serviceRequestNumber = generateCarServiceRequestNumber();
        
        CarServiceRequest serviceRequest = carServiceRequestMapper.map(carServiceCustomerRequestDTO);
        carServiceRequestService.makeServiceRequest(serviceRequest);
        
        // Add service request number to redirect attributes
        redirectAttributes.addFlashAttribute("serviceRequestNumber", serviceRequestNumber);
        
        return "redirect:/service/request/done";
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
