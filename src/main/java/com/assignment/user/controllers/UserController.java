package com.assignment.user.controllers;

import com.assignment.user.dtos.RegisterUserDto;
import com.assignment.user.enums.PermissionEnum;
import com.assignment.user.enums.RoleEnum;
import com.assignment.user.enums.SortType;
import com.assignment.user.enums.UserSortBy;
import com.assignment.user.response.ApiResponse;
import com.assignment.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("v3/api/user/")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse> createAdministrator(@RequestBody RegisterUserDto registerUserDto) throws Exception {
        ApiResponse createdAdmin = userService.createOrUpdateUser(registerUserDto);
        return ResponseEntity.ok(createdAdmin);
    }

    @GetMapping("/getList")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "USER_ID") UserSortBy sortBy,
                                         @RequestParam(defaultValue = "") RoleEnum userType,
                                         @RequestParam(defaultValue = "DESC") SortType sortDirection,
                                         @RequestParam(defaultValue = "") String search) throws BadRequestException {
        return ResponseEntity.ok(userService.getList(size, page, sortBy.getSortType(), sortDirection.getSortType(), userType, search));
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionEnum>> getAllPermissions() {
        List<PermissionEnum> permissions = Arrays.asList(PermissionEnum.values());
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleEnum>> getAllRoles() {
        List<RoleEnum> roles = Arrays.asList(RoleEnum.values());
        return ResponseEntity.ok(roles);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "", required = false) String email) throws BadRequestException {
        return ResponseEntity.ok(userService.delete(email));
    }
}
