package com.arcangelcalderon.service.impl;

import com.arcangelcalderon.exception.RecursoNoEncontradoException;
import com.arcangelcalderon.model.Curso;
import com.arcangelcalderon.model.Estudiante;
import com.arcangelcalderon.repository.CursoRepository;
import com.arcangelcalderon.repository.EstudianteRepository;
import com.arcangelcalderon.service.EstudianteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, CursoRepository cursoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
    }

    @Override
    public List<Estudiante> getAllEstudiantes() {
        return estudianteRepository.findAll();
    }

    @Override
    public Estudiante getEstudianteById(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id));
    }

    @Override
    public Estudiante createEstudiante(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    @Override
    public Estudiante updateEstudiante(Long id, Estudiante estudiante) {
        Estudiante existingEstudiante = getEstudianteById(id);
        existingEstudiante.setNombre(estudiante.getNombre());
        existingEstudiante.setApellido(estudiante.getApellido());
        existingEstudiante.setEmail(estudiante.getEmail());
        existingEstudiante.setFechaNacimiento(estudiante.getFechaNacimiento());
        return estudianteRepository.save(existingEstudiante);
    }

    @Override
    public void deleteEstudiante(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id);
        }
        estudianteRepository.deleteById(id);
    }

    @Override
    public List<Curso> getCursosInscritos(Long estudianteId) {
        Estudiante estudiante = getEstudianteById(estudianteId);
        return estudiante.getCursosInscritos();
    }

    @Override
    @Transactional
    public void inscribirEnCurso(Long estudianteId, Long cursoId) {
        Estudiante estudiante = getEstudianteById(estudianteId);
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con ID: " + cursoId));

        estudiante.getCursosInscritos().add(curso);
        curso.getEstudiantesInscritos().add(estudiante);
        estudianteRepository.save(estudiante);
        cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void desinscribirDeCurso(Long estudianteId, Long cursoId) {
        Estudiante estudiante = getEstudianteById(estudianteId);
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con ID: " + cursoId));

        estudiante.getCursosInscritos().remove(curso);
        curso.getEstudiantesInscritos().remove(estudiante);
        estudianteRepository.save(estudiante);
        cursoRepository.save(curso);
    }
}