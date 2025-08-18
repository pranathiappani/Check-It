package in.pranathi.todoapp.repository;

import in.pranathi.todoapp.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
}
