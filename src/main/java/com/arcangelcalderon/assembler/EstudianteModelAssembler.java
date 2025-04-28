package com.arcangelcalderon.assembler;

import com.arcangelcalderon.controller.EstudianteController;
import com.arcangelcalderon.model.Estudiante;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EstudianteModelAssembler implements RepresentationModelAssembler<Estudiante, EntityModel<Estudiante>> {

    @Override
    public EntityModel<Estudiante> toModel(Estudiante estudiante) {
        return EntityModel.of(estudiante,
                linkTo(methodOn(EstudianteController.class).getEstudianteById(estudiante.getId())).withSelfRel(),
                linkTo(methodOn(EstudianteController.class).updateEstudiante(estudiante.getId(), null)).withRel("editar"),
                linkTo(methodOn(EstudianteController.class).deleteEstudiante(estudiante.getId())).withRel("eliminar"),
                linkTo(methodOn(EstudianteController.class).getCursosInscritos(estudiante.getId())).withRel("cursos"));
    }
}