package com.attendance.repository;

import com.attendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByClassRoomClassId(Long classId);

    List<Student> findByParentParentId(Long parentId);

    Optional<Student> findByUserUserId(Long userId);
    Optional<Student> findByUserUsername(String username);
}
