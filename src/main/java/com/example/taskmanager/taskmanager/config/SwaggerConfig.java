package com.example.taskmanager.taskmanager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Task Manager System API",
                version = "1.0",
                description = "Spring Boot Application to manage tasks"
        )
)
public class SwaggerConfig {
}
