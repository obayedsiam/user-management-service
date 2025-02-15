package com.assignment.user.util;

import com.assignment.user.entities.Permission;
import com.assignment.user.entities.Role;
import com.assignment.user.entities.User;
import com.assignment.user.enums.PermissionEnum;
import com.assignment.user.enums.RoleEnum;
import com.assignment.user.repositories.RoleRepository;
import com.assignment.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            Role superAdminRole = new Role();
            superAdminRole.setRoleName(RoleEnum.SUPER_ADMIN);

            Role adminRole = new Role();
            adminRole.setRoleName(RoleEnum.ADMIN);

            Role userRole = new Role();
            userRole.setRoleName(RoleEnum.USER);

            List<Role> roles = List.of(superAdminRole, adminRole, userRole);

            roleRepository.saveAll(roles);
            System.out.println("Super Admin, Admin and User Role created. \n\n");
        }

        if (userRepository.count() == 0) {

            User superAdmin = new User();

            String username = "SuperAdmin";
            String password = "admin123";
            superAdmin.setName(username);
            superAdmin.setPassword(passwordEncoder.encode(password));
            superAdmin.setEmail("super.admin@email.com");
            superAdmin.setRole(roleRepository.findByRoleName(RoleEnum.SUPER_ADMIN).get());
            superAdmin.setPermissionSet(getAllPermission());
            System.out.println("Super Admin User created with following credentials : ");
            System.out.println("Username : "+username);
            System.out.println("Password : "+password);

            userRepository.save(superAdmin);
        }
    }

    public Set<PermissionEnum> getAllPermission() {
        return Arrays.stream(PermissionEnum.values()).collect(Collectors.toSet());
    }
}
