package com.emobile.springtodo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "ToDo", version = "1.0",
    description = "Приложение по управлению задачами"))
@Configuration
public class SwaggerConfig {
}
