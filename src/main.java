
import java.util.*;

public class main {

    // INVENTORY
    static class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 5);
            availability.put("Double", 3);
            availability.put("Suite", 2);
        }

        public void increase(String type) {
            availability.put(type, availability.get(type) + 1);
        }

        public int getAvailable(String type) {
            return availability.get(type);
        }
    }

    // CANCELLATION SERVICE
    static class CancellationService {

        private Stack<String> releasedRoomIds;
        private Map<String, String> reservationRoomMap;

        public CancellationService() {
            releasedRoomIds = new Stack<>();
            reservationRoomMap = new HashMap<>();
        }

        // Register booking
        public void registerBooking(String reservationId, String roomType) {
            reservationRoomMap.put(reservationId, roomType);
        }

        // Cancel booking
        public void cancelBooking(String reservationId, RoomInventory inventory) {

            if (!reservationRoomMap.containsKey(reservationId)) {
                System.out.println("Invalid cancellation request.");
                return;
            }

            String roomType = reservationRoomMap.get(reservationId);

            // Push to stack (rollback)
            releasedRoomIds.push(reservationId);

            // Restore inventory
            inventory.increase(roomType);

            // Remove booking
            reservationRoomMap.remove(reservationId);

            System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        }

        // Show rollback history
        public void showRollbackHistory() {
            System.out.println("\nRollback History (Most Recent First):");
            for (int i = releasedRoomIds.size() - 1; i >= 0; i--) {
                System.out.println("Released Reservation ID: " + releasedRoomIds.get(i));
            }
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        // Simulate confirmed booking
        service.registerBooking("Single-1", "Single");

        // Cancel booking
        service.cancelBooking("Single-1", inventory);

        // Show rollback history
        service.showRollbackHistory();

        // Show updated inventory
        System.out.println("\nUpdated Single Room Availability: "
                + inventory.getAvailable("Single"));
    }
}