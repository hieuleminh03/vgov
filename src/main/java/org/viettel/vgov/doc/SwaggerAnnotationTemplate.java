package org.viettel.vgov.doc;

/**
 * Swagger Annotation Template and Guidelines
 * 
 * This file serves as a reference for developers to add consistent Swagger documentation
 * to controllers. Follow these patterns for consistency across the API documentation.
 * 
 * CONTROLLER-LEVEL ANNOTATIONS:
 * 
 * @Tag(name = "Controller Group Name", description = "Brief description of controller functionality")
 * @SecurityRequirement(name = "bearerAuth") // For controllers requiring authentication
 * 
 * METHOD-LEVEL ANNOTATIONS:
 * 
 * @Operation(summary = "Brief action description", description = "Detailed description of what this endpoint does")
 * 
 * NOTE: @ApiResponses and @Parameter annotations are NOT used in this project for simplicity.
 * Only @Tag, @SecurityRequirement, and @Operation annotations are used.
 * 
 * TAG GROUPS DEFINED IN SwaggerConfig:
 * 
 * 1. "Authentication" - Login, logout, token refresh, current user
 * 2. "User Management" - User CRUD (Admin only)
 * 3. "Profile Management" - User profile and personal settings
 * 4. "Project Management" - Project CRUD and status management
 * 5. "Project Members" - Project member assignment and workload management
 * 6. "Work Logs" - Work log tracking and reporting
 * 7. "Dashboard & Analytics" - Dashboard overview and analytics data
 * 8. "Notifications" - In-app notification management
 * 9. "File Management" - File upload, download and management via MinIO
 * 10. "System & Lookup" - System information and lookup data endpoints
 * 
 * ROLE-BASED ACCESS PATTERNS:
 * 
 * - Admin only: Use @PreAuthorize("hasRole('ADMIN')")
 * - All authenticated: Just @SecurityRequirement(name = "bearerAuth")
 * - Role-based access: @PreAuthorize("hasRole('ADMIN') or hasRole('PM') or ...")
 * - Custom security: @PreAuthorize("@customSecurityService.canAccess(...)")
 * 
 * CONSISTENCY GUIDELINES:
 * 
 * 1. Use consistent response codes across similar operations
 * 2. Include role requirements in operation descriptions
 * 3. Describe business rules and constraints in descriptions
 * 4. Use clear, professional language in summaries and descriptions
 * 5. Group related endpoints using the predefined tags
 */
public class SwaggerAnnotationTemplate {
    // This is a documentation-only class
}
