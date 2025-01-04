package ua.com.faceit.todolist.dto;

import lombok.Getter;
import lombok.Setter;
import ua.com.faceit.todolist.data.Role;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Set<Role> roles;

    private boolean enabled;

    private Date createdAt;

    private Date updatedAt;
}
