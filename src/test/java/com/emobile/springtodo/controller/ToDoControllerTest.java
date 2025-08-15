package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.service.ToDoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToDoController.class)
class ToDoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ToDoService toDoService;

  @Test
  @DisplayName("Создание задачи - POST /api/todos")
  void createToDo_success() throws Exception {
    ToDoDto dto = new ToDoDto();
    dto.setTitle("Test Task");
    dto.setDescription("Test Description");
    dto.setCompleted(false);

    doNothing().when(toDoService).save(any(ToDoDto.class));

    mockMvc.perform(post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Получение списка задач - GET /api/todos")
  void getAllToDos_success() throws Exception {
    ToDoDto dto1 = new ToDoDto("Task 1", "Desc 1", false);
    ToDoDto dto2 = new ToDoDto("Task 2", "Desc 2", true);

    when(toDoService.findAll(10,0)).thenReturn(List.of(dto1, dto2));

    mockMvc.perform(get("/api/todos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].title").value("Task 1"))
        .andExpect(jsonPath("$[1].completed").value(true));
  }

  @Test
  @DisplayName("Удаление задачи - DELETE /api/todos?idToDo=1")
  void deleteToDo_success() throws Exception {
    doNothing().when(toDoService).deleteToDo(1L);

    mockMvc.perform(delete("/api/todos")
            .param("idToDo", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("задача с id 1 успешно удалена"));
  }

  @Test
  @DisplayName("Получение задачи по id - GET /api/todos/getToDo?idToDo=1")
  void getToDo_success() throws Exception {
    ToDoDto dto = new ToDoDto("Task 1", "Desc 1", false);

    when(toDoService.getToDo(1L)).thenReturn(dto);

    mockMvc.perform(get("/api/todos/getToDo")
            .param("idToDo", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Task 1"))
        .andExpect(jsonPath("$.description").value("Desc 1"));
  }

  @Test
  @DisplayName("Обновление completed - PATCH /api/todos?idToDo=1&completed=true")
  void updateCompleted_success() throws Exception {
    doNothing().when(toDoService).updateCompleted(1L, true);

    mockMvc.perform(patch("/api/todos")
            .param("idToDo", "1")
            .param("completed", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("задача с id 1 успешно обновлена"));
  }
}
