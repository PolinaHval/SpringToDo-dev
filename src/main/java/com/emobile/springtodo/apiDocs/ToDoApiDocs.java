package com.emobile.springtodo.apiDocs;

import com.emobile.springtodo.dto.ToDoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Tag(name = "Контроллер для работы с задачами", description = "Позволяет создавать, удалять и выводить список задач")
public interface ToDoApiDocs {

  @Operation(summary = "Создание задачи")
  void createToDo(final ToDoDto toDoDto);

  @Operation(summary = "Вывод список задач")
  List<ToDoDto> doDtoList(@Parameter(description = "Максимальное количество задач, возвращаемых в ответе")
                          int limit,
                          @Parameter(description = "Количество задач, которые нужно пропустить(смещение для пагинации)")
                          int offset);

  @Operation(summary = "Удаление задачи")
  Map<String, String> deleteToDo(long idToDo);

  @Operation(summary = "Вывод задачи по заданному id")
  ToDoDto getToDoDto(long idToDo);

  @Operation(summary = "Внесение измененния в поле completed")
  Map<String, String> updateCompleted(@Parameter(description = "ID задачи, которой необходимо изменить поле completed") long idToDo,
                      @Parameter(description = "Новое значение") boolean completed);
}
