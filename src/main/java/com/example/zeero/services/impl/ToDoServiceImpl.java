package com.example.zeero.services.impl;

import com.example.zeero.dto.ToDoItemDTO;
import com.example.zeero.model.ToDoItem;
import com.example.zeero.repositories.ToDoRepository;
import com.example.zeero.services.ToDoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ToDoItemDTO> getAllTasks() {
        return toDoRepository.findAll()
                .stream()
                .filter(task -> !task.isDeleted())
                .map(task -> modelMapper.map(task, ToDoItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ToDoItemDTO> getTaskById(Long id) {
        return toDoRepository.findById(id)
                .filter(task -> !task.isDeleted()) // Ensure deleted tasks are not retrievable
                .map(task -> modelMapper.map(task, ToDoItemDTO.class));
    }

    @Override
    public ToDoItemDTO createTask(ToDoItemDTO taskDTO) {
        ToDoItem task = modelMapper.map(taskDTO, ToDoItem.class);
        return modelMapper.map(toDoRepository.save(task), ToDoItemDTO.class);
    }

    @Override
    public ToDoItemDTO updateTask(Long id, ToDoItemDTO updatedTaskDTO) {
        return toDoRepository.findById(id)
                .filter(task -> !task.isDeleted())
                .map(task -> {
                    modelMapper.map(updatedTaskDTO, task);
                    return modelMapper.map(toDoRepository.save(task), ToDoItemDTO.class);
                }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public void deleteTask(Long id) {
        toDoRepository.findById(id).ifPresent(task -> {
            task.setDeleted(true); // Mark task as deleted instead of hard deletion
            toDoRepository.save(task);
        });
    }
}
