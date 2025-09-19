package com.example.taskmanager.taskmanager.exception;

public class CustomExceptions {
    public static class TaskDeletionException extends RuntimeException {
        public TaskDeletionException(String message) {
            super(message);
        }
    }
}
