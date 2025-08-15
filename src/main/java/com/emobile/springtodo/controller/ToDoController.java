package com.emobile.springtodo.controller;

import com.emobile.springtodo.apiDocs.ToDoApiDocs;
import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.service.ToDoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class ToDoController implements ToDoApiDocs {
  private final ToDoService toDoService;

  public ToDoController(ToDoService toDoService) {
    this.toDoService = toDoService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Override
  public void createToDo(@RequestBody @Valid ToDoDto toDoDto) {
    toDoService.save(toDoDto);
  }

  @GetMapping
  @Override
  public List<ToDoDto> doDtoList(@RequestParam(defaultValue = "10") int limit,
                                 @RequestParam(defaultValue = "0") int offset) {
    return toDoService.findAll(limit,offset);
  }

  @DeleteMapping
  @Override
  public Map<String, String> deleteToDo(@RequestParam long idToDo) {
    toDoService.deleteToDo(idToDo);
    return Map.of("message", "задача с id " + idToDo + " успешно удалена");
  }

  @GetMapping("/getToDo")
  @Override
  public ToDoDto getToDoDto(@RequestParam long idToDo) {
    return toDoService.getToDo(idToDo);
  }

  @PatchMapping
  @Override
  public Map<String, String> updateCompleted(@RequestParam long idToDo, @RequestParam boolean completed) {
    toDoService.updateCompleted(idToDo,completed);
    return Map.of("message", "задача с id " + idToDo + " успешно обновлена");
  }
}

