package ua.com.faceit.todolist.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinTable(
            name = "todo_lists_tasks",
            joinColumns = {
                    @JoinColumn(name = "tasks_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "todo_list_id")
            }
    )
    private TodoList todoList;
}
