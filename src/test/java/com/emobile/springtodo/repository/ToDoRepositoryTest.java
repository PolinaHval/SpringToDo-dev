package com.emobile.springtodo.repository;

import com.emobile.springtodo.config.HibernateUtil;
import com.emobile.springtodo.model.ToDo;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ToDoRepositoryTest {

  private ToDoRepository repository;

  @BeforeAll
  void setup() {
    repository = new ToDoRepository();
  }

  @AfterAll
  void teardown() {
    HibernateUtil.closeSessionFactory();
  }

  @BeforeEach
  void cleanDb() {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      Transaction tx = session.beginTransaction();
      session.createQuery("DELETE FROM ToDo").executeUpdate();
      tx.commit();
    }
  }

  @Test
  @DisplayName("Сохранение и получение задачи по ID")
  void testSaveAndGetToDo() {
    ToDo todo = new ToDo();
    todo.setTitle("Tittle");
    todo.setCompleted(false);

    repository.save(todo);
    assertNotNull(todo.getId());

    ToDo loaded = repository.getToDoById(todo.getId());
    assertEquals("Tittle", loaded.getTitle());
    assertFalse(loaded.isCompleted());
  }

  @Test
  @DisplayName("Обновление статуса задачи на выполнено")
  void testUpdateCompleted() {
    ToDo todo = new ToDo();
    todo.setTitle("Update");
    todo.setCompleted(false);
    repository.save(todo);

    repository.updateCompleted(todo.getId(), true);
    ToDo updated = repository.getToDoById(todo.getId());
    assertTrue(updated.isCompleted());
  }

  @Test
  @DisplayName("Удаление задачи")
  void testDelete() {
    ToDo todo = new ToDo();
    todo.setTitle("Delete");
    repository.save(todo);

    repository.delete(todo.getId());
    assertNull(repository.getToDoById(todo.getId()));
  }

  @Test
  @DisplayName("Получение задач с пагинацией")
  void testFindAllWithPagination() {
    for (int i = 1; i <= 5; i++) {
      ToDo todo = new ToDo();
      todo.setTitle("Task " + i);
      todo.setCompleted(false);
      repository.save(todo);
    }

    List<ToDo> firstThree = repository.findAll(3, 0);
    assertEquals(3, firstThree.size());
    assertEquals("Task 1", firstThree.get(0).getTitle());
    assertEquals("Task 2", firstThree.get(1).getTitle());
    assertEquals("Task 3", firstThree.get(2).getTitle());

    List<ToDo> nextTwo = repository.findAll(3, 3);
    assertEquals(2, nextTwo.size());
    assertEquals("Task 4", nextTwo.get(0).getTitle());
    assertEquals("Task 5", nextTwo.get(1).getTitle());
  }
}
