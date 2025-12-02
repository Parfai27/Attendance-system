package com.attendance.security;

import com.attendance.model.User;
import com.attendance.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();

        switch (user.getRole()) {
            case ADMIN -> response.sendRedirect("/dashboard/admin");
            case TEACHER -> response.sendRedirect("/dashboard/teacher");
            case STUDENT -> response.sendRedirect("/dashboard/student");
            case PARENT -> response.sendRedirect("/dashboard/parent");
            default -> response.sendRedirect("/");
        }
    }
}



