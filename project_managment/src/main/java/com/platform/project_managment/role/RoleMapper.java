package com.platform.project_managment.role;


import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(RoleDTO dto) {
        return Role.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public RoleDTO toDTO(Role role) {
        return RoleDTO.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}
