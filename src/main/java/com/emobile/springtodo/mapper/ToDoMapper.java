package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.model.ToDo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ToDoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "completed", target = "completed")
  ToDo toEntity(ToDoDto toDoDto);

  ToDoDto toDto(ToDo toDo);
}
