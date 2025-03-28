package status;

/**
 * Represents the possible statuses of an HDB Officer's registration for a project.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public enum RegistrationStatus {
    /**
     * The registration is pending approval.
     */
    PENDING,
    /**
     * The registration has been approved.
     */
    APPROVED,
    /**
     * The registration has been rejected.
     */
    REJECTED
}
