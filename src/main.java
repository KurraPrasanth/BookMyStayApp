import java.util.*;

public class main {

    // RESERVATION
    static class Reservation {
        String guestName;
        String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    // BOOKING QUEUE
    static class BookingRequestQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void add(Reservation r) {
            queue.offer(r);
        }

        public Reservation getNext() {
            return queue.poll();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // INVENTORY
    static class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 3);
            availability.put("Double", 2);
            availability.put("Suite", 3);
        }

        public int getAvailable(String type) {
            return availability.getOrDefault(type, 0);
        }

        public void reduce(String type) {
            availability.put(type, availability.get(type) - 1);
        }

        public Map<String, Integer> getAll() {
            return availability;
        }
    }

    // ALLOCATION SERVICE
    static class RoomAllocationService {

        private Map<String, Integer> counters = new HashMap<>();

        public void allocateRoom(Reservation r, RoomInventory inventory) {

            String type = r.roomType;

            if (inventory.getAvailable(type) <= 0) {
                return;
            }

            int count = counters.getOrDefault(type, 0) + 1;
            counters.put(type, count);

            String roomId = type + "-" + count;

            inventory.reduce(type);

            System.out.println("Booking confirmed for Guest: "
                    + r.guestName + ", Room ID: " + roomId);
        }
    }

    // THREAD PROCESSOR
    static class ConcurrentBookingProcessor implements Runnable {

        private BookingRequestQueue bookingQueue;
        private RoomInventory inventory;
        private RoomAllocationService allocationService;

        public ConcurrentBookingProcessor(
                BookingRequestQueue bookingQueue,
                RoomInventory inventory,
                RoomAllocationService allocationService) {
            this.bookingQueue = bookingQueue;
            this.inventory = inventory;
            this.allocationService = allocationService;
        }

        @Override
        public void run() {

            while (true) {
                Reservation reservation;

                // Sync queue
                synchronized (bookingQueue) {
                    if (bookingQueue.isEmpty()) return;
                    reservation = bookingQueue.getNext();
                }

                // Sync inventory
                synchronized (inventory) {
                    allocationService.allocateRoom(reservation, inventory);
                }
            }
        }
    }

    // MAIN
    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        // Add requests
        queue.add(new Reservation("Abhi", "Single"));
        queue.add(new Reservation("Vamathi", "Double"));
        queue.add(new Reservation("Kural", "Suite"));
        queue.add(new Reservation("Subha", "Single"));

        // Create threads
        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));

        // Start threads
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        // Print remaining inventory
        System.out.println("\nRemaining Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getAll().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}