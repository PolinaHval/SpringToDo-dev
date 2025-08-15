package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.model.ToDo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ToDoMapper {
  @Mapping(target = "id", ignore = true)
  ToDo toEntity(ToDoDto toDoDto);

  ToDoDto toDto(ToDo toDo);
}

