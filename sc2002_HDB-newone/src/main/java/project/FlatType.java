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
    THREE_ROOM;
  
    /**
     * Converts a string representation of a flat type to the corresponding enum value.
     * Handles variations and invalid input by returning null.
     * 
     * @param flatTypeString The string representation of a flat type (e.g., "2_ROOM", "TWO ROOM").
     * @return The corresponding FlatType enum value, or null if the input is invalid.
     */
    public static FlatType parseFlatType(String flatTypeString) {
        if (flatTypeString == null) return null;

        flatTypeString = flatTypeString.trim().toUpperCase().replace(" ", "_");

        if (flatTypeString.equals("2_ROOM") || flatTypeString.equals("2-ROOM") || flatTypeString.equals("TWO_ROOM") || flatTypeString.equals("TWO ROOM")) {
            return TWO_ROOM;
        } else if (flatTypeString.equals("3_ROOM") || flatTypeString.equals("3-ROOM") || flatTypeString.equals("THREE_ROOM") || flatTypeString.equals("THREE ROOM")) {
            return THREE_ROOM;
        } else {
            System.err.println("Invalid FlatType: " + flatTypeString);
            return null;
        }
    }
}