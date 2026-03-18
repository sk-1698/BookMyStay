import java.util.*;
import java.io.*;
//UC12
public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - UC12 Data Persistence =====");

        RoomInventory inventory = new RoomInventory();
        BookingManager manager = new BookingManager(inventory);

        manager.bookRoom("Rohan", "Single");
        manager.bookRoom("Aryan", "Double");
        manager.bookRoom("Kiran", "Suite");

        manager.displayBookingHistory();

        manager.saveBookingsToFile();
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
}

// ================= BOOKING MANAGER =================
class BookingManager {

    private RoomInventory inventory;
    private List<String> bookingHistory;

    private int roomCounter = 100;

    public BookingManager(RoomInventory inventory) {

        this.inventory = inventory;
        bookingHistory = new ArrayList<>();
    }

    public void bookRoom(String customer, String type) {

        if (inventory.bookRoom(type)) {

            String roomId = generateRoomId(type);

            String record = customer + " -> " + roomId + " (" + type + ")";

            bookingHistory.add(record);

            System.out.println("Booking confirmed: " + record);
        }

        else {

            System.out.println("Booking failed for " + customer);
        }
    }

    private synchronized String generateRoomId(String type) {

        roomCounter++;

        return type.substring(0,1).toUpperCase() + roomCounter;
    }

    public void displayBookingHistory() {

        System.out.println("\nBooking History:");

        for (String record : bookingHistory) {

            System.out.println(record);
        }
    }

    // ================= UC12 FILE SAVE =================
    public void saveBookingsToFile() {

        try {

            FileWriter writer = new FileWriter("booking_history.txt");

            for (String record : bookingHistory) {

                writer.write(record + "\n");
            }

            writer.close();

            System.out.println("\nBooking history saved to file: booking_history.txt");

        } catch (IOException e) {

            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}