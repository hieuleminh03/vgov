package org.viettel.vgov.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.viettel.vgov.model.*;
import org.viettel.vgov.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final WorkLogRepository workLogRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            logger.info("Initializing sample data for V-GOV system...");
            
            // Initialize in correct order to respect foreign key constraints
            List<User> users = initializeUsers();
            List<Project> projects = initializeProjects(users);
            initializeProjectMembers(users, projects);
            initializeWorkLogs(users, projects);
            initializeNotifications(users, projects);
            
            logger.info("Sample data initialization completed successfully!");
            logLoginCredentials();
        } else {
            logger.info("Database already contains data, skipping initialization");
        }
    }
    
    private List<User> initializeUsers() {
        logger.info("Creating sample users...");
        List<User> users = new ArrayList<>();
        
        // Admin user
        User admin = createUser("ADMIN001", "System Administrator", "admin@vgov.vn", "admin123", 
                                User.Role.admin, User.Gender.other, LocalDate.of(1980, 1, 1));
        users.add(userRepository.save(admin));
        
        // Project Managers
        User pm1 = createUser("PM001", "Nguyen Van Manager", "pm1@vgov.vn", "pm123", 
                              User.Role.pm, User.Gender.male, LocalDate.of(1985, 5, 15));
        pm1.setCreatedBy(admin);
        users.add(userRepository.save(pm1));
        
        User pm2 = createUser("PM002", "Tran Thi Leader", "pm2@vgov.vn", "pm123", 
                              User.Role.pm, User.Gender.female, LocalDate.of(1987, 8, 22));
        pm2.setCreatedBy(admin);
        users.add(userRepository.save(pm2));
        
        // Developers
        User dev1 = createUser("DEV001", "Le Van Developer", "dev1@vgov.vn", "dev123", 
                               User.Role.dev, User.Gender.male, LocalDate.of(1990, 3, 10));
        dev1.setCreatedBy(admin);
        users.add(userRepository.save(dev1));
        
        User dev2 = createUser("DEV002", "Pham Thi Coder", "dev2@vgov.vn", "dev123", 
                               User.Role.dev, User.Gender.female, LocalDate.of(1992, 7, 18));
        dev2.setCreatedBy(admin);
        users.add(userRepository.save(dev2));
        
        User dev3 = createUser("DEV003", "Ho Van Programmer", "dev3@vgov.vn", "dev123", 
                               User.Role.dev, User.Gender.male, LocalDate.of(1991, 11, 5));
        dev3.setCreatedBy(admin);
        users.add(userRepository.save(dev3));
        
        User dev4 = createUser("DEV004", "Vo Thi Frontend", "dev4@vgov.vn", "dev123", 
                               User.Role.dev, User.Gender.female, LocalDate.of(1993, 2, 28));
        dev4.setCreatedBy(admin);
        users.add(userRepository.save(dev4));
        
        // Business Analysts
        User ba1 = createUser("BA001", "Dang Van Analyst", "ba1@vgov.vn", "ba123", 
                              User.Role.ba, User.Gender.male, LocalDate.of(1988, 9, 12));
        ba1.setCreatedBy(admin);
        users.add(userRepository.save(ba1));
        
        User ba2 = createUser("BA002", "Bui Thi Business", "ba2@vgov.vn", "ba123", 
                              User.Role.ba, User.Gender.female, LocalDate.of(1989, 4, 20));
        ba2.setCreatedBy(admin);
        users.add(userRepository.save(ba2));
        
        // Testers
        User test1 = createUser("TEST001", "Ngo Van Tester", "test1@vgov.vn", "test123", 
                                User.Role.test, User.Gender.male, LocalDate.of(1990, 6, 14));
        test1.setCreatedBy(admin);
        users.add(userRepository.save(test1));
        
        User test2 = createUser("TEST002", "Duong Thi QA", "test2@vgov.vn", "test123", 
                                User.Role.test, User.Gender.female, LocalDate.of(1991, 12, 3));
        test2.setCreatedBy(admin);
        users.add(userRepository.save(test2));
        
        logger.info("Created {} sample users", users.size());
        return users;
    }
    
    private User createUser(String employeeCode, String fullName, String email, String password,
                           User.Role role, User.Gender gender, LocalDate birthDate) {
        User user = new User();
        user.setEmployeeCode(employeeCode);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);
        user.setGender(gender);
        user.setBirthDate(birthDate);
        user.setIsActive(true);
        return user;
    }
    
    private List<Project> initializeProjects(List<User> users) {
        logger.info("Creating sample projects...");
        List<Project> projects = new ArrayList<>();
        
        User admin = users.get(0);
        User pm1 = users.get(1); // pm1@vgov.vn
        User pm2 = users.get(2); // pm2@vgov.vn
        
        // Active projects
        Project project1 = createProject("PROJ001", "E-Government Portal", pm1.getEmail(),
                                         LocalDate.of(2024, 1, 15), LocalDate.of(2024, 12, 31),
                                         Project.ProjectType.TM, Project.Status.InProgress,
                                         "Development of comprehensive e-government portal for citizen services", admin);
        projects.add(projectRepository.save(project1));
        
        Project project2 = createProject("PROJ002", "Digital Tax Management", pm2.getEmail(),
                                         LocalDate.of(2024, 3, 1), LocalDate.of(2024, 11, 30),
                                         Project.ProjectType.Package, Project.Status.InProgress,
                                         "Modernization of tax management system with digital workflows", admin);
        projects.add(projectRepository.save(project2));
        
        Project project3 = createProject("PROJ003", "Smart City Dashboard", pm1.getEmail(),
                                         LocalDate.of(2024, 6, 1), LocalDate.of(2025, 5, 31),
                                         Project.ProjectType.OSDC, Project.Status.InProgress,
                                         "Real-time city monitoring and analytics dashboard", admin);
        projects.add(projectRepository.save(project3));
        
        Project project4 = createProject("PROJ004", "Healthcare Information System", pm2.getEmail(),
                                         LocalDate.of(2024, 2, 15), LocalDate.of(2024, 8, 15),
                                         Project.ProjectType.TM, Project.Status.Hold,
                                         "Integrated healthcare management system for public hospitals", admin);
        projects.add(projectRepository.save(project4));
        
        Project project5 = createProject("PROJ005", "AI-Powered Document Processing", pm1.getEmail(),
                                         LocalDate.of(2024, 9, 1), null,
                                         Project.ProjectType.Presale, Project.Status.Presale,
                                         "Automated document processing using AI/ML technologies", admin);
        projects.add(projectRepository.save(project5));
        
        // Completed project
        Project project6 = createProject("PROJ006", "Legacy System Migration", pm2.getEmail(),
                                         LocalDate.of(2023, 6, 1), LocalDate.of(2024, 1, 31),
                                         Project.ProjectType.Package, Project.Status.Closed,
                                         "Migration of legacy systems to modern cloud infrastructure", admin);
        projects.add(projectRepository.save(project6));
        
        logger.info("Created {} sample projects", projects.size());
        return projects;
    }
    
    private Project createProject(String projectCode, String projectName, String pmEmail,
                                 LocalDate startDate, LocalDate endDate, Project.ProjectType projectType,
                                 Project.Status status, String description, User createdBy) {
        Project project = new Project();
        project.setProjectCode(projectCode);
        project.setProjectName(projectName);
        project.setPmEmail(pmEmail);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setProjectType(projectType);
        project.setStatus(status);
        project.setDescription(description);
        project.setCreatedBy(createdBy);
        return project;
    }
    
    private void initializeProjectMembers(List<User> users, List<Project> projects) {
        logger.info("Creating project member assignments...");
        
        User admin = users.get(0);
        
        // Project 1: E-Government Portal (Full team)
        createProjectMember(projects.get(0), users.get(3), new BigDecimal("80.00"), admin); // dev1 - 80%
        createProjectMember(projects.get(0), users.get(4), new BigDecimal("60.00"), admin); // dev2 - 60%
        createProjectMember(projects.get(0), users.get(7), new BigDecimal("50.00"), admin); // ba1 - 50%
        createProjectMember(projects.get(0), users.get(9), new BigDecimal("40.00"), admin); // test1 - 40%
        
        // Project 2: Digital Tax Management
        createProjectMember(projects.get(1), users.get(5), new BigDecimal("70.00"), admin); // dev3 - 70%
        createProjectMember(projects.get(1), users.get(6), new BigDecimal("50.00"), admin); // dev4 - 50%
        createProjectMember(projects.get(1), users.get(8), new BigDecimal("60.00"), admin); // ba2 - 60%
        createProjectMember(projects.get(1), users.get(10), new BigDecimal("45.00"), admin); // test2 - 45%
        
        // Project 3: Smart City Dashboard (Smaller team)
        createProjectMember(projects.get(2), users.get(3), new BigDecimal("20.00"), admin); // dev1 - 20% (remaining capacity)
        createProjectMember(projects.get(2), users.get(4), new BigDecimal("40.00"), admin); // dev2 - 40% (remaining capacity)
        createProjectMember(projects.get(2), users.get(7), new BigDecimal("50.00"), admin); // ba1 - 50% (remaining capacity)
        
        // Project 4: Healthcare (On hold, but still assigned)
        createProjectMember(projects.get(3), users.get(5), new BigDecimal("30.00"), admin); // dev3 - 30% (remaining capacity)
        createProjectMember(projects.get(3), users.get(8), new BigDecimal("40.00"), admin); // ba2 - 40% (remaining capacity)
        createProjectMember(projects.get(3), users.get(10), new BigDecimal("55.00"), admin); // test2 - 55% (remaining capacity)
        
        // Project 5: AI Document Processing (Presale - minimal assignment)
        createProjectMember(projects.get(4), users.get(6), new BigDecimal("50.00"), admin); // dev4 - 50% (remaining capacity)
        createProjectMember(projects.get(4), users.get(9), new BigDecimal("60.00"), admin); // test1 - 60% (remaining capacity)
        
        // Project 6: Legacy Migration (Closed - historical data)
        ProjectMember closedMember1 = createProjectMember(projects.get(5), users.get(3), new BigDecimal("100.00"), admin);
        closedMember1.setIsActive(false);
        closedMember1.setLeftDate(LocalDate.of(2024, 1, 31));
        projectMemberRepository.save(closedMember1);
        
        ProjectMember closedMember2 = createProjectMember(projects.get(5), users.get(7), new BigDecimal("80.00"), admin);
        closedMember2.setIsActive(false);
        closedMember2.setLeftDate(LocalDate.of(2024, 1, 31));
        projectMemberRepository.save(closedMember2);
        
        logger.info("Created project member assignments");
    }
    
    private ProjectMember createProjectMember(Project project, User user, BigDecimal workload, User createdBy) {
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        member.setWorkloadPercentage(workload);
        member.setJoinedDate(project.getStartDate());
        member.setIsActive(true);
        member.setCreatedBy(createdBy);
        return projectMemberRepository.save(member);
    }
    
    private void initializeWorkLogs(List<User> users, List<Project> projects) {
        logger.info("Creating sample work logs...");
        
        // Generate work logs for the past 2 weeks for active project members
        LocalDate today = LocalDate.now();
        LocalDate twoWeeksAgo = today.minusWeeks(2);
        
        // Work logs for Project 1 (E-Government Portal)
        createWorkLogsForPeriod(users.get(3), projects.get(0), twoWeeksAgo, today, "Frontend Development");
        createWorkLogsForPeriod(users.get(4), projects.get(0), twoWeeksAgo, today, "Backend API Development");
        createWorkLogsForPeriod(users.get(7), projects.get(0), twoWeeksAgo, today, "Requirements Analysis");
        createWorkLogsForPeriod(users.get(9), projects.get(0), twoWeeksAgo, today, "Testing & QA");
        
        // Work logs for Project 2 (Digital Tax Management)
        createWorkLogsForPeriod(users.get(5), projects.get(1), twoWeeksAgo, today, "Core System Development");
        createWorkLogsForPeriod(users.get(6), projects.get(1), twoWeeksAgo, today, "UI/UX Implementation");
        createWorkLogsForPeriod(users.get(8), projects.get(1), twoWeeksAgo, today, "Business Process Design");
        createWorkLogsForPeriod(users.get(10), projects.get(1), twoWeeksAgo, today, "System Testing");
        
        // Work logs for Project 3 (Smart City Dashboard)
        createWorkLogsForPeriod(users.get(3), projects.get(2), twoWeeksAgo, today, "Dashboard Components");
        createWorkLogsForPeriod(users.get(4), projects.get(2), twoWeeksAgo, today, "Data Integration");
        createWorkLogsForPeriod(users.get(7), projects.get(2), twoWeeksAgo, today, "Analytics Requirements");
        
        logger.info("Created sample work logs for the past 2 weeks");
    }
    
    private void createWorkLogsForPeriod(User user, Project project, LocalDate startDate, LocalDate endDate, String taskFeature) {
        LocalDate current = startDate;
        String[] workDescriptions = {
            "Implemented new features and resolved technical issues",
            "Code review and documentation updates",
            "Bug fixes and performance optimization",
            "Feature development and unit testing",
            "Integration testing and deployment preparation",
            "Research and technical analysis",
            "Meeting with stakeholders and requirement gathering",
            "Code refactoring and architecture improvements"
        };
        
        while (current.isBefore(endDate)) {
            // Skip weekends
            if (current.getDayOfWeek().getValue() <= 5) {
                WorkLog workLog = new WorkLog();
                workLog.setUser(user);
                workLog.setProject(project);
                workLog.setWorkDate(current);
                workLog.setHoursWorked(new BigDecimal("8.00"));
                workLog.setTaskFeature(taskFeature);
                workLog.setWorkDescription(workDescriptions[current.getDayOfMonth() % workDescriptions.length]);
                workLogRepository.save(workLog);
            }
            current = current.plusDays(1);
        }
    }
    
    private void initializeNotifications(List<User> users, List<Project> projects) {
        logger.info("Creating sample notifications...");
        
        // Welcome notifications for all non-admin users
        for (int i = 1; i < users.size(); i++) {
            User user = users.get(i);
            Notification welcome = new Notification();
            welcome.setUser(user);
            welcome.setTitle("Welcome to V-GOV System");
            welcome.setMessage("Welcome to the V-GOV project management system. You can now track your work, view project details, and manage your profile.");
            welcome.setNotificationType("WELCOME");
            welcome.setIsRead(false);
            notificationRepository.save(welcome);
        }
        
        // Project assignment notifications
        createNotification(users.get(3), "Project Assignment", 
                          "You have been assigned to project: E-Government Portal", 
                          "PROJECT_ASSIGNMENT", projects.get(0), null);
        
        createNotification(users.get(4), "Project Assignment", 
                          "You have been assigned to project: E-Government Portal", 
                          "PROJECT_ASSIGNMENT", projects.get(0), null);
        
        createNotification(users.get(5), "Project Assignment", 
                          "You have been assigned to project: Digital Tax Management", 
                          "PROJECT_ASSIGNMENT", projects.get(1), null);
        
        // Project status change notifications
        for (User user : List.of(users.get(5), users.get(8), users.get(10))) {
            createNotification(user, "Project Status Update", 
                              "Project 'Healthcare Information System' status has been changed to 'Hold'", 
                              "PROJECT_STATUS_CHANGE", projects.get(3), null);
        }
        
        // Workload reminder notifications
        createNotification(users.get(3), "Workload Notice", 
                          "Your current workload is 100%. Please ensure work logs are up to date.", 
                          "WORKLOAD_REMINDER", null, null);
        
        createNotification(users.get(10), "Workload Notice", 
                          "Your current workload is 100%. Please ensure work logs are up to date.", 
                          "WORKLOAD_REMINDER", null, null);
        
        logger.info("Created sample notifications");
    }
    
    private void createNotification(User user, String title, String message, String type, Project project, User relatedUser) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setNotificationType(type);
        notification.setRelatedProject(project);
        notification.setRelatedUser(relatedUser);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }
    
    private void logLoginCredentials() {
        logger.info("\n" + "=".repeat(80));
        logger.info("SAMPLE LOGIN CREDENTIALS FOR V-GOV SYSTEM");
        logger.info("=".repeat(80));
        logger.info("Admin User:");
        logger.info("  Email: admin@vgov.vn");
        logger.info("  Password: admin123");
        logger.info("");
        logger.info("Project Managers:");
        logger.info("  Email: pm1@vgov.vn | Password: pm123");
        logger.info("  Email: pm2@vgov.vn | Password: pm123");
        logger.info("");
        logger.info("Developers:");
        logger.info("  Email: dev1@vgov.vn | Password: dev123");
        logger.info("  Email: dev2@vgov.vn | Password: dev123");
        logger.info("  Email: dev3@vgov.vn | Password: dev123");
        logger.info("  Email: dev4@vgov.vn | Password: dev123");
        logger.info("");
        logger.info("Business Analysts:");
        logger.info("  Email: ba1@vgov.vn | Password: ba123");
        logger.info("  Email: ba2@vgov.vn | Password: ba123");
        logger.info("");
        logger.info("Testers:");
        logger.info("  Email: test1@vgov.vn | Password: test123");
        logger.info("  Email: test2@vgov.vn | Password: test123");
        logger.info("=".repeat(80));
    }
}
