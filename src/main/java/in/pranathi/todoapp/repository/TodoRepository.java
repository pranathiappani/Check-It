package in.pranathi.todoapp.repository;

import in.pranathi.todoapp.entity.TodoEntity;
import in.pranathi.todoapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    List<TodoEntity> findByUser(UserEntity user);
}
