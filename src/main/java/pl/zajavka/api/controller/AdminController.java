package pl.zajavka.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.UsersDTO;
import pl.zajavka.api.dto.mapper.UserMapper;
import pl.zajavka.business.UserManagementService;
import pl.zajavka.domain.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@AllArgsConstructor
public class AdminController {

    public static final String ADMIN = "/admin";
    public static final String ADMIN_CREATE_USER = "/admin/create";
    public static final String ADMIN_DELETE_USER = "/admin/delete";
    public static final String ADMIN_TOGGLE_USER = "/admin/toggle";

    private final UserManagementService userManagementService;
    private final UserMapper userMapper;

    @GetMapping(value = ADMIN)
    public ModelAndView adminPortal() {
        Map<String, ?> model = prepareAdminData();
        return new ModelAndView("admin_portal", model);
    }

    private Map<String, ?> prepareAdminData() {
        List<UserDTO> users = userManagementService.findAllUsers().stream()
                .map(userMapper::map)
                .toList();

        Set<String> availableRoles = userManagementService.getAvailableRoles();

        return Map.of(
                "usersDTO", UsersDTO.builder().users(users).build(),
                "availableRoles", availableRoles,
                "newUserDTO", UserDTO.buildDefault()
        );
    }

    @PostMapping(value = ADMIN_CREATE_USER)
    public String createUser(
            @Valid @ModelAttribute("newUserDTO") UserDTO userDTO,
            @RequestParam("selectedRole") String selectedRole,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("pesel") String pesel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAllAttributes(prepareAdminData());
            model.addAttribute("errorMessage", "Validation errors occurred");
            return "admin_portal";
        }

        if (selectedRole == null || selectedRole.isEmpty()) {
            model.addAllAttributes(prepareAdminData());
            model.addAttribute("errorMessage", "Role must be selected");
            return "admin_portal";
        }

        try {
            User user = userMapper.map(userDTO);
            userManagementService.createUser(user, selectedRole, name, surname, pesel);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully");
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAllAttributes(prepareAdminData());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin_portal";
        }
    }

    @PostMapping(value = ADMIN_DELETE_USER)
    public String deleteUser(
            @RequestParam("userId") Integer userId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userManagementService.deleteUser(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping(value = ADMIN_TOGGLE_USER)
    public String toggleUserActive(
            @RequestParam("userId") Integer userId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userManagementService.toggleUserActive(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot toggle user: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
