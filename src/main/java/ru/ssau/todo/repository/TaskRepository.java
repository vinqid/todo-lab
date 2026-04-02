package ru.ssau.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ssau.todo.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
//когда хочется написать запрос в точности в синтаксисе конкретной базы данныx
    //native SQL пишет запросы прямо к таблицам базы данных, например task, users, created_by
    //Hibernate — это ORM-фреймворк,
// который позволяет работать с таблицами базы данных как с Java-объектами.
    @Query(
            value = """
                    SELECT * 
                    FROM task
                    WHERE created_by = :userId
                      AND created_at BETWEEN :from AND :to
                    """,
            nativeQuery = true
    )
    List<Task> findAllByCreatedAtBetweenAndUserId(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("userId") Long userId
    );
//JPQL работает не с таблицами и колонками, а с сущностями и их полями.
    //JPQL работает на уровне Java-сущностей, например Task, createdBy, status
    @Query("""
           SELECT COUNT(t)
           FROM Task t
           WHERE t.createdBy.id = :userId
             AND (t.status = ru.ssau.todo.entity.TaskStatus.OPEN
                  OR t.status = ru.ssau.todo.entity.TaskStatus.IN_PROGRESS)
           """)
    long countActiveTasksByUserId(@Param("userId") Long userId);
}