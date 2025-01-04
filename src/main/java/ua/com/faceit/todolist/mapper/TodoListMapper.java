package ua.com.faceit.todolist.mapper;

import org.mapstruct.*;
import ua.com.faceit.todolist.data.TodoList;
import ua.com.faceit.todolist.dto.TodoListDTO;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.SETTER, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TodoListMapper {

    @Mapping(target = "tasks", source = "tasks")
    List<TodoListDTO> toDTOs(List<TodoList> todoLists);

    TodoListDTO toDTO(TodoList todoList);

    TodoList update(@MappingTarget TodoList todoList, TodoListDTO todoListDTO);
}
