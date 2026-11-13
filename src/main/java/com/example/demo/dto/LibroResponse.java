package com.example.demo.dto;

public record LibroResponse(Long id, String titulo, Integer anioPublicacion,
                            Long autorId, String autorNombre) {}
