package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.mapper.ToDoMapper;
import com.emobile.springtodo.metrics.ToDoMetrics;
import com.emobile.springtodo.model.ToDo;
import com.emobile.springtodo.repository.ToDoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {
  private final ToDoRepository toDoRepository;
  private final ToDoMapper toDoMapper;
  private final ToDoMetrics toDoMetrics;

  public ToDoService(ToDoRepository toDoRepository, ToDoMapper toDoMapper, ToDoMetrics toDoMetrics) {
    this.toDoRepository = toDoRepository;
    this.toDoMapper = toDoMapper;
    this.toDoMetrics = toDoMetrics;
  }

  public void save(ToDoDto toDoDto){
    ToDo entity = toDoMapper.toEntity(toDoDto);

    toDoRepository.save(entity);
    toDoMetrics.incrementCreatedTasks();
  }

  public List<ToDoDto> findAll(int limit, int offset){
    List<ToDo> todos = toDoRepository.findAll(limit,offset);
    return todos.stream().map(toDoMapper::toDto).toList();
  }

  @CacheEvict(value = "todo")
  public void deleteToDo(long idToDo){
    ToDo toDo = toDoRepository.getToDoById(idToDo);

    if(toDo == null){
      throw new TaskNotFoundException("Задача с id " + idToDo + " не существует");
    }

    toDoRepository.delete(idToDo);
  }

  @Cacheable(value = "todo")
  public ToDoDto getToDo(long idToDo){
    ToDo toDo = toDoRepository.getToDoById(idToDo);

    if(toDo == null){
      throw new TaskNotFoundException("Задача с id " + idToDo + " не существует");
    }

    return toDoMapper.toDto(toDo);
  }

  public void updateCompleted(long idToDo, boolean completed){
    ToDo toDo = toDoRepository.getToDoById(idToDo);

    if(toDo == null){
      throw new TaskNotFoundException("Задача с id " + idToDo + " не существует");
    }

    toDoRepository.updateCompleted(idToDo,completed);

    toDoMetrics.incrementCompletedTasks();
  }
}
