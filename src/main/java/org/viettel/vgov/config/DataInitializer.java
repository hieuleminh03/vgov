package org.viettel.vgov.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.viettel.vgov.model.User;
import org.viettel.vgov.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }
    
    private void initializeAdminUser() {
        String adminEmail = "admin@vgov.vn";
        
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmployeeCode("ADMIN001");
            admin.setFullName("System Administrator");
            admin.setEmail(adminEmail);
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.admin);
            admin.setGender(User.Gender.other);
            admin.setIsActive(true);
            
            userRepository.save(admin);
            logger.info("Default admin user created with email: {} and password: admin123", adminEmail);
        } else {
            logger.info("Admin user already exists");
        }
    }
}
