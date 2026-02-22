package pl.nowakowski.business.dao;

import pl.nowakowski.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User save(User user);

    void deleteById(Integer id);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    /**
     * Resets the password for a user.
     *
     * @param userId the ID of the user whose password should be reset
     * @param newPassword the new password (already encoded)
     * @return the updated user
     */
    User resetPassword(Integer userId, String newPassword);
}
