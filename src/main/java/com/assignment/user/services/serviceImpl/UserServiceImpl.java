package com.assignment.user.services.serviceImpl;


import com.assignment.user.dtos.RegisterUserDto;
import com.assignment.user.entities.Role;
import com.assignment.user.entities.User;
import com.assignment.user.enums.PermissionEnum;
import com.assignment.user.enums.RoleEnum;
import com.assignment.user.repositories.RoleRepository;
import com.assignment.user.repositories.UserRepository;
import com.assignment.user.request.UserRequest;
import com.assignment.user.response.ApiResponse;
import com.assignment.user.response.PaginatedResponse;
import com.assignment.user.response.Response;
import com.assignment.user.response.UserResponseDto;
import com.assignment.user.services.CustomUserDetailsService;
import com.assignment.user.services.UserService;
import com.assignment.user.util.JwtTokenProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService userDetailsService;

    private final JwtTokenProvider tokenProvider;

    public ApiResponse delete(String email) throws BadRequestException {
        String tokenUserEmail = tokenProvider.getEmailFromContext(); // Get token user's email
        User tokenUser = tokenProvider.getUserFromContext(); // Get token user object

        if (email.isEmpty()) {
            throw new BadRequestException("Email must be provided.");
        }

        Optional<User> userToDelete = userRepository.findByEmail(email);
        if (userToDelete.isEmpty()) {
            throw new BadRequestException("User to delete not found.");
        }

        // Check if the token user has the permission to delete the provided user
        if (!checkDeletePermission(tokenUserEmail, userToDelete.get())) {
            throw new BadRequestException("Permission denied.");
        }

        // Proceed to delete (or disable) the user
        userToDelete.get().setEnabled(false);
        userRepository.save(userToDelete.get());

        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setData(null);
        response.setMessage("User successfully deactivated.");
        return response;
    }
    public boolean checkDeletePermission(String tokenUserEmail, User userToDelete) throws BadRequestException {
        // Find the token user
        Optional<User> tokenUserOpt = userRepository.findByEmail(tokenUserEmail);
        if (tokenUserOpt.isEmpty()) {
            throw new BadRequestException("Token user not found");
        }

        User tokenUser = tokenUserOpt.get();

        // Check if the token user is a super admin (can delete any user)
        if (tokenUser.getRole().getRoleName().equals(RoleEnum.SUPER_ADMIN)) {
            return true;
        }

        // Check if the token user is an admin and has DELETE permission
        if (tokenUser.getRole().getRoleName().equals(RoleEnum.ADMIN)) {
            // Check if the token user has the DELETE permission
            if (tokenUser.getPermissionSet().contains(PermissionEnum.DELETE)) {
                // Check if the user to delete was created by the admin (same `createdBy` user)
                if (userToDelete.getCreatedBy() != null && userToDelete.getCreatedBy().getId().equals(tokenUser.getId())) {
                    return true;
                }
            }
        }

        // Check if the token user is a regular user and trying to delete their own account
        if (tokenUser.getRole().getRoleName().equals(RoleEnum.USER)) {
            if (userToDelete.getId().equals(tokenUser.getId())) {
                return true; // User can delete their own account
            }
        }
        return false;
    }

    @Override
    public PaginatedResponse<UserResponseDto> getList(Integer size, Integer page, String sortBy, String sortDirection, RoleEnum userType, String search) throws BadRequestException {

        checkValidation(userType);
        Sort sort = getSort(sortDirection,sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        RoleEnum roleEnum = getRole(userType);
        Long adminId = getAdminUuidIfUserIsAdmin();

        String userTypeName = userType!=null ? userType.getRoleName():null;
        Page<User> userPage = userRepository.searchUsers(search, userTypeName, roleEnum, adminId, pageable);
        Page<UserResponseDto> userResponseDtoPage = userPage.map(UserResponseDto::getResponseDto);
        PaginatedResponse<UserResponseDto> response = getPaginatedResponse(userResponseDtoPage);
        return response;
    }

    public Long getAdminUuidIfUserIsAdmin(){
        User user = tokenProvider.getUserFromContext();
        if(user.getRole().getRoleName().equals(RoleEnum.ADMIN)) return user.getUserId();
        return null;
    }

    public void checkValidation(RoleEnum userType) throws BadRequestException {
        User user = tokenProvider.getUserFromContext();
        if(user==null) throw new BadRequestException("Invalid credentials");
        RoleEnum roleEnum = user.getRole().getRoleName();
        if(roleEnum.equals(RoleEnum.USER)) {
            throw new BadRequestException(roleEnum+" is not authorized to see list of any kind of users");
        }

        if(roleEnum.equals(RoleEnum.ADMIN)){
            if( (userType==null || userType.equals(RoleEnum.SUPER_ADMIN) || userType.equals(RoleEnum.ADMIN))){
                    throw new BadRequestException(roleEnum +" is not authorized see this list");
            }
        }

    }

    public Sort getSort(String sortDirection, String sortBy){
        Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return sort;
    }

    public RoleEnum getRole(RoleEnum userType){
        RoleEnum roleEnum = null;
        if ( userType != null && !userType.getRoleName().trim().isEmpty()) {
            try {
                roleEnum = RoleEnum.valueOf(userType.getRoleName().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid userType: " + userType);
            }
        }
        return roleEnum;
    }

    public PaginatedResponse<UserResponseDto> getPaginatedResponse(Page<UserResponseDto> userPage){
        PaginatedResponse<UserResponseDto> response = new PaginatedResponse<>();
        response.setData(userPage.getContent());
        response.setPageNumber(userPage.getNumber());
        response.setPageSize(userPage.getSize());
        response.setTotalElements(userPage.getTotalElements());
        response.setTotalPages(userPage.getTotalPages());
        response.setLast(userPage.isLast());
        return response;
    }

    public User saveUser(RegisterUserDto input, User creator) {

        Optional<Role> optionalRole = roleRepository.findByRoleName(input.getRoleEnum());
        if (optionalRole.isEmpty()) {
            return null;
        }
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        User user = optionalUser.orElse(new User());

        user.setName(input.getFullName() != null ? input.getFullName() : user.getName());
        user.setEmail(input.getEmail() != null ? input.getEmail() : user.getEmail());


        if (input.getPassword() != null && !input.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(input.getPassword()));
        }
        user.setRole(optionalRole.get());

        if (optionalUser.isEmpty()) {
            Set<PermissionEnum> permissionEnumSet = new HashSet<>();
            if (input.getRoleEnum().equals(RoleEnum.USER)) {
                permissionEnumSet.add(PermissionEnum.READ);
                permissionEnumSet.add(PermissionEnum.UPDATE);
                permissionEnumSet.add(PermissionEnum.DELETE);
            } else {
                permissionEnumSet.addAll(input.getPermissions() != null ? input.getPermissions() : new HashSet<>());
            }
            user.setPermissionSet(permissionEnumSet);
        } else {
            user.setPermissionSet(input.getPermissions() != null ? input.getPermissions() : user.getPermissionSet());
        }

        user.setCreatedBy(creator);
        return userRepository.save(user);
    }

    public ApiResponse createOrUpdateUser(RegisterUserDto input) throws Exception {
        String emailFromToken = tokenProvider.getEmailFromContext();
        if (isAllowedToCreateOrUpdateUser(emailFromToken, input)) {
            Optional<User> creator = userRepository.findByEmail(emailFromToken);
            Optional<User> userGoingToBeCreated = userRepository.findByEmail(input.getEmail());

            saveUser(input, creator.get());

            ApiResponse response = new ApiResponse();
            response.setData(null);
            response.setMessage("User "+ ( userGoingToBeCreated.isEmpty() ? "created " : "updated ") + "successfully");
            response.setSuccess(true);
            return response;
        } else throw new AccessDeniedException("You don't have permission to do this operation !");
    }

    public boolean isAllowedToCreateOrUpdateUser(String emailFromToken, RegisterUserDto input) {
        Optional<User> userFromToken = userRepository.findByEmail(emailFromToken);
        RoleEnum targetRole = input.getRoleEnum();

        if (userFromToken.isPresent()) {
            RoleEnum currentUserRole = userFromToken.get().getRole().getRoleName();

            if (currentUserRole == RoleEnum.SUPER_ADMIN) {
                return true;
            }
            if (currentUserRole == RoleEnum.ADMIN && targetRole == RoleEnum.USER) {
                return checkCreateOrUpdatePermission(userFromToken.get());
            }
            if(currentUserRole == RoleEnum.ADMIN || currentUserRole == RoleEnum.USER){
                if (userFromToken.get().getEmail().equals(input.getEmail())){
                    input.setRoleEnum(null);
                    input.setPermissions(null);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkCreateOrUpdatePermission(User user) {
        Set<PermissionEnum> permissionEnumSet = user.getPermissionSet();
        return permissionEnumSet.contains(PermissionEnum.CREATE) || permissionEnumSet.contains(PermissionEnum.UPDATE);
    }

    public User createUser(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByRoleName(input.getRoleEnum());

        if (optionalRole.isEmpty()) {
            return null;
        }

        User user = new User();
        user.setName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(optionalRole.get());

        return userRepository.save(user);
    }


}
