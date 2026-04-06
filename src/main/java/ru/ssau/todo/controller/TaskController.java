package ru.ssau.todo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.dto.TaskCreateDto;
import ru.ssau.todo.dto.TaskDto;
import ru.ssau.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final LocalDateTime DEFAULT_FROM = LocalDateTime.of(1970, 1, 1, 0, 0);
    private static final LocalDateTime DEFAULT_TO = LocalDateTime.of(2100, 1, 1, 0, 0);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskDto create(@RequestBody TaskCreateDto taskCreateDto, Authentication authentication) {
        return taskService.create(taskCreateDto, authentication.getName());
    }

    @GetMapping("/{id}")
    public TaskDto findById(@PathVariable long id) {
        return taskService.findById(id);
    }

    @GetMapping
    public List<TaskDto> findAll(
            @RequestParam long userId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to
    ) {
        if (from == null) {
            from = DEFAULT_FROM;
        }
        if (to == null) {
            to = DEFAULT_TO;
        }

        return taskService.findAll(from, to, userId);
    }

    @PutMapping("/{id}")
    public TaskDto update(@PathVariable long id, @RequestBody TaskDto taskDto) {
        return taskService.update(id, taskDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        taskService.delete(id);
    }

    @GetMapping("/active/count")
    public long countActive(@RequestParam long userId) {
        return taskService.countActiveTasksByUserId(userId);
    }
}