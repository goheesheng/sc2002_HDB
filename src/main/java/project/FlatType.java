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
       // Normalize the input (e.g., remove spaces, convert to uppercase, and replace "2_ROOM" with "TWO_ROOM")
       flatTypeString = flatTypeString.trim().toUpperCase().replace(" ", "_");

       // Map common misrepresentations to standard enum values
       if (flatTypeString.equals("2-ROOM")) {
           flatTypeString = "TWO_ROOM"; // Map "2_ROOM" to "TWO_ROOM"
       } else if (flatTypeString.equals("3-ROOM")){
            flatTypeString = "THREE_ROOM"; // Map "3_ROOM" to "THREE_ROOM"
       }

       try {
           // Attempt to convert the string to the corresponding FlatType enum
           return FlatType.valueOf(flatTypeString);
       } catch (IllegalArgumentException e) {
           // Return UNKNOWN if the string doesn't match any valid enum value
           System.err.println("Invalid FlatType: " + flatTypeString);
           return FlatType.UNKNOWN; // Return UNKNOWN when the input is invalid
       }
   }
}
