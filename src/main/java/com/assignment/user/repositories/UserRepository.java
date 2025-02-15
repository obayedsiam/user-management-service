package com.assignment.user.repositories;

import com.assignment.user.entities.User;
import com.assignment.user.enums.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    @Query("""
                SELECT u FROM User u 
                WHERE (:search IS NULL OR :search = '' 
                    OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) 
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
                AND (:userType IS NULL OR :userType = '' OR u.role.roleName = :roleEnum)
                AND (:adminId IS NULL OR u.createdBy.userId = :adminId)
            """)
    Page<User> searchUsers(
            @Param("search") String search,
            @Param("userType") String userType,
            @Param("roleEnum") RoleEnum roleEnum,
            @Param("adminId") Long adminId,
            Pageable pageable
    );

    Page<User> findAll(Pageable pageable);
}
