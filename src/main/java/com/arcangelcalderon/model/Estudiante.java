package com.arcangelcalderon.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaNacimiento;

    @ManyToMany(mappedBy = "estudiantesInscritos")
    private List<Curso> cursosInscritos;

    public Estudiante() {
    }

    public Estudiante(Long id, String nombre, String apellido, String email, LocalDate fechaNacimiento, List<Curso> cursosInscritos) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.cursosInscritos = cursosInscritos;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public List<Curso> getCursosInscritos() { return cursosInscritos; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEmail(String email) { this.email = email; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setCursosInscritos(List<Curso> cursosInscritos) { this.cursosInscritos = cursosInscritos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudiante that = (Estudiante) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}