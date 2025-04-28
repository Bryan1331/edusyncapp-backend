package com.arcangelcalderon.service.impl;

import com.arcangelcalderon.exception.RecursoNoEncontradoException;
import com.arcangelcalderon.model.Curso;
import com.arcangelcalderon.model.Estudiante;
import com.arcangelcalderon.repository.CursoRepository;
import com.arcangelcalderon.repository.EstudianteRepository;
import com.arcangelcalderon.service.CursoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;

    public CursoServiceImpl(CursoRepository cursoRepository, EstudianteRepository estudianteRepository) {
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    public List<Curso> getAllCursos() {
        return cursoRepository.findAll();
    }

    @Override
    public Curso getCursoById(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con ID: " + id));
    }

    @Override
    public Curso createCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    public Curso updateCurso(Long id, Curso curso) {
        Curso existingCurso = getCursoById(id);
        existingCurso.setNombre(curso.getNombre());
        existingCurso.setDescripcion(curso.getDescripcion());
        existingCurso.setCreditos(curso.getCreditos());
        return cursoRepository.save(existingCurso);
    }

    @Override
    public void deleteCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Curso no encontrado con ID: " + id);
        }
        cursoRepository.deleteById(id);
    }

    @Override
    public List<Estudiante> getEstudiantesInscritos(Long cursoId) {
        Curso curso = getCursoById(cursoId);
        return curso.getEstudiantesInscritos();
    }

    @Override
    @Transactional
    public void inscribirEstudiante(Long cursoId, Long estudianteId) {
        Curso curso = getCursoById(cursoId);
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + estudianteId));

        curso.getEstudiantesInscritos().add(estudiante);
        estudiante.getCursosInscritos().add(curso);
        cursoRepository.save(curso);
        estudianteRepository.save(estudiante);
    }

    @Override
    @Transactional
    public void desinscribirEstudiante(Long cursoId, Long estudianteId) {
        Curso curso = getCursoById(cursoId);
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + estudianteId));

        curso.getEstudiantesInscritos().remove(estudiante);
        estudiante.getCursosInscritos().remove(curso);
        cursoRepository.save(curso);
        estudianteRepository.save(estudiante);
    }
}