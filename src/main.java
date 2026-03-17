import java.io.*;
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

        public Map<String, Integer> getAll() {
            return availability;
        }

        public void set(String type, int count) {
            availability.put(type, count);
        }
    }

    // FILE PERSISTENCE SERVICE
    static class FilePersistenceService {

        // Save inventory to file
        public void saveInventory(RoomInventory inventory, String filePath) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

                for (Map.Entry<String, Integer> entry : inventory.getAll().entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue());
                    writer.newLine();
                }

                System.out.println("Inventory saved successfully.");

            } catch (IOException e) {
                System.out.println("Error saving inventory.");
            }
        }

        // Load inventory from file
        public void loadInventory(RoomInventory inventory, String filePath) {
            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("No valid inventory data found. Starting fresh.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    inventory.set(parts[0], Integer.parseInt(parts[1]));
                }

                System.out.println("Inventory loaded successfully.");

            } catch (Exception e) {
                System.out.println("Error loading inventory. Starting fresh.");
            }
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        System.out.println("System Recovery");

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService service = new FilePersistenceService();

        String filePath = "inventory.txt";

        // Load data
        service.loadInventory(inventory, filePath);

        // Display inventory
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getAll().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Save data
        service.saveInventory(inventory, filePath);
    }
}