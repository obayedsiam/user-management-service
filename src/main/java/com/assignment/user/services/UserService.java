package com.assignment.user.services;


import com.assignment.user.dtos.RegisterUserDto;
import com.assignment.user.entities.User;
import com.assignment.user.enums.RoleEnum;
import com.assignment.user.request.UserRequest;
import com.assignment.user.response.ApiResponse;
import com.assignment.user.response.PaginatedResponse;
import com.assignment.user.response.Response;
import com.assignment.user.response.UserResponseDto;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {

    ApiResponse delete(String email) throws BadRequestException;

    PaginatedResponse<UserResponseDto> getList(Integer size, Integer page, String sortBy, String sortDirection, RoleEnum userType, String search) throws BadRequestException;

    ApiResponse createOrUpdateUser(RegisterUserDto input) throws Exception;

    User createUser(RegisterUserDto input);
}
