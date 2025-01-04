package ua.com.faceit.todolist.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TodoListDTO {
    private Long id;

    private String name;

    private List<TaskDTO> tasks;

    private UserDTO user;
}
