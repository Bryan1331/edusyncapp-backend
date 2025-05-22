package com.arcangelcalderon.repository;

import com.arcangelcalderon.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}