import java.util.*;

public class main {

    // CUSTOM EXCEPTION
    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

    // INVENTORY
    static class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 5);
            availability.put("Double", 3);
            availability.put("Suite", 2);
        }

        public boolean isValidRoomType(String type) {
            return availability.containsKey(type);
        }
    }

    // RESERVATION VALIDATOR
    static class ReservationValidator {

        public void validate(String guestName, String roomType, RoomInventory inventory)
                throws InvalidBookingException {

            if (guestName == null || guestName.trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty.");
            }

            if (!inventory.isValidRoomType(roomType)) {
                throw new InvalidBookingException("Invalid room type selected.");
            }
        }
    }

    // BOOKING QUEUE
    static class BookingRequestQueue {
        private Queue<String> queue = new LinkedList<>();

        public void add(String request) {
            queue.offer(request);
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue queue = new BookingRequestQueue();

        try {
            System.out.print("Enter guest name: ");
            String name = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validate input
            validator.validate(name, roomType, inventory);

            // If valid, add to queue
            queue.add(name + " - " + roomType);

            System.out.println("Booking request added successfully.");

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}