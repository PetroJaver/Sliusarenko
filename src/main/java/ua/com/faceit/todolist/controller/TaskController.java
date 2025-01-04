package ua.com.faceit.todolist.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.faceit.todolist.dto.TaskDTO;
import ua.com.faceit.todolist.exception.BadRequestException;
import ua.com.faceit.todolist.repository.UserRepository;
import ua.com.faceit.todolist.service.TaskService;
import ua.com.faceit.todolist.validation.group.CreateTaskInfo;
import ua.com.faceit.todolist.validation.group.UpdateTaskInfo;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Value("${webServerUrl}")
    String webServerUrl;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllTasks() {
        List<TaskDTO> tasks = taskService.getTasks();

        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addTask(@Validated(CreateTaskInfo.class) @RequestBody TaskDTO task) {
        if(task.getId() != null) {
            throw new BadRequestException("Task id should be null");
        }

        TaskDTO taskDTO = taskService.addTask(task);

        return ResponseEntity.created(URI.create(webServerUrl + "/api/tasks/" + taskDTO.getId())).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateTask(@Validated(UpdateTaskInfo.class) @RequestBody TaskDTO task) {
        TaskDTO taskDTO = taskService.updateTask(task);

        return ResponseEntity.ok(taskDTO);
    }
}
