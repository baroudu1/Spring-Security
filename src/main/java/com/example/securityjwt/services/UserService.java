package com.example.securityjwt.services;

import com.example.securityjwt.models.User;
import com.example.securityjwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User findUserByEmail(User user) {
        return userRepository.findByEmail(user.getEmail()).orElse(null);
    }
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
    public User updateUser(User user) {
        User userToUpdate = findUserById(user.getId());
        if (userToUpdate != null) {

            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setPassword(user.getPassword());
//            userToUpdate.setRoles(user.getRoles());

            return saveUser(userToUpdate);
        }
        return null;
    }

}
