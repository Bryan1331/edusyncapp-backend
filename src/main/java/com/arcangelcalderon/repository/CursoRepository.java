package com.arcangelcalderon.repository;

import com.arcangelcalderon.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}