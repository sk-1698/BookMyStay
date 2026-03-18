import java.util.*;

public class BookMyStay {
//UC11
    public static void main(String[] args) {

        System.out.println("===== Book My Stay - UC11 Concurrent Booking =====");

        RoomInventory inventory = new RoomInventory();
        BookingManager manager = new BookingManager(inventory);

        // Simulating multiple users booking at same time
        Thread t1 = new Thread(() -> manager.bookRoom("Rohan", "Single"));
        Thread t2 = new Thread(() -> manager.bookRoom("Aryan", "Single"));
        Thread t3 = new Thread(() -> manager.bookRoom("Kiran", "Single"));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        inventory.displayInventory();
    }
}

// ================= INVENTORY =================
class RoomInventory {

    private HashMap<String, Integer> availability;

    public RoomInventory() {

        availability = new HashMap<>();

        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    // synchronized to avoid race condition
    public synchronized boolean bookRoom(String type) {

        if (!availability.containsKey(type)) {
            System.out.println("Invalid room type: " + type);
            return false;
        }

        if (availability.get(type) > 0) {

            availability.put(type, availability.get(type) - 1);

            return true;
        }

        return false;
    }

    public synchronized void displayInventory() {

        System.out.println("\nRemaining Rooms:");

        for (String type : availability.keySet()) {

            System.out.println(type + " : " + availability.get(type));
        }
    }
}

// ================= BOOKING MANAGER =================
class BookingManager {

    private RoomInventory inventory;

    private int roomCounter = 100;

    public BookingManager(RoomInventory inventory) {

        this.inventory = inventory;
    }

    public void bookRoom(String customer, String type) {

        if (inventory.bookRoom(type)) {

            String roomId = generateRoomId(type);

            System.out.println(customer +
                    " successfully booked room " +
                    roomId +
                    " (" + type + ")");
        }

        else {

            System.out.println("Booking failed for " +
                    customer +
                    " (No rooms available)");
        }
    }

    private synchronized String generateRoomId(String type) {

        roomCounter++;

        return type.substring(0,1).toUpperCase() + roomCounter;
    }
}