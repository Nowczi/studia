package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.business.dao.MechanicDAO;
import pl.zajavka.business.dao.SalesmanDAO;
import pl.zajavka.business.dao.UserDAO;
import pl.zajavka.domain.Mechanic;
import pl.zajavka.domain.Salesman;
import pl.zajavka.domain.User;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.domain.exception.ProcessingException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserManagementService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final MechanicDAO mechanicDAO;
    private final SalesmanDAO salesmanDAO;

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userDAO.findAll();
    }

    @Transactional(readOnly = true)
    public User findUser(String userName) {
        return userDAO.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("Could not find user by username: [%s]".formatted(userName)));
    }

    @Transactional
    public User createUser(User user, Set<String> roles) {
        if (userDAO.existsByUserName(user.getUserName())) {
            throw new ProcessingException("User with username [%s] already exists".formatted(user.getUserName()));
        }
        if (userDAO.existsByEmail(user.getEmail())) {
            throw new ProcessingException("User with email [%s] already exists".formatted(user.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        User userToSave = user
                .withPassword(encodedPassword)
                .withActive(true)
                .withRoles(roles);

        return userDAO.save(userToSave);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        // Find user
        User user = userDAO.findAll().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Prevent deletion of admin user
        if ("admin".equals(user.getUserName())) {
            throw new ProcessingException("Cannot delete the admin user");
        }

        // Unlink user from mechanic if exists
        Optional<Mechanic> mechanic = mechanicDAO.findByUserId(userId);
        mechanic.ifPresent(m -> {
            Mechanic unlinkedMechanic = m.withUserId(null);
            mechanicDAO.save(unlinkedMechanic);
        });

        // Unlink user from salesman if exists
        Optional<Salesman> salesman = salesmanDAO.findByUserId(userId);
        salesman.ifPresent(s -> {
            Salesman unlinkedSalesman = s.withUserId(null);
            salesmanDAO.save(unlinkedSalesman);
        });

        // HARD DELETE: Permanently delete the user from the database
        userDAO.deleteById(userId);
    }

    @Transactional
    public User toggleUserActive(Integer userId) {
        User user = userDAO.findAll().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Prevent deactivation of admin user
        if ("admin".equals(user.getUserName()) && Boolean.TRUE.equals(user.getActive())) {
            throw new ProcessingException("Cannot deactivate the admin user");
        }

        User updatedUser = user.withActive(!user.getActive());
        return userDAO.save(updatedUser);
    }

    public Set<String> getAvailableRoles() {
        return Set.of("SALESMAN", "MECHANIC", "REST_API", "ADMIN");
    }
}