package com.example.zeero.controllers;

import com.example.zeero.constants.ApiPaths;
import com.example.zeero.dto.ToDoItemDTO;
import com.example.zeero.enums.Priority;
import com.example.zeero.services.ToDoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.ToDo.ROOT)
@RequiredArgsConstructor
@Tag(name = "To-Do Controller", description = "Operations for managing to-do tasks")
public class ToDoController {

    private final ToDoService toDoService;

    @GetMapping
    @Operation(
            summary = "Retrieve all tasks",
            description = "Fetches a list of tasks with optional filtering by priority and completion status.",
            parameters = {
                    @Parameter(name = "priority", description = "Filter tasks by priority level"),
                    @Parameter(name = "completed", description = "Filter tasks by completion status")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tasks",
                            content = @Content(schema = @Schema(implementation = ToDoItemDTO.class)))
            }
    )
    public ResponseEntity<List<ToDoItemDTO>> getAllTasks(
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Boolean completed) {
        List<ToDoItemDTO> tasks = toDoService.getFilteredTasks(priority, completed);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/sorted")
    @Operation(
            summary = "Retrieve sorted tasks",
            description = "Fetches all tasks sorted by due date in ascending order.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved sorted tasks",
                            content = @Content(schema = @Schema(implementation = ToDoItemDTO.class)))
            }
    )
    public ResponseEntity<List<ToDoItemDTO>> getSortedTasks() {
        return ResponseEntity.ok(toDoService.getTasksSortedByDueDate());
    }

    @GetMapping(ApiPaths.ToDo.GET_BY_ID)
    @Operation(
            summary = "Get a task by ID",
            description = "Retrieves a specific to-do task by its unique ID.",
            parameters = {
                    @Parameter(name = "id", description = "Task ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task found",
                            content = @Content(schema = @Schema(implementation = ToDoItemDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    public ResponseEntity<ToDoItemDTO> getTaskById(@PathVariable Long id) {
        return toDoService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @Operation(
            summary = "Create a new task",
            description = "Adds a new to-do task to the list.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task created successfully",
                            content = @Content(schema = @Schema(implementation = ToDoItemDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<ToDoItemDTO> createTask(@RequestBody ToDoItemDTO task) {
        ToDoItemDTO createdTask = toDoService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping(ApiPaths.ToDo.UPDATE)
    @Operation(
            summary = "Update an existing task",
            description = "Modifies the details of an existing to-do task.",
            parameters = {
                    @Parameter(name = "id", description = "Task ID to update", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    public ResponseEntity<ToDoItemDTO> updateTask(@PathVariable Long id, @RequestBody ToDoItemDTO task) {
        return toDoService.getTaskById(id)
                .map(existing -> ResponseEntity.ok(toDoService.updateTask(id, task)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping(ApiPaths.ToDo.TEMPORAL_DELETE)
    @Operation(
            summary = "Soft delete a task",
            description = "Marks a task as deleted instead of removing it permanently.",
            parameters = {
                    @Parameter(name = "id", description = "Task ID to delete", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task successfully soft deleted"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    public ResponseEntity<Void> temporalDeleteTask(@PathVariable Long id) {
        return toDoService.getTaskById(id)
                .map(task -> {
                    toDoService.deleteTask(id);
                    return ResponseEntity.noContent().<Void>build();
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @DeleteMapping(ApiPaths.ToDo.PERMANENT_DELETE)
    @Operation(
            summary = "Hard delete a task",
            description = "Permanently removes a task from the database.",
            parameters = {
                    @Parameter(name = "id", description = "Task ID to delete permanently", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task permanently deleted"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    public ResponseEntity<Void> permanentDeleteTask(@PathVariable Long id) {
        return toDoService.getTaskById(id)
                .map(task -> {
                    toDoService.permanentDeleteTask(id);
                    return ResponseEntity.noContent().<Void>build();
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
