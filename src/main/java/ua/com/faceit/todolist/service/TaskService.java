package ua.com.faceit.todolist.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.faceit.todolist.data.Task;
import ua.com.faceit.todolist.data.User;
import ua.com.faceit.todolist.dto.TaskDTO;
import ua.com.faceit.todolist.exception.AccessDeniedException;
import ua.com.faceit.todolist.exception.BadRequestException;
import ua.com.faceit.todolist.exception.NotFoundException;
import ua.com.faceit.todolist.mapper.TaskMapper;
import ua.com.faceit.todolist.repository.TaskRepository;
import ua.com.faceit.todolist.repository.UserRepository;

import java.util.List;

import static ua.com.faceit.todolist.utils.SecurityUtils.getUserId;
import static ua.com.faceit.todolist.utils.SecurityUtils.isUserAdmin;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public List<TaskDTO> getTasks() {
        List<Task> tasks;
        if (isUserAdmin()) {
            tasks = taskRepository.findAll();
        } else {
            Long userId = getUserId();
            tasks = taskRepository.findAllByUser_Id(userId);
        }

        return taskMapper.toTaskDTOs(tasks);
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));

        if (!task.getUser().getId().equals(getUserId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        return taskMapper.toTaskDTO(task);
    }

    public TaskDTO addTask(TaskDTO taskDTO) {
        Long userId = getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setUser(user);

        taskRepository.save(task);

        return taskMapper.toTaskDTO(task);
    }

    public void deleteTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));

        boolean isDeleteAllowed = isUserAdmin() || task.getUser().getId().equals(getUserId());
        if(isDeleteAllowed) {
            taskRepository.delete(task);
        } else {
            throw new AccessDeniedException("You do not have permission to delete this resource");
        }
    }

    public TaskDTO updateTask(TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskDTO.getId()).orElseThrow(() -> new NotFoundException("Task not found"));

        boolean isUpdateAllowed = isUserAdmin() || task.getUser().getId().equals(getUserId());
        if(isUpdateAllowed) {
            task = taskMapper.updateTask(task, taskDTO);
            taskRepository.save(task);
        } else {
            throw new AccessDeniedException("You do not have permission to update this resource");
        }

        return taskMapper.toTaskDTO(task);
    }
}