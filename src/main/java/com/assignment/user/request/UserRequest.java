package com.assignment.user.request;

import com.assignment.user.entities.Role;
import com.assignment.user.entities.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserRequest {

    private Long id;
    private String userName;
    private String password;
    private String phone;
    private String email;
    private List<Role> roleList;

    public User requestToEntity(){
        User user = new User();
        user.setName(this.userName);
        user.setPassword(this.password);
        user.setEmail(this.email);
        return user;
    }

}
