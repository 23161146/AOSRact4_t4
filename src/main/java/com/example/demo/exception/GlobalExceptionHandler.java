package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> detalles = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            detalles.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }
        return responder(HttpStatusCode.valueOf(400), "Datos inválidos", detalles);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> jsonInvalido(HttpMessageNotReadableException ex) {
        return responder(HttpStatusCode.valueOf(400),
                "El cuerpo no es un JSON válido o tiene tipos incorrectos", Map.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return responder(HttpStatusCode.valueOf(400),
                "El parámetro '" + ex.getName() + "' tiene un valor inválido", Map.of());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> estado(ResponseStatusException ex) {
        return responder(ex.getStatusCode(), ex.getReason(), Map.of());
    }

    private ResponseEntity<ErrorResponse> responder(HttpStatusCode status, String mensaje,
                                                    Map<String, String> detalles) {
        HttpStatus hs = HttpStatus.resolve(status.value());
        String error = hs != null ? hs.getReasonPhrase() : "Error";
        return ResponseEntity.status(status)
                .body(new ErrorResponse(Instant.now(), status.value(), error, mensaje, detalles));
    }
}