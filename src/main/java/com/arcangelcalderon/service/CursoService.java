package com.arcangelcalderon.service;

import com.arcangelcalderon.model.Curso;
import com.arcangelcalderon.model.Estudiante;
import java.util.List;

public interface CursoService {
    List<Curso> getAllCursos();
    Curso getCursoById(Long id);
    Curso createCurso(Curso curso);
    Curso updateCurso(Long id, Curso curso);
    void deleteCurso(Long id);
    List<Estudiante> getEstudiantesInscritos(Long cursoId);
    void inscribirEstudiante(Long cursoId, Long estudianteId);
    void desinscribirEstudiante(Long cursoId, Long estudianteId);
}