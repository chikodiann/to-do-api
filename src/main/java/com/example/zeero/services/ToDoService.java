package com.example.zeero.services;

import com.example.zeero.dto.ToDoItemDTO;
import java.util.List;
import java.util.Optional;

public interface ToDoService {
    List<ToDoItemDTO> getAllTasks();
    Optional<ToDoItemDTO> getTaskById(Long id);
    ToDoItemDTO createTask(ToDoItemDTO taskDTO);
    ToDoItemDTO updateTask(Long id, ToDoItemDTO updatedTaskDTO);
    void deleteTask(Long id);
}
