package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.mapper.ToDoMapper;
import com.emobile.springtodo.metrics.ToDoMetrics;
import com.emobile.springtodo.model.ToDo;
import com.emobile.springtodo.repository.ToDoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ToDoServiceTest {

  @Mock
  private ToDoRepository toDoRepository;

  @Mock
  private ToDoMapper toDoMapper;

  @Mock
  private ToDoMetrics toDoMetrics;

  @InjectMocks
  private ToDoService toDoService;

  @Test
  @DisplayName("Успешное сохранение задачи")
  void save_success() {
    ToDoDto dto = new ToDoDto();
    ToDo entity = new ToDo();

    when(toDoMapper.toEntity(dto)).thenReturn(entity);

    toDoService.save(dto);

    verify(toDoRepository).save(entity);
    verify(toDoMetrics).incrementCreatedTasks();
  }

  @Test
  @DisplayName("Успешное получение списка задач")
  void findAll_success() {
    ToDo entity = new ToDo();
    ToDoDto dto = new ToDoDto();

    when(toDoRepository.findAll()).thenReturn(List.of(entity));
    when(toDoMapper.toDto(entity)).thenReturn(dto);

    List<ToDoDto> result = toDoRepository.findAll().stream()
        .map(toDoMapper::toDto)
        .toList();

    assertEquals(1, result.size());
    assertSame(dto, result.get(0));
  }

  @Test
  @DisplayName("Успешное удаление задачи")
  void deleteToDo_success() {
    long id = 1L;
    ToDo entity = new ToDo();
    when(toDoRepository.findById(id)).thenReturn(Optional.of(entity));

    toDoService.deleteToDo(id);

    verify(toDoRepository).delete(entity);
  }

  @Test
  @DisplayName("Неудачное удаление задачи. Здача с id не найдена")
  void deleteToDo_notFound() {
    when(toDoRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> toDoService.deleteToDo(1L));
  }

  @Test
  @DisplayName("Успешное получение задачи")
  void getToDo_success() {
    long id = 1L;
    ToDo entity = new ToDo();
    ToDoDto dto = new ToDoDto();

    when(toDoRepository.findById(id)).thenReturn(Optional.of(entity));
    when(toDoMapper.toDto(entity)).thenReturn(dto);

    ToDoDto result = toDoService.getToDo(id);

    assertSame(dto, result);
  }

  @Test
  @DisplayName("Задача с id не найдена")
  void getToDo_notFound() {
    when(toDoRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> toDoService.getToDo(1L));
  }

  @Test
  @DisplayName("Успешное обновление поля completed")
  void updateCompleted_success() {
    long id = 1L;
    ToDo entity = new ToDo();
    entity.setCompleted(false);

    when(toDoRepository.findById(id)).thenReturn(Optional.of(entity));

    toDoService.updateCompleted(id, true);

    assertTrue(entity.isCompleted());
    verify(toDoRepository).save(entity);
    verify(toDoMetrics).incrementCompletedTasks();
  }

  @Test
  @DisplayName("Неудачное обновление задачи. Задача не найдена")
  void updateCompleted_notFound() {
    long id = 1L;
    when(toDoRepository.findById(id)).thenReturn(Optional.empty());

    TaskNotFoundException exception = assertThrows(
        TaskNotFoundException.class,
        () -> toDoService.updateCompleted(id, true)
    );
    assertEquals("Задача с id " + id + " не найдена", exception.getMessage());
  }
}

