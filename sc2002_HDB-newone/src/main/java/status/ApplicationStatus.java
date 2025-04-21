package status;

/**
 * Represents the possible statuses of a BTO application.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public enum ApplicationStatus {
    /**
     * The application is pending review.
     */
    PENDING,
    /**
     * The application has been successful and the applicant is eligible for booking.
     */
    SUCCESSFUL,
    /**
     * The application has been unsuccessful and the applicant is not eligible for booking.
     */
    UNSUCCESSFUL,
    /**
     * The applicant has booked a flat after a successful application.
     */
    BOOKED
}
