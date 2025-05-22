package com.arcangelcalderon.service.impl;

import com.arcangelcalderon.exception.RecursoNoEncontradoException;
import com.arcangelcalderon.model.Classroom;
import com.arcangelcalderon.repository.ClassroomRepository;
import com.arcangelcalderon.service.ClassroomService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public Classroom getClassroomById(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Aula no encontrada con ID: " + id));
    }

    @Override
    public Classroom createClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom updateClassroom(Long id, Classroom classroom) {
        Classroom existingClassroom = getClassroomById(id);
        existingClassroom.setName(classroom.getName());
        existingClassroom.setCapacity(classroom.getCapacity());
        existingClassroom.setLocation(classroom.getLocation());
        existingClassroom.setCourse(classroom.getCourse());
        return classroomRepository.save(existingClassroom);
    }

    @Override
    public void deleteClassroom(Long id) {
        if (!classroomRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Aula no encontrada con ID: " + id);
        }
        classroomRepository.deleteById(id);
    }
}