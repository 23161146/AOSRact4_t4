package com.example.demo.dto;

public record AuthResponse(String token, String tipo, long expiraEnMs) {}