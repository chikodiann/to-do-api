package com.example.zeero.services;

import com.example.zeero.dto.ToDoItemDTO;
import com.example.zeero.enums.Priority;

import java.util.List;
import java.util.Optional;

public interface ToDoService {
    List<ToDoItemDTO> getAllTasks();
    Optional<ToDoItemDTO> getTaskById(Long id);
    ToDoItemDTO createTask(ToDoItemDTO taskDTO);
    ToDoItemDTO updateTask(Long id, ToDoItemDTO updatedTaskDTO);
    void deleteTask(Long id); // Soft delete
    void permanentDeleteTask(Long id); // Hard delete
    List<ToDoItemDTO> getTasksSortedByDueDate();
    List<ToDoItemDTO> getTasksByPriority(Priority priority);
    List<ToDoItemDTO> getTasksByCompletionStatus(boolean completed);
    List<ToDoItemDTO> getFilteredTasks(Priority priority, Boolean completed);
}
