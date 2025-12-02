package com.attendance.service;

import com.attendance.dto.*;
import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final ClassRoomRepository classRoomRepository;

    public AdminService(PasswordEncoder passwordEncoder,
                        UserRepository userRepository,
                        TeacherRepository teacherRepository,
                        ParentRepository parentRepository,
                        StudentRepository studentRepository,
                        ClassRoomRepository classRoomRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.classRoomRepository = classRoomRepository;
    }

    @Transactional
    public Teacher registerTeacher(TeacherRegistrationRequest request) {
        User user = createUser(request.getUsername(), request.getPassword(), User.Role.TEACHER);
        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setFullName(request.getFullName());
        teacher.setEmail(request.getEmail());
        teacher.setSubject(request.getSubject());
        teacher.setClassAssigned(request.getClassAssigned());
        return teacherRepository.save(teacher);
    }

    @Transactional
    public Parent registerParent(ParentRegistrationRequest request) {
        User user = createUser(request.getUsername(), request.getPassword(), User.Role.PARENT);
        Parent parent = new Parent();
        parent.setUser(user);
        parent.setName(request.getFullName());
        parent.setPhone(request.getPhone());
        parent.setEmail(request.getEmail());
        return parentRepository.save(parent);
    }

    @Transactional
    public Student registerStudent(StudentRegistrationRequest request) {
        User user = createUser(request.getUsername(), request.getPassword(), User.Role.STUDENT);
        Student student = new Student();
        student.setUser(user);
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setGender(request.getGender());
        student.setParentContact(request.getParentContact());

        ClassRoom classRoom = classRoomRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        student.setClassRoom(classRoom);

        if (request.getParentId() != null) {
            Parent parent = parentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent not found"));
            student.setParent(parent);
        }

        return studentRepository.save(student);
    }

    @Transactional
    public ClassRoom createClassroom(ClassroomRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        ClassRoom classRoom = new ClassRoom();
        classRoom.setClassName(request.getClassName());
        classRoom.setSection(request.getSection());
        classRoom.setSchedule(request.getSchedule());
        classRoom.setTeacher(teacher);
        return classRoomRepository.save(classRoom);
    }

    public List<Teacher> listTeachers() {
        return teacherRepository.findAll();
    }

    public List<Student> listStudents() {
        return studentRepository.findAll();
    }

    public List<Parent> listParents() {
        return parentRepository.findAll();
    }

    public List<ClassRoom> listClasses() {
        return classRoomRepository.findAll();
    }

    private User createUser(String username, String rawPassword, User.Role role) {
        userRepository.findByUsername(username).ifPresent(u -> {
            throw new IllegalArgumentException("Username already in use");
        });
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        return userRepository.save(user);
    }
}

