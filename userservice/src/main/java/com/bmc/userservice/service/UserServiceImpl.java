package com.bmc.userservice.service;

import com.bmc.userservice.dao.UserDAO;
import com.bmc.userservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.bmc.userservice.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserDAO userDAO;

    @Override
    public User acceptUserDetails(User user) {
        return userDAO.save(user);
    }

    @Override
    public User getUserDetails(String id) throws ResourceNotFoundException{
        Optional<User> user = userDAO.findById(id);
        if (user.isPresent()){
            return user.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public User updateUserDetails(String id, User user) {
        User savedUser = getUserDetails(id);

        savedUser.setFirstName(user.getFirstName());
        savedUser.setlastName(user.getLastName());
        savedUser.setDob(user.getDob());
        savedUser.setMobile(user.getMobile());
        savedUser.setEmailId(user.getEmailId());
        savedUser.setCreatedDate(user.getCreatedDate());

        return userDAO.save(savedUser);
    }

    @Override
    public boolean deleteUser(String id) {
        User savedUser = getUserDetails(id);
        if (savedUser == null)
            return false;
        userDAO.delete(savedUser);
        return true;
    }

    @Override
    public List<User> getAllUsers(){
        return userDAO.findAll();
    }

    @Override
    public Page<User> getPaginatedUserDetails(Pageable pageRequest) {
        return userDAO.findAll(pageRequest);
    }
}
