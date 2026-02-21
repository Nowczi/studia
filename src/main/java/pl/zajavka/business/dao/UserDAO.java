package pl.zajavka.business.dao;

import pl.zajavka.domain.User;

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
}