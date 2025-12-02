package com.attendance.config;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(UserRepository userRepo,
                           TeacherRepository teacherRepo,
                           StudentRepository studentRepo,
                           ClassRoomRepository classRepo,
                           ParentRepository parentRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepo.count() == 0) {
                // Admin user
                User admin = new User("admin", passwordEncoder.encode("admin123"), User.Role.ADMIN);
                userRepo.save(admin);

                // Parent
                User parentUser = new User("parent1", passwordEncoder.encode("parent123"), User.Role.PARENT);
                userRepo.save(parentUser);
                Parent parent = new Parent();
                parent.setUser(parentUser);
                parent.setName("Pat Parent");
                parent.setEmail("pat.parent@example.com");
                parent.setPhone("+123456789");
                parentRepository.save(parent);

                // Teacher user
                User tUser = new User("teacher1", passwordEncoder.encode("teach123"), User.Role.TEACHER);
                userRepo.save(tUser);
                Teacher teacher = new Teacher();
                teacher.setUser(tUser);
                teacher.setFullName("Alice Teacher");
                teacher.setEmail("alice.teacher@example.com");
                teacher.setSubject("Mathematics");
                teacher.setClassAssigned("Form 1");
                teacherRepo.save(teacher);

                // Class
                ClassRoom cls = new ClassRoom();
                cls.setClassName("Form 1A");
                cls.setSection("A");
                cls.setSchedule("Weekdays 8AM");
                cls.setTeacher(teacher);
                classRepo.save(cls);

                // Student user
                User sUser = new User("student1", passwordEncoder.encode("stud123"), User.Role.STUDENT);
                userRepo.save(sUser);
                Student student = new Student();
                student.setUser(sUser);
                student.setFullName("Bob Student");
                student.setEmail("bob.student@example.com");
                student.setGender("Male");
                student.setParentContact(parent.getPhone());
                student.setClassRoom(cls);
                student.setParent(parent);
                studentRepo.save(student);
            }
        };
    }
}
