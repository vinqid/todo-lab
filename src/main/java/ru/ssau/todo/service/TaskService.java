package ru.ssau.todo.service;

import org.springframework.stereotype.Service;
import ru.ssau.todo.dto.TaskCreateDto;
import ru.ssau.todo.dto.TaskDto;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.TaskStatus;
import ru.ssau.todo.entity.User;
import ru.ssau.todo.exception.TaskNotFoundException;
import ru.ssau.todo.repository.TaskRepository;
import ru.ssau.todo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDto create(TaskCreateDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));

        if ((dto.getStatus() == TaskStatus.OPEN
                || dto.getStatus() == TaskStatus.IN_PROGRESS)
                && taskRepository.countActiveTasksByUserId(user.getId()) >= 10) {
            throw new IllegalStateException("У пользователя уже есть 10 активных задач");
        }

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setStatus(dto.getStatus());
        task.setCreatedBy(user);
        task.setCreatedAt(LocalDateTime.now());

        Task saved = taskRepository.save(task);
        return toDto(saved);
    }

    public TaskDto findById(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return toDto(task);
    }

    public List<TaskDto> findAll(LocalDateTime from, LocalDateTime to, long userId) {
        return taskRepository.findAllByCreatedAtBetweenAndUserId(from, to, userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<TaskDto> findAllForCurrentUser(LocalDateTime from, LocalDateTime to, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));

        return taskRepository.findAllByCreatedAtBetweenAndUserId(from, to, user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public TaskDto update(long id, TaskDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(dto.getTitle());
        task.setStatus(dto.getStatus());

        Task updated = taskRepository.save(task);
        return toDto(updated);
    }

    public void delete(long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Проверка, что задача не была создана менее 5 минут назад
        if (task.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5))) {
            throw new IllegalStateException("Задача не может быть удалена в течение 5 минут после создания");
        }

        taskRepository.deleteById(id);
    }

    public long countActiveTasksByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));

        return taskRepository.countActiveTasksByUserId(user.getId());
    }

    public long countActiveTasksByUserId(long userId) {
        return taskRepository.countActiveTasksByUserId(userId);
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getStatus(),
                task.getCreatedBy().getId(),
                task.getCreatedAt()
        );
    }
}