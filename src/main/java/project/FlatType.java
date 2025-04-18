package project;
/**
 * Represents the types of flats available in BTO projects.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public enum FlatType {
    /**
     * A two-room flat.
     */
    TWO_ROOM,
    /**
     * A three-room flat.
     */
    THREE_ROOM,
    /**
     * Unknown flat type (used for invalid or unsupported values).
     */
    UNKNOWN;


    /**
    * Converts a string representation of a flat type to the corresponding enum value.
    * Handles variations and invalid input by returning UNKNOWN.
    * 
    * @param flatTypeString The string representation of a flat type (e.g., "2_ROOM", "TWO ROOM").
    * @return The corresponding FlatType enum value, or UNKNOWN if the input is invalid.
    */
    public static FlatType parseFlatType(String flatTypeString) {
        
        if (flatTypeString == null) return UNKNOWN;
    
        flatTypeString = flatTypeString.trim().toUpperCase().replace(" ", "_");
    
        if (flatTypeString.equals("2_ROOM") || flatTypeString.equals("2-ROOM") || flatTypeString.equals("TWO ROOM")) {
            flatTypeString = "TWO_ROOM";
        } else if (flatTypeString.equals("3_ROOM") || flatTypeString.equals("3-ROOM") || flatTypeString.equals("THREE ROOM")) {
            flatTypeString = "THREE_ROOM";
        }
    
        try {
            return FlatType.valueOf(flatTypeString);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid FlatType: " + flatTypeString);
            return UNKNOWN;
        }
    }
        
}
