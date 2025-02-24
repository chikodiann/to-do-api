package com.example.zeero.services.impl;

import com.example.zeero.dto.ToDoItemDTO;
import com.example.zeero.enums.Priority;
import com.example.zeero.model.ToDoItem;
import com.example.zeero.repositories.ToDoRepository;
import com.example.zeero.services.ToDoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ToDoItemDTO> getAllTasks() {
        return toDoRepository.findByDeletedFalse()
                .stream()
                .map(task -> modelMapper.map(task, ToDoItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ToDoItemDTO> getTaskById(Long id) {
        return toDoRepository.findById(id)
                .filter(task -> !task.isDeleted())
                .map(task -> modelMapper.map(task, ToDoItemDTO.class));
    }

    @Override
    public ToDoItemDTO createTask(ToDoItemDTO taskDTO) {
        ToDoItem task = modelMapper.map(taskDTO, ToDoItem.class);
        task.setDeleted(false); // Ensure new tasks are not deleted
        return modelMapper.map(toDoRepository.save(task), ToDoItemDTO.class);
    }

    @Override
    public ToDoItemDTO updateTask(Long id, ToDoItemDTO updatedTaskDTO) {
        ToDoItem task = toDoRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setTitle(updatedTaskDTO.getTitle());
        task.setDescription(updatedTaskDTO.getDescription());
        task.setDueDate(updatedTaskDTO.getDueDate());
        task.setPriority(updatedTaskDTO.getPriority());
        task.setCompleted(updatedTaskDTO.isCompleted());

        return modelMapper.map(toDoRepository.save(task), ToDoItemDTO.class);
    }

    @Override
    public void deleteTask(Long id) {
        ToDoItem task = toDoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setDeleted(true);
        toDoRepository.save(task);
    }

    @Override
    public void permanentDeleteTask(Long id) {
        if (!toDoRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found");
        }
        toDoRepository.deleteById(id);
    }

    @Override
    public List<ToDoItemDTO> getTasksSortedByDueDate() {
        return toDoRepository.findByDeletedFalseOrderByDueDateAsc()
                .stream()
                .map(task -> modelMapper.map(task, ToDoItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ToDoItemDTO> getTasksByPriority(Priority priority) {
        return toDoRepository.findByDeletedFalseAndPriority(priority)
                .stream()
                .map(task -> modelMapper.map(task, ToDoItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ToDoItemDTO> getTasksByCompletionStatus(boolean completed) {
        return toDoRepository.findByDeletedFalseAndCompleted(completed)
                .stream()
                .map(task -> modelMapper.map(task, ToDoItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ToDoItemDTO> getFilteredTasks(Priority priority, Boolean completed) {
        List<ToDoItem> tasks;

        if (priority != null && completed != null) {
            tasks = toDoRepository.findByDeletedFalse()
                    .stream()
                    .filter(task -> task.getPriority() == priority && task.isCompleted() == completed)
                    .collect(Collectors.toList());
        } else if (priority != null) {
            tasks = toDoRepository.findByDeletedFalseAndPriority(priority);
        } else if (completed != null) {
            tasks = toDoRepository.findByDeletedFalseAndCompleted(completed);
        } else {
            tasks = toDoRepository.findByDeletedFalse();
        }

        return tasks.stream()
                .map(task -> modelMapper.map(task, ToDoItemDTO.class))
                .collect(Collectors.toList());
    }

}
