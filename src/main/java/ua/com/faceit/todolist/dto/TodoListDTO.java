package ua.com.faceit.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import ua.com.faceit.todolist.validation.group.CreateTodoListInfo;
import ua.com.faceit.todolist.validation.group.UpdateTodoListInfo;

import java.util.List;

@Getter
@Setter
public class TodoListDTO {
    @NotBlank(groups = {UpdateTodoListInfo.class})
    private Long id;

    @NotBlank(groups = {UpdateTodoListInfo.class, CreateTodoListInfo.class})
    private String name;

    private List<TaskDTO> tasks;
}
