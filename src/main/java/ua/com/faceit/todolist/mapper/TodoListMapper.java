package ua.com.faceit.todolist.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.com.faceit.todolist.data.TodoList;
import ua.com.faceit.todolist.dto.TodoListDTO;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.SETTER, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TodoListMapper {
    List<TodoListDTO> toDTOs(List<TodoList> todoLists);

    TodoListDTO toDTO(TodoList todoList);

    TodoList update(@MappingTarget TodoList todoList, TodoListDTO todoListDTO);
}
