package com.example.demo.repository;

import com.example.demo.model.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByAutorId(Long autorId);

    @Override
    @EntityGraph(attributePaths = "autor")
    Page<Libro> findAll(Pageable pageable);
}