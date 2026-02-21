package pl.zajavka.api.dto.mapper;

import org.springframework.stereotype.Component;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO map(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .active(user.getActive())
                .roles(user.getRoles())
                .build();
    }

    public User map(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        return User.builder()
                .id(dto.getId())
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .active(dto.getActive())
                .roles(dto.getRoles())
                .build();
    }

    public User mapFromEntity(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        // Handle null ID safely
        Integer id = null;
        if (entity.getId() != 0) {  // Check if ID was assigned (not default 0)
            id = entity.getId();
        }

        return User.builder()
                .id(id)
                .userName(entity.getUserName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .active(entity.getActive())
                .roles(entity.getRoles().stream()
                        .map(RoleEntity::getRole)
                        .collect(Collectors.toSet()))
                .build();
    }

    public UserEntity mapToEntity(User user, Set<RoleEntity> roles) {
        if (user == null) {
            return null;
        }

        // Build entity - ID will be auto-generated if null
        UserEntity.UserEntityBuilder builder = UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .active(user.getActive())
                .roles(roles);

        // Only set ID if it's not null (for updates)
        if (user.getId() != null) {
            builder.id(user.getId());
        }

        return builder.build();
    }
}