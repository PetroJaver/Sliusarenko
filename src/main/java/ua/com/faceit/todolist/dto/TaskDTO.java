package ua.com.faceit.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ua.com.faceit.todolist.validation.group.CreateTaskInfo;
import ua.com.faceit.todolist.validation.group.UpdateTaskInfo;

@Getter
@Setter
public class TaskDTO {

    @NotNull(groups = UpdateTaskInfo.class, message = "Id cannot be null")
    private Long id;

    @NotBlank(groups = CreateTaskInfo.class)
    @Size(min = 3, max = 50, groups = {CreateTaskInfo.class, UpdateTaskInfo.class})
    private String title;

    private String description;
}
