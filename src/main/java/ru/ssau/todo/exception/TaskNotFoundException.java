package ru.ssau.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(long taskId) {
        super("Задача с таким id " + taskId + " не существует");
    }
}
