package com.example.securityjwt.services;

import com.example.securityjwt.models.ERole;
import com.example.securityjwt.models.Role;
import com.example.securityjwt.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findRoleById(Role role) {
        return roleRepository.findById(role.getId()).orElse(null);
    }

    public Role findRoleByName(ERole eRole) {
        return roleRepository.findByName(eRole);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }

    public Role updateRole(Role role) {
        Role roleToUpdate = findRoleById(role);
        if (roleToUpdate != null) {
            roleToUpdate.setName(role.getName());
            return saveRole(roleToUpdate);
        }
        return null;
    }

}
