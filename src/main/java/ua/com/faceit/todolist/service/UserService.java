package ua.com.faceit.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;
import ua.com.faceit.todolist.data.User;
import ua.com.faceit.todolist.dto.UserDTO;
import ua.com.faceit.todolist.mapper.UserMapper;
import ua.com.faceit.todolist.repository.UserRepository;

import java.util.List;

@Service
@EnableJpaAuditing
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.toUserDTOs(users);
    }
}