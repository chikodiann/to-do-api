package com.example.zeero.repositories;

import com.example.zeero.enums.Priority;
import com.example.zeero.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDoItem, Long> {
    List<ToDoItem> findByDeletedFalse(); // Fetch only active (not deleted) tasks
    List<ToDoItem> findByDeletedFalseOrderByDueDateAsc(); // Sort by due date
    List<ToDoItem> findByDeletedFalseAndPriority(Priority priority); // Filter by priority
    List<ToDoItem> findByDeletedFalseAndCompleted(boolean completed); // Filter by completion status
}
