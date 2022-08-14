package com.example.securityjwt;

import com.example.securityjwt.models.Role;
import com.example.securityjwt.models.ERole;
import com.example.securityjwt.models.User;
import com.example.securityjwt.repositories.RoleRepository;
import com.example.securityjwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SecurityJwtApplication implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        roleRepository.saveAll(
                List.of(
                        new Role(ERole.ROLE_ADMIN),
                        new Role(ERole.ROLE_USER),
                        new Role(ERole.ROLE_GUEST)
                )
        );
//        userRepository.saveAll(
//                List.of(
//                        new User("admin", "admin","admin", "admin@admin.com","admin")
//                )
//        );
    }
}
