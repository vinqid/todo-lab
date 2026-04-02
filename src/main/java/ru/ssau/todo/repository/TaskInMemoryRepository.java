//package ru.ssau.todo.repository;
//
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Repository;
//import ru.ssau.todo.entity.Task;
//import ru.ssau.todo.entity.TaskStatus;
//import ru.ssau.todo.exception.TaskNotFoundException;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Repository
//@Profile("inmemory")
//public class TaskInMemoryRepository implements TaskRepository{
//
//    private final Map<Long, Task> storage = new HashMap<>();
//    private long idCounter = 0;
//
//    @Override
//    public Task create(Task task) throws TaskNotFoundException {
//        if (task == null) {
//            throw new IllegalArgumentException("Заголовок не должен быть пустым");
//        }
//
//        long newId = ++idCounter; // первый id= 1
//        task.setId(newId);
//        storage.put(newId, task);
//
//        return task;
//    }
//
//    @Override
//    public Optional<Task> findById(long id) {
//        return Optional.ofNullable(storage.get(id)); //Optional.ofNullable(value):
//                                            //      если value != null → создаётся Optional с объектом
//                                            //      если value == null → создаётся Optional.empty()
//    }
//
//    @Override
//    public List<Task> findAll(LocalDateTime from, LocalDateTime to, long userId) {
//        return storage.values().stream()
//                .filter(task -> task.getCreatedBy() == userId)
//                .filter(task -> !task.getCreatedAt().isBefore(from)
//                        && !task.getCreatedAt().isAfter(to))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void update(Task task) throws Exception {
//        Task existing = storage.get(task.getId());
//
//        if (existing == null) {
//            throw new TaskNotFoundException(task.getId());
//        }
//
//        existing.setTitle(task.getTitle());
//        existing.setStatus(task.getStatus());
//        existing.setCreatedBy(task.getCreatedBy());
//    }
//
//    @Override
//    public void deleteById(long id) {
//        storage.remove(id);
//    }
//
//    @Override
//    public long countActiveTasksByUserId(long userId) {
//        return storage.values().stream()
//                .filter(task -> task.getCreatedBy() == userId)
//                .filter(task -> task.getStatus() == TaskStatus.OPEN
//                        || task.getStatus() == TaskStatus.IN_PROGRESS)
//                .count();
//    }
//}
