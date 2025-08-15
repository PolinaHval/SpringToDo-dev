package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.mapper.ToDoMapper;
import com.emobile.springtodo.metrics.ToDoMetrics;
import com.emobile.springtodo.model.ToDo;
import com.emobile.springtodo.repository.ToDoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public void save(ToDoDto toDoDto){
    ToDo entity = toDoMapper.toEntity(toDoDto);
    toDoRepository.save(entity);
    toDoMetrics.incrementCreatedTasks();
  }

  @Transactional(readOnly = true)
  public List<ToDoDto> findAll(int limit, int offset){
    Pageable pageable = PageRequest.of(offset / limit, limit);
    return toDoRepository.findAll(pageable).getContent().stream().map(toDoMapper::toDto).toList();
  }

  @Transactional
  @CacheEvict(value = "todo")
  public void deleteToDo(long idToDo){
    ToDo toDo = toDoRepository.findById(idToDo)
        .orElseThrow(() -> new TaskNotFoundException("Задача с id " + idToDo +" не найдена"));

    toDoRepository.delete(toDo);
  }

  @Cacheable(value = "todo")
  @Transactional(readOnly = true)
  public ToDoDto getToDo(long idToDo){
    ToDo toDo = toDoRepository.findById(idToDo)
        .orElseThrow(() -> new TaskNotFoundException("Задача с id " + idToDo +" не найдена"));
    return toDoMapper.toDto(toDo);
  }

  @Transactional
  public void updateCompleted(long idToDo, boolean completed){
    ToDo toDo = toDoRepository.findById(idToDo)
        .orElseThrow(() -> new TaskNotFoundException("Задача с id " + idToDo +" не найдена"));

    toDo.setCompleted(completed);
    toDoRepository.save(toDo);

     toDoMetrics.incrementCompletedTasks();
  }
}
