package org.viettel.vgov.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProjectMemberRequestDto {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Workload percentage is required")
    @DecimalMin(value = "0.01", message = "Workload percentage must be greater than 0")
    @DecimalMax(value = "100.00", message = "Workload percentage cannot exceed 100%")
    private BigDecimal workloadPercentage;
    
    private LocalDate joinedDate;
    
    private LocalDate leftDate;
    
    private Boolean isActive = true;
}
