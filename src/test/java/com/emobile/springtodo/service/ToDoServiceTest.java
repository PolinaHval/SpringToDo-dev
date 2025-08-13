package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.mapper.ToDoMapper;
import com.emobile.springtodo.metrics.ToDoMetrics;
import com.emobile.springtodo.model.ToDo;
import com.emobile.springtodo.repository.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

  private ToDoDto sampleDto;
  private ToDo sampleEntity;

  @BeforeEach
  void setUp() {
    sampleDto = new ToDoDto("Title", "Test", false);
    sampleEntity = new ToDo(1L, "Title", "Test", false);
  }

  @Test
  @DisplayName("Метод должен сохранять задачу и обновлять метрику созданных задач")
  void saveShouldCallRepositoryAndMetrics() {
    when(toDoMapper.toEntity(sampleDto)).thenReturn(sampleEntity);

    toDoService.save(sampleDto);

    verify(toDoMapper).toEntity(sampleDto);
    verify(toDoRepository).save(sampleEntity);
    verify(toDoMetrics).incrementCreatedTasks();
  }

  @Test
  @DisplayName("Метод должен возвращать список задач")
  void findAllShouldReturnMappedDto() {
    when(toDoRepository.findAll(10, 0)).thenReturn(List.of(sampleEntity));
    when(toDoMapper.toDto(sampleEntity)).thenReturn(sampleDto);

    List<ToDoDto> resultList = toDoService.findAll(10, 0);

    assertEquals(1, resultList.size());
    assertEquals(sampleDto, resultList.get(0));
  }

  @Test
  @DisplayName("Метод должен вернуть TaskNotFoundException, если задача с id не найдена")
  void deleteShouldThrowExceptionWhenNotFound() {
   when(toDoRepository.delete(1L)).thenReturn(0);

    TaskNotFoundException resultException = assertThrows(TaskNotFoundException.class,
        () -> toDoService.deleteToDo(1L));

    assertEquals("Задача с id 1 не существует", resultException.getMessage());
  }

  @Test
  @DisplayName("Метод должен вернуть TaskNotFoundException, если задача с id не найдена")
  void getToDoShouldThrowExceptionWhenNotFound() {
    when(toDoRepository.getToDoById(1L)).thenReturn(null);

    TaskNotFoundException resultException = assertThrows(TaskNotFoundException.class,
        () -> toDoService.getToDo(1L));

    assertEquals("Задача с id 1 не существует", resultException.getMessage());
  }

  @Test
  @DisplayName("Метод должен вернуть задачу по id")
  void getToDoShouldReturnDtoWhenFound() {
    when(toDoRepository.getToDoById(1L)).thenReturn(sampleEntity);
    when(toDoMapper.toDto(sampleEntity)).thenReturn(sampleDto);

    ToDoDto result = toDoService.getToDo(1L);

    assertEquals(sampleDto, result);
  }

  @Test
  @DisplayName("Метод должен обновить статус задачи " +
      "и инкрементировать метрику завершённых задач при успешном обновлении")
  void updateCompletedShouldUpdateAndIncrementMetric() {
    when(toDoRepository.updateCompleted(1L, true)).thenReturn(1);

    toDoService.updateCompleted(1L, true);

    verify(toDoRepository).updateCompleted(1L,true);
    verify(toDoMetrics).incrementCompletedTasks();
  }

  @Test
  @DisplayName("Метод должен вернуть TaskNotFoundException, если задача с id не найдена")
  void updateCompletedShouldThrowExceptionWhenNotFound() {
    when(toDoRepository.updateCompleted(1L, true)).thenReturn(0);

    TaskNotFoundException resultException = assertThrows(TaskNotFoundException.class,
        () -> toDoService.updateCompleted(1L, true));

    assertEquals("Задача с id 1 не существует", resultException.getMessage());
  }
}
