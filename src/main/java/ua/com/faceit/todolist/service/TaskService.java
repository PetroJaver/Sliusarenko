package ua.com.faceit.todolist.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.validation.Validation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ua.com.faceit.todolist.data.Task;
import ua.com.faceit.todolist.data.TodoList;
import ua.com.faceit.todolist.data.User;
import ua.com.faceit.todolist.dto.TaskDTO;
import ua.com.faceit.todolist.exception.AccessDeniedException;
import ua.com.faceit.todolist.exception.BadRequestException;
import ua.com.faceit.todolist.exception.NotFoundException;
import ua.com.faceit.todolist.mapper.TaskMapper;
import ua.com.faceit.todolist.repository.TaskRepository;
import ua.com.faceit.todolist.repository.TodoListRepository;
import ua.com.faceit.todolist.repository.UserRepository;
import ua.com.faceit.todolist.validation.group.CreateTaskInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ua.com.faceit.todolist.utils.SecurityUtils.getUserId;
import static ua.com.faceit.todolist.utils.SecurityUtils.isUserAdmin;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final TodoListRepository todoListRepository;

    public List<TaskDTO> getAll() {
        List<Task> tasks;
        if (isUserAdmin()) {
            tasks = taskRepository.findAll();
        } else {
            Long userId = getUserId();
            tasks = taskRepository.findAllByUser_Id(userId);
        }

        return taskMapper.toDTOs(tasks);
    }

    public TaskDTO getById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));

        if (!task.getUser().getId().equals(getUserId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        return taskMapper.toDTO(task);
    }

    public TaskDTO add(@Validated({CreateTaskInfo.class}) TaskDTO taskDTO) {
        Long userId = getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
        TodoList todoList = todoListRepository.findById(taskDTO.getTodoListId()).orElseThrow(() -> new NotFoundException("Todo list not found"));

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setUser(user);
        task.setTodoList(todoList);

        taskRepository.save(task);

        return taskMapper.toDTO(task);
    }

    public void deleteById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));

        boolean isDeleteAllowed = isUserAdmin() || task.getUser().getId().equals(getUserId());
        if(isDeleteAllowed) {
            taskRepository.delete(task);
        } else {
            throw new AccessDeniedException("You do not have permission to delete this resource");
        }
    }

    public TaskDTO update(TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskDTO.getId()).orElseThrow(() -> new NotFoundException("Task not found"));

        boolean isUpdateAllowed = isUserAdmin() || task.getUser().getId().equals(getUserId());
        if(isUpdateAllowed) {
            task = taskMapper.update(task, taskDTO);

            Long todoListId = taskDTO.getTodoListId();
            if(todoListId != null) {
                TodoList todoList = todoListRepository.findById(todoListId).orElseThrow(() -> new NotFoundException("Todo list not found"));
                task.setTodoList(todoList);
            }

            taskRepository.save(task);
        } else {
            throw new AccessDeniedException("You do not have permission to update this resource");
        }

        return taskMapper.toDTO(task);
    }

    public List<TaskDTO> upload(MultipartFile file) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
                .addColumn("title")
                .addColumn("description")
                .addNumberColumn("todoListId")
                .build();

        List<TaskDTO> taskDTOS;
        try (MappingIterator<TaskDTO> parsedData = csvMapper.readerWithSchemaFor(TaskDTO.class)
                .with(schema)
                .readValues(new String(file.getBytes(), StandardCharsets.UTF_8))) {

            TaskDTO taskDTO;
            taskDTOS = new ArrayList<>();
            while (parsedData.hasNext()) {
                taskDTO = parsedData.next();
                taskDTOS.add(add(taskDTO));
            }
        }

        return taskDTOS;
    }
}
