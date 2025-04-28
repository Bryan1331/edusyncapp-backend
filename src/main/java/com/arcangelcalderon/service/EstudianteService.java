package com.arcangelcalderon.service;

import com.arcangelcalderon.model.Curso;
import com.arcangelcalderon.model.Estudiante;
import java.util.List;

public interface EstudianteService {
    List<Estudiante> getAllEstudiantes();
    Estudiante getEstudianteById(Long id);
    Estudiante createEstudiante(Estudiante estudiante);
    Estudiante updateEstudiante(Long id, Estudiante estudiante);
    void deleteEstudiante(Long id);
    List<Curso> getCursosInscritos(Long estudianteId);
    void inscribirEnCurso(Long estudianteId, Long cursoId);
    void desinscribirDeCurso(Long estudianteId, Long cursoId);
}