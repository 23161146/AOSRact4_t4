package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LibroRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede pasar de 150 caracteres")
        String titulo,

        @Min(value = 0, message = "El año no puede ser negativo")
        @Max(value = 2100, message = "El año no puede ser mayor a 2100")
        Integer anioPublicacion,

        @NotNull(message = "autorId es obligatorio")
        Long autorId) {}