package com.example.demo.controller;

import com.example.demo.dto.LibroPatchRequest;
import com.example.demo.dto.LibroRequest;
import com.example.demo.dto.LibroResponse;
import com.example.demo.dto.PageResponse;
import com.example.demo.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public PageResponse<LibroResponse> listar(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return libroService.listar(page, size);
    }

    @GetMapping("/{id}")
    public LibroResponse obtener(@PathVariable Long id) { return libroService.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibroResponse crear(@Valid @RequestBody LibroRequest req) { return libroService.crear(req); }

    @PutMapping("/{id}")
    public LibroResponse actualizar(@PathVariable Long id, @Valid @RequestBody LibroRequest req) {
        return libroService.actualizar(id, req);
    }

    @PatchMapping("/{id}")
    public LibroResponse parcial(@PathVariable Long id, @Valid @RequestBody LibroPatchRequest req) {
        return libroService.actualizarParcial(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { libroService.eliminar(id); }
}