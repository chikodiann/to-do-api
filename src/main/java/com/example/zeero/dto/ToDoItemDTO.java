package com.example.zeero.dto;
import com.example.zeero.enums.Priority;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class ToDoItemDTO {
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private boolean completed;
    private boolean isDeleted = false;
}
