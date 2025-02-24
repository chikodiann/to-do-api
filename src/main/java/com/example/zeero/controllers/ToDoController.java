package com.example.zeero.controllers;

import com.example.zeero.constants.ApiPaths;
import com.example.zeero.dto.ToDoItemDTO;
import com.example.zeero.services.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.ToDo.ROOT)
public class ToDoController {

    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }
    @GetMapping
    public ResponseEntity<List<ToDoItemDTO>> getAllTasks() {
        return ResponseEntity.ok(toDoService.getAllTasks());
    }

    @GetMapping(ApiPaths.ToDo.GET_BY_ID)
    public ResponseEntity<ToDoItemDTO> getTaskById(@PathVariable Long id) {
        return toDoService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ToDoItemDTO> createTask(@RequestBody ToDoItemDTO task) {
        return ResponseEntity.ok(toDoService.createTask(task));
    }

    @PutMapping(ApiPaths.ToDo.UPDATE)
    public ResponseEntity<ToDoItemDTO> updateTask(@PathVariable Long id, @RequestBody ToDoItemDTO task) {
        return ResponseEntity.ok(toDoService.updateTask(id, task));
    }

    @DeleteMapping(ApiPaths.ToDo.DELETE)
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        toDoService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
