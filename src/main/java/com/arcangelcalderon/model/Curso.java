package com.arcangelcalderon.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer creditos;

    @ManyToMany
    @JoinTable(
            name = "curso_estudiante",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    private List<Estudiante> estudiantesInscritos;

    public Curso() { }
    public Curso(Long id, String nombre, String descripcion, Integer creditos, List<Estudiante> estudiantesInscritos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.estudiantesInscritos = estudiantesInscritos;
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Integer getCreditos() { return creditos; }
    public List<Estudiante> getEstudiantesInscritos() { return estudiantesInscritos; }
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCreditos(Integer creditos) { this.creditos = creditos; }
    public void setEstudiantesInscritos(List<Estudiante> estudiantesInscritos) { this.estudiantesInscritos = estudiantesInscritos; }
    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Curso curso = (Curso) o; return Objects.equals(id, curso.id); }
    @Override
    public int hashCode() { return Objects.hash(id); }
}