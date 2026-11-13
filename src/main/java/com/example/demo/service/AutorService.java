package com.example.demo.service;

import com.example.demo.model.Autor;
import com.example.demo.repository.AutorRepository;
import com.example.demo.repository.LibroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;

    public AutorService(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public List<Autor> listar() {
        return autorRepository.findAll();
    }

    public Autor crear(Autor autor) {
        if (autor.getNombre() == null || autor.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
        autor.setId(null);
        return autorRepository.save(autor);
    }

    public void eliminar(Long id) {
        if (!autorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor " + id + " no existe");
        }
        if (libroRepository.existsByAutorId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El autor tiene libros asociados");
        }
        autorRepository.deleteById(id);
    }
}