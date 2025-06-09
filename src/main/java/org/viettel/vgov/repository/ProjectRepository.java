package org.viettel.vgov.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.viettel.vgov.model.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    Optional<Project> findByProjectCode(String projectCode);
    
    boolean existsByProjectCode(String projectCode);
    
    List<Project> findByPmEmail(String pmEmail);
    
    Page<Project> findByPmEmail(String pmEmail, Pageable pageable);
    
    List<Project> findByStatus(Project.Status status);
    
    List<Project> findByProjectType(Project.ProjectType projectType);
    
    @Query("SELECT p FROM Project p WHERE p.pmEmail = :pmEmail AND p.status = :status")
    List<Project> findByPmEmailAndStatus(@Param("pmEmail") String pmEmail, @Param("status") Project.Status status);
    
    @Query("SELECT DISTINCT p FROM Project p JOIN p.projectMembers pm WHERE pm.user.id = :userId AND pm.isActive = true")
    List<Project> findProjectsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT DISTINCT p FROM Project p JOIN p.projectMembers pm WHERE pm.user.id = :userId AND pm.isActive = true")
    Page<Project> findProjectsByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.projectName LIKE %:name%")
    List<Project> findByProjectNameContainingIgnoreCase(@Param("name") String name);
    
    @Query("SELECT p FROM Project p JOIN FETCH p.createdBy WHERE p.id = :id")
    Optional<Project> findByIdWithCreatedBy(@Param("id") Long id);
    
    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = :status")
    long countByStatus(@Param("status") Project.Status status);
    
    @Query("SELECT p FROM Project p WHERE p.pmEmail = :pmEmail ORDER BY p.createdAt DESC")
    List<Project> findProjectsManagedByPm(@Param("pmEmail") String pmEmail);
}
