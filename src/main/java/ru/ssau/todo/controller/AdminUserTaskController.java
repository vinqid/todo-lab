package ru.ssau.todo.controller;

import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.dto.TaskDto;
import ru.ssau.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class AdminUserTaskController {

    private static final LocalDateTime DEFAULT_FROM = LocalDateTime.of(1970, 1, 1, 0, 0);
    private static final LocalDateTime DEFAULT_TO = LocalDateTime.of(2100, 1, 1, 0, 0);

    private final TaskService taskService;

    public AdminUserTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{userId}/tasks")
    public List<TaskDto> findTasksByUserId(
            @PathVariable long userId,
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

    @GetMapping("/{userId}/tasks/active/count")
    public long countActiveByUserId(@PathVariable long userId) {
        return taskService.countActiveTasksByUserId(userId);
    }
}