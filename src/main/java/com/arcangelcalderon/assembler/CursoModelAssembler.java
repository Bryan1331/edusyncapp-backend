package com.arcangelcalderon.assembler;

import com.arcangelcalderon.controller.CursoController;
import com.arcangelcalderon.model.Curso;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CursoModelAssembler implements RepresentationModelAssembler<Curso, EntityModel<Curso>> {

    @Override
    public EntityModel<Curso> toModel(Curso curso) {
        return EntityModel.of(curso,
                linkTo(methodOn(CursoController.class).getCursoById(curso.getId())).withSelfRel(),
                linkTo(methodOn(CursoController.class).updateCurso(curso.getId(), null)).withRel("editar"),
                linkTo(methodOn(CursoController.class).deleteCurso(curso.getId())).withRel("eliminar"),
                linkTo(methodOn(CursoController.class).getEstudiantesInscritos(curso.getId())).withRel("estudiantes"));
    }
}