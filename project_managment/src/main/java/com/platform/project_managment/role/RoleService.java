package com.platform.project_managment.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Integer id) {
        return roleRepository.findById(id);
    }

    public Role createRoleFromDTO(RoleDTO dto) {
        Role role = roleMapper.toEntity(dto);
        return roleRepository.save(role);
    }

    public Role updateRole(Integer id, RoleDTO updatedDto) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setName(updatedDto.getName());
                    role.setDescription(updatedDto.getDescription());
                    return roleRepository.save(role);
                })
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public void deleteRole(Integer id) {
        roleRepository.deleteById(id);
    }
}
