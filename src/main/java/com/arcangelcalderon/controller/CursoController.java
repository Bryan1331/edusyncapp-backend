package com.arcangelcalderon.controller;

import com.arcangelcalderon.assembler.CursoModelAssembler;
import com.arcangelcalderon.model.Estudiante;
import com.arcangelcalderon.model.Curso;
import com.arcangelcalderon.service.CursoService;
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
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final CursoModelAssembler assembler;

    public CursoController(CursoService cursoService, CursoModelAssembler assembler) {
        this.cursoService = cursoService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Curso>> getAllCursos() {
        List<EntityModel<Curso>> cursos = cursoService.getAllCursos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(cursos,
                linkTo(methodOn(CursoController.class).getAllCursos()).withSelfRel(),
                linkTo(methodOn(CursoController.class).createCurso(null)).withRel("crear"));
    }

    @GetMapping("/{id}")
    public EntityModel<Curso> getCursoById(@PathVariable Long id) {
        Curso curso = cursoService.getCursoById(id);
        return assembler.toModel(curso);
    }

    @PostMapping
    public ResponseEntity<?> createCurso(@RequestBody Curso nuevoCurso) {
        EntityModel<Curso> entityModel = assembler.toModel(cursoService.createCurso(nuevoCurso));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCurso(@PathVariable Long id, @RequestBody Curso cursoActualizado) {
        Curso curso = cursoService.updateCurso(id, cursoActualizado);
        EntityModel<Curso> entityModel = assembler.toModel(curso);

        return ResponseEntity
                .ok()
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCurso(@PathVariable Long id) {
        cursoService.deleteCurso(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/estudiantes")
    public CollectionModel<EntityModel<Estudiante>> getEstudiantesInscritos(@PathVariable Long id) {
        Curso curso = cursoService.getCursoById(id);
        List<EntityModel<Estudiante>> estudiantes = curso.getEstudiantesInscritos().stream()
                .map(estudiante -> EntityModel.of(estudiante,
                        linkTo(methodOn(EstudianteController.class).getEstudianteById(estudiante.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(estudiantes,
                linkTo(methodOn(CursoController.class).getEstudiantesInscritos(id)).withSelfRel(),
                linkTo(methodOn(CursoController.class).inscribirEstudiante(id, null)).withRel("inscribir")); // El estudianteId se pasará en el POST
    }

    @PostMapping("/{id}/estudiantes/{estudianteId}")
    public ResponseEntity<?> inscribirEstudiante(@PathVariable Long id, @PathVariable Long estudianteId) {
        cursoService.inscribirEstudiante(id, estudianteId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/estudiantes/{estudianteId}")
    public ResponseEntity<?> desinscribirEstudiante(@PathVariable Long id, @PathVariable Long estudianteId) {
        cursoService.desinscribirEstudiante(id, estudianteId);
        return ResponseEntity.noContent().build();
    }
}