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
        User admin = createUser("ADMIN001", "Quản Trị Viên Hệ Thống", "admin@vgov.vn", "123456", 
                                User.Role.admin, User.Gender.other, LocalDate.of(1980, 1, 1));
        users.add(userRepository.save(admin));
        
        // Project Managers
        User pm1 = createUser("PM001", "Nguyễn Văn Quản", "pm1@vgov.vn", "123456", 
                              User.Role.pm, User.Gender.male, LocalDate.of(1985, 5, 15));
        pm1.setCreatedBy(admin);
        users.add(userRepository.save(pm1));
        
        User pm2 = createUser("PM002", "Trần Thị Linh", "pm2@vgov.vn", "123456", 
                              User.Role.pm, User.Gender.female, LocalDate.of(1987, 8, 22));
        pm2.setCreatedBy(admin);
        users.add(userRepository.save(pm2));
        
        // Developers
        User dev1 = createUser("DEV001", "Lê Văn Đức", "dev1@vgov.vn", "123456", 
                               User.Role.dev, User.Gender.male, LocalDate.of(1990, 3, 10));
        dev1.setCreatedBy(admin);
        users.add(userRepository.save(dev1));
        
        User dev2 = createUser("DEV002", "Phạm Thị Mai", "dev2@vgov.vn", "123456", 
                               User.Role.dev, User.Gender.female, LocalDate.of(1992, 7, 18));
        dev2.setCreatedBy(admin);
        users.add(userRepository.save(dev2));
        
        User dev3 = createUser("DEV003", "Hồ Văn Tuấn", "dev3@vgov.vn", "123456", 
                               User.Role.dev, User.Gender.male, LocalDate.of(1991, 11, 5));
        dev3.setCreatedBy(admin);
        users.add(userRepository.save(dev3));
        
        User dev4 = createUser("DEV004", "Võ Thị Hương", "dev4@vgov.vn", "123456", 
                               User.Role.dev, User.Gender.female, LocalDate.of(1993, 2, 28));
        dev4.setCreatedBy(admin);
        users.add(userRepository.save(dev4));
        
        // Business Analysts
        User ba1 = createUser("BA001", "Đặng Văn Minh", "ba1@vgov.vn", "123456", 
                              User.Role.ba, User.Gender.male, LocalDate.of(1988, 9, 12));
        ba1.setCreatedBy(admin);
        users.add(userRepository.save(ba1));
        
        User ba2 = createUser("BA002", "Bùi Thị Lan", "ba2@vgov.vn", "123456", 
                              User.Role.ba, User.Gender.female, LocalDate.of(1989, 4, 20));
        ba2.setCreatedBy(admin);
        users.add(userRepository.save(ba2));
        
        // Testers
        User test1 = createUser("TEST001", "Ngô Văn Thắng", "test1@vgov.vn", "123456", 
                                User.Role.test, User.Gender.male, LocalDate.of(1990, 6, 14));
        test1.setCreatedBy(admin);
        users.add(userRepository.save(test1));
        
        User test2 = createUser("TEST002", "Dương Thị Nga", "test2@vgov.vn", "123456", 
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
        Project project1 = createProject("PROJ001", "Hệ Thống Quản Lý Bán Hàng Online", pm1.getEmail(),
                                         LocalDate.of(2024, 1, 15), LocalDate.of(2024, 12, 31),
                                         Project.ProjectType.TM, Project.Status.InProgress,
                                         "Phát triển nền tảng thương mại điện tử tích hợp đầy đủ với hệ thống thanh toán, quản lý kho, CRM và báo cáo thống kê cho khách hàng Singapore. Sử dụng React, Node.js, PostgreSQL và AWS.", admin);
        projects.add(projectRepository.save(project1));
        
        Project project2 = createProject("PROJ002", "Ứng Dụng Mobile Banking", pm2.getEmail(),
                                         LocalDate.of(2024, 3, 1), LocalDate.of(2024, 11, 30),
                                         Project.ProjectType.Package, Project.Status.InProgress,
                                         "Xây dựng ứng dụng ngân hàng di động cho ngân hàng Úc với các tính năng chuyển tiền, thanh toán hóa đơn, quản lý tài khoản và tích hợp AI chatbot. Công nghệ: React Native, Java Spring Boot, Oracle Database.", admin);
        projects.add(projectRepository.save(project2));
        
        Project project3 = createProject("PROJ003", "Hệ Thống ERP Doanh Nghiệp", pm1.getEmail(),
                                         LocalDate.of(2024, 6, 1), LocalDate.of(2025, 5, 31),
                                         Project.ProjectType.OSDC, Project.Status.InProgress,
                                         "Phát triển hệ thống hoạch định tài nguyên doanh nghiệp (ERP) cho tập đoàn sản xuất tại Nhật Bản, bao gồm module quản lý nhân sự, tài chính, sản xuất và chuỗi cung ứng. Sử dụng Angular, .NET Core, SQL Server.", admin);
        projects.add(projectRepository.save(project3));
        
        Project project4 = createProject("PROJ004", "Nền Tảng Học Trực Tuyến", pm2.getEmail(),
                                         LocalDate.of(2024, 2, 15), LocalDate.of(2024, 8, 15),
                                         Project.ProjectType.TM, Project.Status.Hold,
                                         "Xây dựng nền tảng giáo dục trực tuyến với tính năng video conference, quản lý khóa học, bài kiểm tra và theo dõi tiến độ học tập cho startup Mỹ. Công nghệ: Vue.js, Python Django, MongoDB, WebRTC.", admin);
        projects.add(projectRepository.save(project4));
        
        Project project5 = createProject("PROJ005", "Hệ Thống IoT Thông Minh", pm1.getEmail(),
                                         LocalDate.of(2024, 9, 1), null,
                                         Project.ProjectType.Presale, Project.Status.Presale,
                                         "Phát triển hệ thống quản lý thiết bị IoT cho nhà máy thông minh tại Đức, tích hợp AI/ML để dự đoán bảo trì và tối ưu hóa quy trình sản xuất. Sử dụng React, Node.js, InfluxDB, Docker, Kubernetes.", admin);
        projects.add(projectRepository.save(project5));
        
        // Completed project
        Project project6 = createProject("PROJ006", "Hệ Thống Quản Lý Logistics", pm2.getEmail(),
                                         LocalDate.of(2023, 6, 1), LocalDate.of(2024, 1, 31),
                                         Project.ProjectType.Package, Project.Status.Closed,
                                         "Hoàn thành hệ thống quản lý vận chuyển và kho bãi cho công ty logistics Hàn Quốc với tính năng theo dõi GPS, tối ưu hóa tuyến đường và quản lý đơn hàng. Công nghệ: React, Java Spring, MySQL, Redis.", admin);
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
        User pm1 = users.get(1); // PM for projects 1, 3, 5
        User pm2 = users.get(2); // PM for projects 2, 4, 6
        
        // Project 1: E-Government Portal (Full team) - PM1 also participates
        createProjectMember(projects.get(0), pm1, new BigDecimal("20.00"), admin); // pm1 - 20%
        createProjectMember(projects.get(0), users.get(3), new BigDecimal("80.00"), admin); // dev1 - 80%
        createProjectMember(projects.get(0), users.get(4), new BigDecimal("60.00"), admin); // dev2 - 60%
        createProjectMember(projects.get(0), users.get(7), new BigDecimal("50.00"), admin); // ba1 - 50%
        createProjectMember(projects.get(0), users.get(9), new BigDecimal("40.00"), admin); // test1 - 40%
        
        // Project 2: Digital Tax Management - PM2 also participates
        createProjectMember(projects.get(1), pm2, new BigDecimal("25.00"), admin); // pm2 - 25%
        createProjectMember(projects.get(1), users.get(5), new BigDecimal("70.00"), admin); // dev3 - 70%
        createProjectMember(projects.get(1), users.get(6), new BigDecimal("50.00"), admin); // dev4 - 50%
        createProjectMember(projects.get(1), users.get(8), new BigDecimal("60.00"), admin); // ba2 - 60%
        createProjectMember(projects.get(1), users.get(10), new BigDecimal("45.00"), admin); // test2 - 45%
        
        // Project 3: Smart City Dashboard (Smaller team) - PM1 also participates
        createProjectMember(projects.get(2), pm1, new BigDecimal("30.00"), admin); // pm1 - 30% (remaining capacity)
        createProjectMember(projects.get(2), users.get(3), new BigDecimal("20.00"), admin); // dev1 - 20% (remaining capacity)
        createProjectMember(projects.get(2), users.get(4), new BigDecimal("40.00"), admin); // dev2 - 40% (remaining capacity)
        createProjectMember(projects.get(2), users.get(7), new BigDecimal("50.00"), admin); // ba1 - 50% (remaining capacity)
        
        // Project 4: Healthcare (On hold, but still assigned) - PM2 also participates
        createProjectMember(projects.get(3), pm2, new BigDecimal("35.00"), admin); // pm2 - 35% (remaining capacity)
        createProjectMember(projects.get(3), users.get(5), new BigDecimal("30.00"), admin); // dev3 - 30% (remaining capacity)
        createProjectMember(projects.get(3), users.get(8), new BigDecimal("40.00"), admin); // ba2 - 40% (remaining capacity)
        createProjectMember(projects.get(3), users.get(10), new BigDecimal("55.00"), admin); // test2 - 55% (remaining capacity)
        
        // Project 5: AI Document Processing (Presale - minimal assignment) - PM1 also participates
        createProjectMember(projects.get(4), pm1, new BigDecimal("50.00"), admin); // pm1 - 50% (remaining capacity)
        createProjectMember(projects.get(4), users.get(6), new BigDecimal("50.00"), admin); // dev4 - 50% (remaining capacity)
        createProjectMember(projects.get(4), users.get(9), new BigDecimal("60.00"), admin); // test1 - 60% (remaining capacity)
        
        // Project 6: Legacy Migration (Closed - historical data) - PM2 also participated
        ProjectMember closedPm2 = createProjectMember(projects.get(5), pm2, new BigDecimal("40.00"), admin);
        closedPm2.setIsActive(false);
        closedPm2.setLeftDate(LocalDate.of(2024, 1, 31));
        projectMemberRepository.save(closedPm2);
        
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
        // Admin (users.get(0)) is excluded from work logs as they don't participate in projects
        LocalDate today = LocalDate.now();
        LocalDate twoWeeksAgo = today.minusWeeks(2);
        
        User pm1 = users.get(1); // PM1
        User pm2 = users.get(2); // PM2
        
        // Work logs for Project 1 (Hệ Thống Quản Lý Bán Hàng Online) - including PM1
        createWorkLogsForPeriod(pm1, projects.get(0), twoWeeksAgo, today, "Quản Lý Dự Án & Lập Kế Hoạch");
        createWorkLogsForPeriod(users.get(3), projects.get(0), twoWeeksAgo, today, "Phát Triển Giao Diện React");
        createWorkLogsForPeriod(users.get(4), projects.get(0), twoWeeksAgo, today, "Phát Triển API Backend");
        createWorkLogsForPeriod(users.get(7), projects.get(0), twoWeeksAgo, today, "Phân Tích Yêu Cầu & Thiết Kế");
        createWorkLogsForPeriod(users.get(9), projects.get(0), twoWeeksAgo, today, "Kiểm Thử & Đảm Bảo Chất Lượng");
        
        // Work logs for Project 2 (Ứng Dụng Mobile Banking) - including PM2
        createWorkLogsForPeriod(pm2, projects.get(1), twoWeeksAgo, today, "Quản Lý Dự Án & Điều Phối");
        createWorkLogsForPeriod(users.get(5), projects.get(1), twoWeeksAgo, today, "Phát Triển Core Banking System");
        createWorkLogsForPeriod(users.get(6), projects.get(1), twoWeeksAgo, today, "Xây Dựng Ứng Dụng Mobile");
        createWorkLogsForPeriod(users.get(8), projects.get(1), twoWeeksAgo, today, "Thiết Kế Quy Trình Nghiệp Vụ");
        createWorkLogsForPeriod(users.get(10), projects.get(1), twoWeeksAgo, today, "Kiểm Thử Bảo Mật & Tích Hợp");
        
        // Work logs for Project 3 (Hệ Thống ERP Doanh Nghiệp) - including PM1
        createWorkLogsForPeriod(pm1, projects.get(2), twoWeeksAgo, today, "Giám Sát Dự Án & Chiến Lược");
        createWorkLogsForPeriod(users.get(3), projects.get(2), twoWeeksAgo, today, "Phát Triển Module ERP");
        createWorkLogsForPeriod(users.get(4), projects.get(2), twoWeeksAgo, today, "Tích Hợp Dữ Liệu & API");
        createWorkLogsForPeriod(users.get(7), projects.get(2), twoWeeksAgo, today, "Phân Tích Yêu Cầu ERP");
        
        // Work logs for Project 4 (Nền Tảng Học Trực Tuyến) - including PM2
        createWorkLogsForPeriod(pm2, projects.get(3), twoWeeksAgo, today, "Quản Lý Dự Án E-Learning");
        createWorkLogsForPeriod(users.get(5), projects.get(3), twoWeeksAgo, today, "Phát Triển Hệ Thống Giáo Dục");
        createWorkLogsForPeriod(users.get(8), projects.get(3), twoWeeksAgo, today, "Phân Tích Quy Trình Học Tập");
        createWorkLogsForPeriod(users.get(10), projects.get(3), twoWeeksAgo, today, "Kiểm Thử Video Conference");
        
        // Work logs for Project 5 (Hệ Thống IoT Thông Minh) - including PM1
        createWorkLogsForPeriod(pm1, projects.get(4), twoWeeksAgo, today, "Quản Lý Dự Án IoT & AI");
        createWorkLogsForPeriod(users.get(6), projects.get(4), twoWeeksAgo, today, "Phát Triển AI/ML & IoT");
        createWorkLogsForPeriod(users.get(9), projects.get(4), twoWeeksAgo, today, "Kiểm Thử Mô Hình AI");
        
        logger.info("Created sample work logs for the past 2 weeks");
    }
    
    private void createWorkLogsForPeriod(User user, Project project, LocalDate startDate, LocalDate endDate, String taskFeature) {
        LocalDate current = startDate;
        String[] workDescriptions = {
            "Triển khai tính năng mới và giải quyết các vấn đề kỹ thuật phức tạp",
            "Thực hiện code review và cập nhật tài liệu kỹ thuật",
            "Sửa lỗi hệ thống và tối ưu hóa hiệu năng ứng dụng",
            "Phát triển chức năng mới và thực hiện unit testing",
            "Kiểm thử tích hợp và chuẩn bị triển khai production",
            "Nghiên cứu công nghệ và phân tích giải pháp kỹ thuật",
            "Họp với khách hàng và thu thập yêu cầu nghiệp vụ",
            "Tái cấu trúc code và cải thiện kiến trúc hệ thống"
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
            welcome.setTitle("Chào Mừng Đến Với Hệ Thống V-GOV");
            welcome.setMessage("Chào mừng bạn đến với hệ thống quản lý dự án V-GOV. Bạn có thể theo dõi công việc, xem chi tiết dự án và quản lý hồ sơ cá nhân.");
            welcome.setNotificationType("WELCOME");
            welcome.setIsRead(false);
            notificationRepository.save(welcome);
        }
        
        // Project assignment notifications
        createNotification(users.get(3), "Phân Công Dự Án", 
                          "Bạn đã được phân công vào dự án: Hệ Thống Quản Lý Bán Hàng Online", 
                          "PROJECT_ASSIGNMENT", projects.get(0), null);
        
        createNotification(users.get(4), "Phân Công Dự Án", 
                          "Bạn đã được phân công vào dự án: Hệ Thống Quản Lý Bán Hàng Online", 
                          "PROJECT_ASSIGNMENT", projects.get(0), null);
        
        createNotification(users.get(5), "Phân Công Dự Án", 
                          "Bạn đã được phân công vào dự án: Ứng Dụng Mobile Banking", 
                          "PROJECT_ASSIGNMENT", projects.get(1), null);
        
        // Project status change notifications
        for (User user : List.of(users.get(5), users.get(8), users.get(10))) {
            createNotification(user, "Cập Nhật Trạng Thái Dự Án", 
                              "Dự án 'Nền Tảng Học Trực Tuyến' đã được chuyển sang trạng thái 'Tạm Dừng'", 
                              "PROJECT_STATUS_CHANGE", projects.get(3), null);
        }
        
        // Workload reminder notifications
        createNotification(users.get(3), "Thông Báo Workload", 
                          "Workload hiện tại của bạn là 100%. Vui lòng đảm bảo cập nhật work log đầy đủ.", 
                          "WORKLOAD_REMINDER", null, null);
        
        createNotification(users.get(10), "Thông Báo Workload", 
                          "Workload hiện tại của bạn là 100%. Vui lòng đảm bảo cập nhật work log đầy đủ.", 
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
        logger.info("THÔNG TIN ĐĂNG NHẬP MẪU CHO HỆ THỐNG V-GOV");
        logger.info("=".repeat(80));
        logger.info("Quản Trị Viên:");
        logger.info("  Email: admin@vgov.vn");
        logger.info("  Mật khẩu: 123456");
        logger.info("");
        logger.info("Quản Lý Dự Án:");
        logger.info("  Email: pm1@vgov.vn | Mật khẩu: 123456");
        logger.info("  Email: pm2@vgov.vn | Mật khẩu: 123456");
        logger.info("");
        logger.info("Lập Trình Viên:");
        logger.info("  Email: dev1@vgov.vn | Mật khẩu: 123456");
        logger.info("  Email: dev2@vgov.vn | Mật khẩu: 123456");
        logger.info("  Email: dev3@vgov.vn | Mật khẩu: 123456");
        logger.info("  Email: dev4@vgov.vn | Mật khẩu: 123456");
        logger.info("");
        logger.info("Phân Tích Viên:");
        logger.info("  Email: ba1@vgov.vn | Mật khẩu: 123456");
        logger.info("  Email: ba2@vgov.vn | Mật khẩu: 123456");
        logger.info("");
        logger.info("Kiểm Thử Viên:");
        logger.info("  Email: test1@vgov.vn | Mật khẩu: 123456");
        logger.info("  Email: test2@vgov.vn | Mật khẩu: 123456");
        logger.info("=".repeat(80));
    }
}
