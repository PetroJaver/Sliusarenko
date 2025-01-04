package ua.com.faceit.todolist.service;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;
import ua.com.faceit.todolist.data.User;
import ua.com.faceit.todolist.dto.UserDTO;
import ua.com.faceit.todolist.mapper.UserMapper;
import ua.com.faceit.todolist.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@EnableJpaAuditing
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();

        return userMapper.toDTOs(users);
    }
}