package net.yuguerten.aritara.service;

import net.yuguerten.aritara.dto.LoginDTO;
import net.yuguerten.aritara.dto.UserDTO;
import net.yuguerten.aritara.model.User;
import net.yuguerten.aritara.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

//    public void updateUser(User user) {
//        userRepository.updateUser(user);
//    }

    public User registerNewUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()) != null ||
                userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        return userRepository.save(user);
    }

    public User validateUser(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (user != null && loginDTO.getPassword().equals(user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
