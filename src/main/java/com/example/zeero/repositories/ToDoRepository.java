package com.example.zeero.repositories;


import com.example.zeero.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDoItem, Long> {
    List<ToDoItem> findByCompleted(boolean completed);
    List<ToDoItem> findByIsDeletedFalse(); // Fetch only active tasks
}

