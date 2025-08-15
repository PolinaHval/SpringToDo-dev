package com.emobile.springtodo.repository;

import com.emobile.springtodo.integration.AbstractIntegrationToDoTest;
import com.emobile.springtodo.model.ToDo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ToDoRepositoryTest extends AbstractIntegrationToDoTest {

  @Autowired
  private ToDoRepository toDoRepository;

  @Test
  @DisplayName("Успешное сохранение задачи")
  void saveToDo_success() {
    ToDo todo = new ToDo();
    todo.setTitle("Test Task");
    todo.setDescription("Description");
    todo.setCompleted(false);

    ToDo saved = toDoRepository.save(todo);

    assertNotNull(saved.getId());
    assertEquals("Test Task", saved.getTitle());
    assertFalse(saved.isCompleted());
  }

  @Test
  @DisplayName("Успешный поиск задачи по id")
  void findById_success() {
    ToDo todo = new ToDo();
    todo.setTitle("Find Task");
    todo.setDescription("Description");
    ToDo saved = toDoRepository.save(todo);

    Optional<ToDo> found = toDoRepository.findById(saved.getId());
    assertTrue(found.isPresent());
    assertEquals("Find Task", found.get().getTitle());
  }

  @Test
  @DisplayName("Успешное удаление задач")
  void deleteToDo_success() {
    ToDo todo = new ToDo();
    todo.setTitle("Delete Task");
    todo.setDescription("Description");
    ToDo saved = toDoRepository.save(todo);

    toDoRepository.delete(saved);

    Optional<ToDo> found = toDoRepository.findById(saved.getId());
    assertTrue(found.isEmpty());
  }

  @Test
  @DisplayName("Успешное получение списка задач")
  void findAll_success() {
    ToDo todo1 = new ToDo();
    todo1.setTitle("Task 1");
    todo1.setDescription("Description First");
    ToDo todo2 = new ToDo();
    todo2.setDescription("Description Second");
    todo2.setTitle("Task 2");

    toDoRepository.save(todo1);
    toDoRepository.save(todo2);

    List<ToDo> todos = toDoRepository.findAll();
    assertEquals(2, todos.size());
  }
}

