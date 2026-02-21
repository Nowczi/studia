package pl.zajavka.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.api.dto.CarServiceMechanicProcessingUnitDTO;
import pl.zajavka.api.dto.CarServiceRequestDTO;
import pl.zajavka.api.dto.MechanicDTO;
import pl.zajavka.api.dto.PartDTO;
import pl.zajavka.api.dto.ServiceDTO;
import pl.zajavka.api.dto.mapper.CarServiceRequestMapper;
import pl.zajavka.api.dto.mapper.MechanicMapper;
import pl.zajavka.api.dto.mapper.PartMapper;
import pl.zajavka.api.dto.mapper.ServiceMapper;
import pl.zajavka.business.CarServiceProcessingService;
import pl.zajavka.business.CarServiceRequestService;
import pl.zajavka.business.PartCatalogService;
import pl.zajavka.business.ServiceCatalogService;
import pl.zajavka.business.dao.MechanicDAO;
import pl.zajavka.domain.CarServiceProcessingRequest;
import pl.zajavka.domain.Mechanic;
import pl.zajavka.domain.Part;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class MechanicController {

    public static final String MECHANIC = "/mechanic";
    public static final String MECHANIC_WORK_UNIT = "/mechanic/workUnit";

    private final CarServiceProcessingService carServiceProcessingService;
    private final CarServiceRequestService carServiceRequestService;
    private final PartCatalogService partCatalogService;
    private final ServiceCatalogService serviceCatalogService;
    private final CarServiceRequestMapper carServiceRequestMapper;
    private final MechanicMapper mechanicMapper;
    private final PartMapper partMapper;
    private final ServiceMapper serviceMapper;
    private final UserRepository userRepository;
    private final MechanicDAO mechanicDAO;

    @GetMapping(value = MECHANIC)
    public ModelAndView mechanicCheckPage() {
        Map<String, Object> data = new HashMap<>(prepareNecessaryData());
        
        // Get current logged-in user's name
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUserName(userName);
        
        if (user != null) {
            // Try to find mechanic profile for the user
            var mechanicOptional = mechanicDAO.findByUserId(user.getId());
            if (mechanicOptional.isPresent()) {
                Mechanic mechanic = mechanicOptional.get();
                data.put("mechanicName", mechanic.getName());
                data.put("mechanicSurname", mechanic.getSurname());
            } else {
                // User is not a mechanic (e.g., Admin), use username or default greeting
                // Capitalize first letter of username for better display
                String displayName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
                data.put("mechanicName", displayName);
                data.put("mechanicSurname", "");
            }
        } else {
            // Fallback if user not found
            data.put("mechanicName", "User");
            data.put("mechanicSurname", "");
        }
        
        return new ModelAndView("mechanic_service", data);
    }

    private Map<String, ?> prepareNecessaryData() {
        var availableServiceRequests = getAvailableServiceRequests();
        var availableCarVins = availableServiceRequests.stream().map(CarServiceRequestDTO::getCarVin).toList();
        var availableMechanics = getAvailableMechanics();
        var availableMechanicPesels = availableMechanics.stream().map(MechanicDTO::getPesel).toList();
        var parts = findParts();
        var services = findServices();
        var partSerialNumbers = preparePartSerialNumbers(parts);
        var serviceCodes = services.stream().map(ServiceDTO::getServiceCode).toList();

        return Map.of(
            "availableServiceRequestDTOs", availableServiceRequests,
            "availableCarVins", availableCarVins,
            "availableMechanicDTOs", availableMechanics,
            "availableMechanicPesels", availableMechanicPesels,
            "partDTOs", parts,
            "partSerialNumbers", partSerialNumbers,
            "serviceDTOs", services,
            "serviceCodes", serviceCodes,
            "carServiceProcessDTO", CarServiceMechanicProcessingUnitDTO.buildDefault()
        );
    }

    @PostMapping(value = MECHANIC_WORK_UNIT)
    public String mechanicWorkUnit(
        @Valid @ModelAttribute("carServiceRequestProcessDTO") CarServiceMechanicProcessingUnitDTO dto,
        ModelMap modelMap
    ) {

        CarServiceProcessingRequest request = carServiceRequestMapper.map(dto);
        carServiceProcessingService.process(request);
        if (dto.getDone()) {
            return "mechanic_service_done";
        } else {
            modelMap.addAllAttributes(prepareNecessaryData());
            return "redirect:/mechanic";
        }
    }

    private List<CarServiceRequestDTO> getAvailableServiceRequests() {
        return carServiceRequestService.availableServiceRequests().stream()
            .map(carServiceRequestMapper::map)
            .toList();
    }

    private List<MechanicDTO> getAvailableMechanics() {
        return carServiceRequestService.availableMechanics().stream()
            .map(mechanicMapper::map)
            .toList();
    }

    private List<PartDTO> findParts() {
        return partCatalogService.findAll().stream()
            .map(partMapper::map)
            .toList();
    }

    private List<ServiceDTO> findServices() {
        return serviceCatalogService.findAll().stream()
            .map(serviceMapper::map)
            .toList();
    }

    private List<String> preparePartSerialNumbers(List<PartDTO> parts) {
        List<String> partSerialNumbers = new ArrayList<>(parts.stream()
            .map(PartDTO::getSerialNumber)
            .toList());
        partSerialNumbers.add(Part.NONE);
        return partSerialNumbers;
    }
}
