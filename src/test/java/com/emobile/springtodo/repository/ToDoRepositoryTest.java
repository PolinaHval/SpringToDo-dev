package com.emobile.springtodo.repository;

import com.emobile.springtodo.integration.AbstractIntegrationToDoTest;
import com.emobile.springtodo.model.ToDo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ToDoRepository.class)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ToDoRepositoryTest extends AbstractIntegrationToDoTest {

  @Autowired
  private ToDoRepository toDoRepository;

  @Test
  @DisplayName("Сохраняет новую задачу и проверяет корректность сохранения")
  void saveToDo(){
    ToDo toDo = new ToDo();
    toDo.setId(1L);
    toDo.setTitle("Title test");
    toDo.setDescription("Description test");
    toDo.setCompleted(false);

    toDoRepository.save(toDo);

    List<ToDo> all = toDoRepository.findAll(10, 0);
    ToDo saved = all.get(all.size() - 1);

    assertEquals(toDo.getTitle(), saved.getTitle());
    assertEquals("Description test", saved.getDescription());
    assertFalse(saved.isCompleted());
  }

  @Test
  @DisplayName("Получает все задачи с ограничением и смещением")
  void findAllTest(){
    List<ToDo> todos = toDoRepository.findAll(10, 0);

    assertNotNull(todos);
    assertEquals(3, todos.size());
  }

  @Test
  @DisplayName("Удаляет задачу по ID и проверяет, что она удалена")
  void testDelete() {
    int rowsAffected = toDoRepository.delete(1L);

    assertEquals(1, rowsAffected);

    ToDo deleted = toDoRepository.getToDoById(1L);

    assertNull(deleted);
  }

  @Test
  @DisplayName("Получает задачу по существующему ID")
  void testGetToDoByIdExists() {
    ToDo todo = toDoRepository.getToDoById(2L);

    assertNotNull(todo);
    assertEquals("Title2", todo.getTitle());
  }

  @Test
  @DisplayName("Получение задачи по несуществующему ID возвращает null")
  void testGetToDoByIdNotExists() {
    ToDo todo = toDoRepository.getToDoById(20L);

    assertNull(todo);
  }

  @Test
  @DisplayName("Обновляет статус completed задачи и проверяет обновление")
  void testUpdateCompleted() {
    int rowsUpdated = toDoRepository.updateCompleted(2L, true);

    assertEquals(1, rowsUpdated);

    ToDo updated = toDoRepository.getToDoById(2L);

    assertTrue(updated.isCompleted());
  }
}
