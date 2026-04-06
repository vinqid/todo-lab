package ru.ssau.todo.dto;

import ru.ssau.todo.entity.TaskStatus;

public class TaskCreateDto {

    private String title;
    private TaskStatus status;

    public TaskCreateDto() {
    }

    public TaskCreateDto(String title, TaskStatus status) {
        this.title = title;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}