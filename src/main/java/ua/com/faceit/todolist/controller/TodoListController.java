package ua.com.faceit.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.faceit.todolist.dto.TodoListDTO;
import ua.com.faceit.todolist.exception.BadRequestException;
import ua.com.faceit.todolist.service.TodoListService;
import ua.com.faceit.todolist.validation.group.CreateTaskInfo;
import ua.com.faceit.todolist.validation.group.CreateTodoListInfo;
import ua.com.faceit.todolist.validation.group.UpdateTaskInfo;
import ua.com.faceit.todolist.validation.group.UpdateTodoListInfo;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/todolist")
@RequiredArgsConstructor
public class TodoListController {

    private final TodoListService todoListService;

    @Value("${webServerUrl}")
    String webServerUrl;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllTasks() {
        List<TodoListDTO> tasks = todoListService.getAll();

        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addTask(@Validated(CreateTodoListInfo.class) @RequestBody TodoListDTO task) {
        if(task.getId() != null) {
            throw new BadRequestException("Task id should be null");
        }

        TodoListDTO taskDTO = todoListService.create(task);

        return ResponseEntity.created(URI.create(webServerUrl + "/api/tasks/" + taskDTO.getId())).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        TodoListDTO task = todoListService.getById(id);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTaskById(@PathVariable Long id) {
        todoListService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateTask(@Validated(UpdateTodoListInfo.class) @RequestBody TodoListDTO task) {
        TodoListDTO taskDTO = todoListService.update(task);

        return ResponseEntity.ok(taskDTO);
    }
}
