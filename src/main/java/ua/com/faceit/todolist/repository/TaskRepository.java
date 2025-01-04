package ua.com.faceit.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.faceit.todolist.data.Task;
import ua.com.faceit.todolist.data.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser_Id(Long username);

    Long user(User user);
}
