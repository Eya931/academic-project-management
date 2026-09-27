package com.platform.project_managment.user;

import com.platform.project_managment.role.Role;
import org.springframework.stereotype.Service;


import java.util.stream.Collectors;

@Service
public class UserMapper {

    private final UserRepository userRepository;

    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());

        if (user.getRoles() != null) {
            dto.setRoleIds(
                    user.getRoles().stream()
                            .map(Role::getId)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    public User toUser(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }
    public Supervisor ToSupervisor(User user) {

        Supervisor supervisor = new Supervisor();
        supervisor.setId(user.getId());
        supervisor.setName(user.getName());
        System.out.println(supervisor.getName() );
    return supervisor;
    }

    public User ToUser(Supervisor supervisor) {
        User user = new User();
        return userRepository.getOne(supervisor.getId());
    }
}
