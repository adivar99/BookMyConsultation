package com.bmc.userservice.service;

import com.bmc.userservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    public User acceptUserDetails(User user);

    public User getUserDetails(String id);

    public User updateUserDetails(String id, User user);

    public boolean deleteUser(String id);

    public List<User> getAllUsers();

    public Page<User> getPaginatedUserDetails(Pageable pageRequest);
}
