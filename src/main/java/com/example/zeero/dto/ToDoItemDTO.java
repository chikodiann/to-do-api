package com.example.zeero.dto;

import com.example.zeero.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "Data Transfer Object for To-Do Items")
public class ToDoItemDTO {

    @NotBlank(message = "Title is required")
    @Schema(description = "Title of the task", example = "Complete API documentation")
    private String title;

    @Schema(description = "Detailed description of the task", example = "Write comprehensive documentation for the To-Do API endpoints.")
    private String description;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    @Schema(description = "Due date of the task", example = "2025-03-01")
    private LocalDate dueDate;

    @NotNull(message = "Priority level is required")
    @Schema(description = "Priority level of the task", example = "HIGH")
    private Priority priority;

    @Schema(description = "Indicates whether the task is completed", example = "false")
    private boolean completed;
}
