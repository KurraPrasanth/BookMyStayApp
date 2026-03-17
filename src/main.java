
import java.util.*;

public class main {

    // SERVICE CLASS
    static class Service {
        private String serviceName;
        private double cost;

        public Service(String serviceName, double cost) {
            this.serviceName = serviceName;
            this.cost = cost;
        }

        public String getServiceName() {
            return serviceName;
        }

        public double getCost() {
            return cost;
        }
    }

    // ADD-ON SERVICE MANAGER
    static class AddOnServiceManager {

        private Map<String, List<Service>> servicesByReservation;

        public AddOnServiceManager() {
            servicesByReservation = new HashMap<>();
        }

        // Add service to reservation
        public void addService(String reservationId, Service service) {
            servicesByReservation.putIfAbsent(reservationId, new ArrayList<>());
            servicesByReservation.get(reservationId).add(service);
        }

        // Calculate total cost
        public double calculateTotalServiceCost(String reservationId) {
            double total = 0.0;

            List<Service> services = servicesByReservation.get(reservationId);
            if (services != null) {
                for (Service s : services) {
                    total += s.getCost();
                }
            }
            return total;
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        System.out.println("Add-On Service Selection\n");

        AddOnServiceManager manager = new AddOnServiceManager();

        // Example reservation ID (from previous use case)
        String reservationId = "Single-1";

        // Add services
        manager.addService(reservationId, new Service("Breakfast", 500));
        manager.addService(reservationId, new Service("Spa", 1000));

        // Calculate total cost
        double totalCost = manager.calculateTotalServiceCost(reservationId);

        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Total Add-On Cost: " + totalCost);
    }
}