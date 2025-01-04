package ua.com.faceit.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.faceit.todolist.dto.UserDTO;
import ua.com.faceit.todolist.service.UserService;

import java.util.List;

@RequestMapping("/api/users")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}