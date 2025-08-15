package com.emobile.springtodo.controller;

import com.emobile.springtodo.integration.AbstractIntegrationToDoTest;
import com.emobile.springtodo.dto.ToDoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ToDoControllerTest extends AbstractIntegrationToDoTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private ToDoDto testDto;

  @BeforeEach
  void setup() {
    testDto = new ToDoDto();
    testDto.setTitle("Title JSON");
    testDto.setDescription("Desc JSON");
    testDto.setCompleted(false);
  }

  @Test
  @DisplayName("Создание новой задачи должно возвращать статус 201 Created")
  void testCreateToDo() throws Exception {
    mockMvc.perform(post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testDto)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Получение списка всех задач с пагинацией возвращает корректный JSON")
  void testGetAllToDos() throws Exception {
    String jsonResponse = mockMvc.perform(get("/api/todos")
            .param("limit", "10")
            .param("offset", "0"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    String expectedPartialJson = """
        [
          {
            "title":"Title1",
            "description":"Description1",
            "completed":false
          },
          {
            "title":"Title2",
            "description":"Description2",
            "completed":false
          },
          {
            "title":"Title3",
            "description":"Description3",
            "completed":false
          }
        ]
        """;

    JSONAssert.assertEquals(expectedPartialJson, jsonResponse, false);
  }

  @Test
  @DisplayName("Получение задачи по ID возвращает корректный объект")
  void testGetToDoById() throws Exception {
    String jsonResponse = mockMvc.perform(get("/api/todos/getToDo")
            .param("idToDo", "1"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    String expectedJson = """
        {
          "title":"Title1",
          "description":"Description1",
          "completed":false
        }
        """;

    JSONAssert.assertEquals(expectedJson, jsonResponse, false);
  }

  @Test
  @DisplayName("Удаление задачи по ID должно вернуть сообщение об успешном удалении")
  void testDeleteToDo() throws Exception {
    mockMvc.perform(delete("/api/todos")
            .param("idToDo", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("задача с id 2 успешно удалена"));
  }

  @Test
  @DisplayName("Обновление статуса выполнения задачи по ID должно вернуть сообщение об успешном обновлении")
  void testUpdateCompleted() throws Exception {
    mockMvc.perform(patch("/api/todos")
            .param("idToDo", "3")
            .param("completed", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("задача с id 3 успешно обновлена"));
  }
}

