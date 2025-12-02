package com.attendance.repository;

import com.attendance.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserUserId(Long userId);
    Optional<Teacher> findByUserUsername(String username);
}
