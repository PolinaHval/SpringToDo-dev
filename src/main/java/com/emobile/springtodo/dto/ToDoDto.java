package com.emobile.springtodo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
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
}
