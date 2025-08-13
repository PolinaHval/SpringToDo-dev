package com.emobile.springtodo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ToDoDto {
  @NotBlank(message = "Поле не может быть пустым")
  String title;
  @NotBlank(message = "Поле не может быть пустым")
  String description;
  @NotNull(message = "Поле обязательно для заполнения")
  Boolean completed;

  public ToDoDto(String title, String description, boolean completed) {
    this.title = title;
    this.description = description;
    this.completed = completed;
  }

  public ToDoDto() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }
}
