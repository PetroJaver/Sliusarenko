package ua.com.faceit.todolist.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.faceit.todolist.data.TodoList;
import ua.com.faceit.todolist.data.User;
import ua.com.faceit.todolist.dto.TodoListDTO;
import ua.com.faceit.todolist.exception.AccessDeniedException;
import ua.com.faceit.todolist.exception.BadRequestException;
import ua.com.faceit.todolist.exception.NotFoundException;
import ua.com.faceit.todolist.mapper.TodoListMapper;
import ua.com.faceit.todolist.repository.TodoListRepository;
import ua.com.faceit.todolist.repository.UserRepository;

import java.util.List;

import static ua.com.faceit.todolist.utils.SecurityUtils.getUserId;
import static ua.com.faceit.todolist.utils.SecurityUtils.isUserAdmin;

@AllArgsConstructor
@Service
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final TodoListMapper todoListMapper;
    private final UserRepository userRepository;

    public List<TodoListDTO> getAll() {
        List<TodoList> todoLists;
        if (isUserAdmin()) {
            todoLists = todoListRepository.findAll();
        } else {
            Long userId = getUserId();
            todoLists = todoListRepository.findAllByUser_Id(userId);
        }

        return todoListMapper.toDTOs(todoLists);
    }

    public TodoListDTO getById(Long id) {
        TodoList todoList = todoListRepository.findById(id).orElseThrow(() -> new NotFoundException("Todo list not found"));

        if (!todoList.getUser().getId().equals(getUserId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        return todoListMapper.toDTO(todoList);
    }

    public TodoListDTO create(TodoListDTO todoListDTO) {
        Long userId = getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));

        TodoList todolist = new TodoList();
        todolist.setName(todoListDTO.getName());
        todolist.setUser(user);

        todoListRepository.save(todolist);

        return todoListMapper.toDTO(todolist);
    }

    public void deleteById(Long id) {
        TodoList todoList = todoListRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));

        boolean isDeleteAllowed = isUserAdmin() || todoList.getUser().getId().equals(getUserId());
        if(isDeleteAllowed) {
            todoListRepository.delete(todoList);
        } else {
            throw new AccessDeniedException("You do not have permission to delete this resource");
        }
    }

    public TodoListDTO update(TodoListDTO todoListDTO) {
        TodoList todoList = todoListRepository.findById(todoListDTO.getId()).orElseThrow(() -> new NotFoundException("Task not found"));

        boolean isUpdateAllowed = isUserAdmin() || todoList.getUser().getId().equals(getUserId());
        if(isUpdateAllowed) {
            todoList = todoListMapper.update(todoList, todoListDTO);
            todoListRepository.save(todoList);
        } else {
            throw new AccessDeniedException("You do not have permission to update this resource");
        }

        return todoListMapper.toDTO(todoList);
    }
}
