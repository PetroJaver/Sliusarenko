package ua.com.faceit.todolist.mapper;

import org.mapstruct.*;
import ua.com.faceit.todolist.data.Task;
import ua.com.faceit.todolist.dto.TaskDTO;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.SETTER, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    List<TaskDTO> toDTOs(List<Task> tasks);

    @Mapping(target = "todoListId", source = "todoList.id")
    TaskDTO toDTO(Task task);

    Task update(@MappingTarget Task task, TaskDTO taskDTO);
}
