package com.arcangelcalderon.controller;

import com.arcangelcalderon.assembler.EstudianteModelAssembler;
import com.arcangelcalderon.model.Curso;
import com.arcangelcalderon.model.Estudiante;
import com.arcangelcalderon.service.EstudianteService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;
    private final EstudianteModelAssembler assembler;

    public EstudianteController(EstudianteService estudianteService, EstudianteModelAssembler assembler) {
        this.estudianteService = estudianteService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Estudiante>> getAllEstudiantes() {
        List<EntityModel<Estudiante>> estudiantes = estudianteService.getAllEstudiantes().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(estudiantes,
                linkTo(methodOn(EstudianteController.class).getAllEstudiantes()).withSelfRel(),
                linkTo(methodOn(EstudianteController.class).createEstudiante(null)).withRel("crear"));
    }

    @GetMapping("/{id}")
    public EntityModel<Estudiante> getEstudianteById(@PathVariable Long id) {
        Estudiante estudiante = estudianteService.getEstudianteById(id);
        return assembler.toModel(estudiante);
    }

    @PostMapping
    public ResponseEntity<?> createEstudiante(@RequestBody Estudiante nuevoEstudiante) {
        EntityModel<Estudiante> entityModel = assembler.toModel(estudianteService.createEstudiante(nuevoEstudiante));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEstudiante(@PathVariable Long id, @RequestBody Estudiante estudianteActualizado) {
        Estudiante estudiante = estudianteService.updateEstudiante(id, estudianteActualizado);
        EntityModel<Estudiante> entityModel = assembler.toModel(estudiante);

        return ResponseEntity
                .ok()
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEstudiante(@PathVariable Long id) {
        estudianteService.deleteEstudiante(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cursos")
    public CollectionModel<EntityModel<Curso>> getCursosInscritos(@PathVariable Long id) {
        Estudiante estudiante = estudianteService.getEstudianteById(id);
        List<EntityModel<Curso>> cursos = estudiante.getCursosInscritos().stream()
                .map(curso -> EntityModel.of(curso,
                        linkTo(methodOn(CursoController.class).getCursoById(curso.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(cursos,
                linkTo(methodOn(EstudianteController.class).getCursosInscritos(id)).withSelfRel(),
                linkTo(methodOn(EstudianteController.class).inscribirEnCurso(id, null)).withRel("inscribir")); // El cursoId se pasará en el POST
    }

    @PostMapping("/{id}/cursos/{cursoId}")
    public ResponseEntity<?> inscribirEnCurso(@PathVariable Long id, @PathVariable Long cursoId) {
        estudianteService.inscribirEnCurso(id, cursoId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/cursos/{cursoId}")
    public ResponseEntity<?> desinscribirDeCurso(@PathVariable Long id, @PathVariable Long cursoId) {
        estudianteService.desinscribirDeCurso(id, cursoId);
        return ResponseEntity.noContent().build();
    }
}