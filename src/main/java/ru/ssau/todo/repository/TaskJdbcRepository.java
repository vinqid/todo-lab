//package ru.ssau.todo.repository;
//
//import org.springframework.context.annotation.Profile;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import ru.ssau.todo.entity.Task;
//import ru.ssau.todo.entity.TaskStatus;
//import ru.ssau.todo.exception.TaskNotFoundException;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//@Profile("jdbc")
//public class TaskJdbcRepository implements TaskRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public TaskJdbcRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    private Task mapRow(ResultSet rs, int rowNum) throws SQLException {
//        return new Task(
//                rs.getLong("id"),
//                rs.getString("title"),
//                TaskStatus.valueOf(rs.getString("status")),
//                rs.getLong("created_by"),
//                rs.getTimestamp("created_at").toLocalDateTime()
//        );
//    }
//    @Override
//    public Task create(Task task) {
//
//        String sql = """
//        INSERT INTO task(title, status, created_by, created_at)
//        VALUES (?, ?, ?, ?)
//        RETURNING id
//        """;
//        Long id = jdbcTemplate.queryForObject(
//                sql,
//                Long.class,
//                task.getTitle(),
//                task.getStatus().name(),
//                task.getCreatedBy(),
//                task.getCreatedAt()
//        );
//
//        task.setId(id);
//
//        return task;
//    }
//
//    @Override
//    public Optional<Task> findById(long id) {
//
//        String sql = "SELECT * FROM task WHERE id = ?";
//
//        List<Task> tasks = jdbcTemplate.query(sql, this::mapRow, id);
//
//        return tasks.stream().findFirst();
//    }
//
//    @Override
//    public List<Task> findAll(LocalDateTime from, LocalDateTime to, long userId) {
//
//        String sql = """
//        SELECT * FROM task
//        WHERE created_by = ?
//        AND created_at BETWEEN ? AND ?
//        """;
//
//        return jdbcTemplate.query(
//                sql,
//                this::mapRow,
//                userId,
//                from,
//                to
//        );
//    }
//
//    @Override
//    public void update(Task task) {
//
//        String sql = """
//        UPDATE task
//        SET title = ?, status = ?, created_by = ?
//        WHERE id = ?
//        """;
//
//        int updated = jdbcTemplate.update(
//                sql,
//                task.getTitle(),
//                task.getStatus().name(),
//                task.getCreatedBy(),
//                task.getId()
//        );
//
//        if (updated == 0) {
//            throw new TaskNotFoundException(task.getId());
//        }
//    }
//
//    @Override
//    public void deleteById(long id) {
//
//        String sql = "DELETE FROM task WHERE id = ?";
//
//        jdbcTemplate.update(sql, id);
//    }
//
//    @Override
//    public long countActiveTasksByUserId(long userId) {
//
//        String sql = """
//        SELECT COUNT(*)
//        FROM task
//        WHERE created_by = ?
//        AND status IN ('OPEN', 'IN_PROGRESS')
//        """;
//
//        return jdbcTemplate.queryForObject(sql, Long.class, userId);
//    }
//}