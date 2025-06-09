package org.viettel.vgov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.viettel.vgov.dto.response.AnalyticsResponseDto;
import org.viettel.vgov.exception.ResourceNotFoundException;
import org.viettel.vgov.model.Project;
import org.viettel.vgov.model.User;
import org.viettel.vgov.repository.ProjectMemberRepository;
import org.viettel.vgov.repository.ProjectRepository;
import org.viettel.vgov.repository.UserRepository;
import org.viettel.vgov.repository.WorkLogRepository;
import org.viettel.vgov.security.UserPrincipal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnalyticsService {
    
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final WorkLogRepository workLogRepository;
    
    public AnalyticsResponseDto getProjectAnalytics() {
        User currentUser = getCurrentUser();
        List<Project> projects = getAccessibleProjects(currentUser);
        
        AnalyticsResponseDto analytics = new AnalyticsResponseDto();
        
        // Basic project counts
        analytics.setTotalProjects((long) projects.size());
        analytics.setActiveProjects(projects.stream()
                .filter(p -> p.getStatus() == Project.Status.InProgress)
                .count());
        analytics.setCompletedProjects(projects.stream()
                .filter(p -> p.getStatus() == Project.Status.Closed)
                .count());
        
        // Projects by status
        Map<String, Long> projectsByStatus = projects.stream()
                .collect(Collectors.groupingBy(
                    p -> p.getStatus().getDisplayName(),
                    Collectors.counting()
                ));
        analytics.setProjectsByStatus(projectsByStatus);
        
        // Projects by type
        Map<String, Long> projectsByType = projects.stream()
                .collect(Collectors.groupingBy(
                    p -> p.getProjectType().getDisplayName(),
                    Collectors.counting()
                ));
        analytics.setProjectsByType(projectsByType);
        
        return analytics;
    }
    
    public AnalyticsResponseDto getEmployeeAnalytics() {
        List<User> employees = userRepository.findByIsActiveTrue();
        
        AnalyticsResponseDto analytics = new AnalyticsResponseDto();
        
        // Basic employee counts
        analytics.setTotalEmployees((long) employees.size());
        analytics.setActiveEmployees((long) employees.size());
        
        // Employees by role
        Map<String, Long> employeesByRole = employees.stream()
                .collect(Collectors.groupingBy(
                    u -> u.getRole().name(),
                    Collectors.counting()
                ));
        analytics.setEmployeesByRole(employeesByRole);
        
        // Average workload
        BigDecimal totalWorkload = employees.stream()
                .filter(u -> u.getRole() != User.Role.admin)
                .map(u -> projectMemberRepository.getTotalWorkloadByUserId(u.getId()))
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long nonAdminEmployees = employees.stream()
                .filter(u -> u.getRole() != User.Role.admin)
                .count();
        
        BigDecimal averageWorkload = nonAdminEmployees > 0 
                ? totalWorkload.divide(BigDecimal.valueOf(nonAdminEmployees), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        analytics.setAverageWorkload(averageWorkload);
        
        return analytics;
    }
    
    public AnalyticsResponseDto getWorkloadAnalytics() {
        List<User> employees = userRepository.findByIsActiveTrue();
        
        AnalyticsResponseDto analytics = new AnalyticsResponseDto();
        
        // Workload by role
        Map<String, BigDecimal> workloadByRole = new HashMap<>();
        for (User.Role role : User.Role.values()) {
            if (role == User.Role.admin) continue;
            
            BigDecimal roleWorkload = employees.stream()
                    .filter(u -> u.getRole() == role)
                    .map(u -> projectMemberRepository.getTotalWorkloadByUserId(u.getId()))
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            workloadByRole.put(role.name(), roleWorkload);
        }
        analytics.setWorkloadByRole(workloadByRole);
        
        // Top workload users
        List<AnalyticsResponseDto.UserWorkloadDto> topWorkloadUsers = employees.stream()
                .filter(u -> u.getRole() != User.Role.admin)
                .map(u -> {
                    BigDecimal workload = projectMemberRepository.getTotalWorkloadByUserId(u.getId());
                    Integer projectCount = projectMemberRepository.countActiveProjectsByUserId(u.getId());
                    
                    return new AnalyticsResponseDto.UserWorkloadDto(
                            u.getId(),
                            u.getFullName(),
                            u.getEmail(),
                            workload != null ? workload : BigDecimal.ZERO,
                            projectCount != null ? projectCount : 0
                    );
                })
                .sorted((u1, u2) -> u2.getTotalWorkload().compareTo(u1.getTotalWorkload()))
                .limit(10)
                .collect(Collectors.toList());
        
        analytics.setTopWorkloadUsers(topWorkloadUsers);
        
        // System workload utilization
        BigDecimal totalCapacity = BigDecimal.valueOf(employees.stream()
                .filter(u -> u.getRole() != User.Role.admin)
                .count() * 100);
        
        BigDecimal totalUsedWorkload = employees.stream()
                .filter(u -> u.getRole() != User.Role.admin)
                .map(u -> projectMemberRepository.getTotalWorkloadByUserId(u.getId()))
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal utilization = totalCapacity.compareTo(BigDecimal.ZERO) > 0
                ? totalUsedWorkload.divide(totalCapacity, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;
        
        analytics.setSystemWorkloadUtilization(utilization);
        
        return analytics;
    }
    
    public AnalyticsResponseDto getProjectTimeline(Long projectId) {
        User currentUser = getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        
        // Check access permission
        if (!canAccessProject(currentUser, project)) {
            throw new RuntimeException("Access denied to this project");
        }
        
        AnalyticsResponseDto analytics = new AnalyticsResponseDto();
        
        // Project timeline
        List<AnalyticsResponseDto.ProjectTimelineDto> timeline = Arrays.asList(
                new AnalyticsResponseDto.ProjectTimelineDto(
                        project.getId(),
                        project.getProjectName(),
                        project.getStartDate(),
                        project.getEndDate(),
                        project.getStatus().getDisplayName(),
                        calculateProjectCompletion(projectId)
                )
        );
        analytics.setProjectMilestones(timeline);
        
        // Work log trends for the project
        List<AnalyticsResponseDto.WorkLogSummaryDto> workLogTrends = workLogRepository.findWorkLogSummaryByProject(projectId);
        analytics.setWorkLogTrends(workLogTrends);
        
        return analytics;
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    private List<Project> getAccessibleProjects(User user) {
        switch (user.getRole()) {
            case admin:
                return projectRepository.findAll();
            case pm:
                return projectRepository.findByPmEmail(user.getEmail());
            default:
                return projectRepository.findProjectsByUserId(user.getId());
        }
    }
    
    private boolean canAccessProject(User user, Project project) {
        switch (user.getRole()) {
            case admin:
                return true;
            case pm:
                return project.getPmEmail().equals(user.getEmail());
            default:
                return projectMemberRepository.existsByProjectIdAndUserIdAndIsActive(project.getId(), user.getId(), true);
        }
    }
    
    private BigDecimal calculateProjectCompletion(Long projectId) {
        // Simple completion calculation based on work logs vs estimated effort
        // This is a placeholder - real implementation would be more sophisticated
        Long totalWorkLogs = workLogRepository.countByProjectId(projectId);
        return totalWorkLogs != null ? BigDecimal.valueOf(Math.min(totalWorkLogs * 10, 100)) : BigDecimal.ZERO;
    }
}
