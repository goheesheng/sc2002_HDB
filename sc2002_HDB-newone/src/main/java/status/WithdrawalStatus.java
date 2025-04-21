package status;

/**
 * Represents the possible statuses of a withdrwal.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public enum WithdrawalStatus {
    /**
     * No withdrawal request has been made
     */
    NIL,
    /**
     * The withdrawal is pending review.
     */
    PENDING,
    /**
     * The withdrawal has been approved 
     */
    APPROVED,
    /**
     * The withdrawal has been rejected
     */
    REJECTED
}
