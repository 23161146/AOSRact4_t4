package com.example.demo.service;

import com.example.demo.dto.LibroPatchRequest;
import com.example.demo.dto.LibroRequest;
import com.example.demo.dto.LibroResponse;
import com.example.demo.dto.PageResponse;
import com.example.demo.model.Autor;
import com.example.demo.model.Libro;
import com.example.demo.repository.AutorRepository;
import com.example.demo.repository.LibroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class LibroService {

    private static final int TAMANIO_MAXIMO = 50;

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<LibroResponse> listar(int page, int size) {
        if (page < 0 || size < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "page debe ser 0 o mayor y size debe ser 1 o mayor");
        }
        Pageable pageable = PageRequest.of(page, Math.min(size, TAMANIO_MAXIMO), Sort.by("id"));
        Page<LibroResponse> p = libroRepository.findAll(pageable).map(this::aRespuesta);
        return new PageResponse<>(p.getContent(), p.getNumber(), p.getSize(),
                p.getTotalElements(), p.getTotalPages());
    }

    @Transactional(readOnly = true)
    public LibroResponse obtener(Long id) {
        return aRespuesta(buscarLibro(id));
    }

    public LibroResponse crear(LibroRequest req) {
        Libro libro = new Libro();
        libro.setTitulo(req.titulo().trim());
        libro.setAnioPublicacion(req.anioPublicacion());
        libro.setAutor(buscarAutor(req.autorId()));
        return aRespuesta(libroRepository.save(libro));
    }

    public LibroResponse actualizar(Long id, LibroRequest req) {
        Libro libro = buscarLibro(id);
        libro.setTitulo(req.titulo().trim());
        libro.setAnioPublicacion(req.anioPublicacion());
        libro.setAutor(buscarAutor(req.autorId()));
        return aRespuesta(libroRepository.save(libro));
    }

    public LibroResponse actualizarParcial(Long id, LibroPatchRequest req) {
        Libro libro = buscarLibro(id);
        if (req.titulo() != null) {
            if (req.titulo().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título no puede estar vacío");
            }
            libro.setTitulo(req.titulo().trim());
        }
        if (req.anioPublicacion() != null) {
            libro.setAnioPublicacion(req.anioPublicacion());
        }
        if (req.autorId() != null) {
            libro.setAutor(buscarAutor(req.autorId()));
        }
        return aRespuesta(libroRepository.save(libro));
    }

    public void eliminar(Long id) {
        libroRepository.delete(buscarLibro(id));
    }

    private Libro buscarLibro(Long id) {
        return libroRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro " + id + " no existe"));
    }

    // 422: el JSON es válido, pero el autor referenciado no existe.
    private Autor buscarAutor(Long id) {
        return autorRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatusCode.valueOf(422), "El autor " + id + " no existe"));
    }

    private LibroResponse aRespuesta(Libro l) {
        return new LibroResponse(l.getId(), l.getTitulo(), l.getAnioPublicacion(),
                l.getAutor().getId(), l.getAutor().getNombre());
    }
}