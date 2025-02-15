package com.assignment.user.repositories;

import com.assignment.user.entities.Role;
import com.assignment.user.entities.User;
import com.assignment.user.enums.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT * FROM ROLE r WHERE r.role_name LIKE %:search%", nativeQuery = true)
    List<Role> findAllByRoleNameLike(String search);

    Optional<Role> findByRoleName(RoleEnum name);

    // ✅ Fixed return type (should return Role instead of User)
    Page<Role> findByRoleNameContainingIgnoreCase(String roleName, Pageable pageable);
}