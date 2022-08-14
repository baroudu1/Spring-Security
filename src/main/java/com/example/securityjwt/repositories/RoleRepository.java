package com.example.securityjwt.repositories;

import com.example.securityjwt.models.ERole;
import com.example.securityjwt.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName (ERole name);

}
