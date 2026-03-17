
import java.util.*;

public class main {

    // RESERVATION
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
    }

    // INVENTORY
    static class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 2);
            availability.put("Double", 1);
            availability.put("Suite", 1);
        }

        public int getAvailable(String type) {
            return availability.getOrDefault(type, 0);
        }

        public void reduce(String type) {
            availability.put(type, availability.get(type) - 1);
        }
    }

    // BOOKING QUEUE (FIFO)
    static class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void add(Reservation r) { queue.offer(r); }
        public Reservation next() { return queue.poll(); }
        public boolean hasNext() { return !queue.isEmpty(); }
    }

    // ROOM ALLOCATION SERVICE
    static class RoomAllocationService {

        private Set<String> allocatedRoomIds = new HashSet<>();
        private Map<String, Set<String>> assignedRooms = new HashMap<>();

        public void allocate(Reservation r, RoomInventory inventory) {

            String type = r.getRoomType();

            // Check availability
            if (inventory.getAvailable(type) <= 0) {
                System.out.println("No rooms available for " + r.getGuestName());
                return;
            }

            // Generate unique ID
            String roomId = generateRoomId(type);

            // Store allocated ID
            allocatedRoomIds.add(roomId);

            assignedRooms.putIfAbsent(type, new HashSet<>());
            assignedRooms.get(type).add(roomId);

            // Reduce inventory
            inventory.reduce(type);

            // Confirm booking
            System.out.println("Booking confirmed for Guest: "
                    + r.getGuestName() + ", Room ID: " + roomId);
        }

        // Generate unique room ID
        private String generateRoomId(String type) {
            int count = assignedRooms.getOrDefault(type, new HashSet<>()).size() + 1;
            return type + "-" + count;
        }
    }

    // MAIN
    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        // Requests
        queue.add(new Reservation("Abhi", "Single"));
        queue.add(new Reservation("Subha", "Single"));
        queue.add(new Reservation("Vamathi", "Suite"));

        // Process FIFO
        while (queue.hasNext()) {
            service.allocate(queue.next(), inventory);
        }
    }
}