package com.assignment.user.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class RoleRequest {

    private Long roleId;
    private String roleName;
    private List<Long> userIds;


}
