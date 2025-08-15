package com.emobile.springtodo.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ToDoMetrics {

  private final MeterRegistry meterRegistry;
  private Counter createdToDoCounter;
  private Counter completedToDoCounter;

  public ToDoMetrics(MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
  }

  @PostConstruct
  public void init() {
    createdToDoCounter = Counter.builder("todo.tasks.created")
        .description("Количество созданных задач")
        .register(meterRegistry);

    completedToDoCounter = Counter.builder("todo.tasks.completed")
        .description("Количество завершённых задач")
        .register(meterRegistry);
  }

  public void incrementCreatedTasks() {
    createdToDoCounter.increment();
  }

  public void incrementCompletedTasks() {
    completedToDoCounter.increment();
  }
}

