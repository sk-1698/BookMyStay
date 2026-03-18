import java.util.*;

public class BookMyStay {
//UC10
    public static void main(String[] args) {

        System.out.println("===== Book My Stay - UC10 Booking Cancellation =====");

        RoomInventory inventory = new RoomInventory();
        BookingManager manager = new BookingManager(inventory);

        manager.addBookingRequest("Rohan", "Single");
        manager.addBookingRequest("Aryan", "Double");
        manager.addBookingRequest("Kiran", "Suite");

        manager.processBookings();

        manager.displayBookingHistory();


        manager.cancelBooking("S101");

        inventory.displayInventory();
    }
}


class RoomInventory {

    private HashMap<String, Integer> availability;

    public RoomInventory() {

        availability = new HashMap<>();

        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public boolean bookRoom(String type) throws Exception {

        if (!availability.containsKey(type)) {
            throw new Exception("Invalid Room Type: " + type);
        }

        if (availability.get(type) > 0) {

            availability.put(type, availability.get(type) - 1);
            return true;
        }

        return false;
    }

    public void returnRoom(String type) {

        availability.put(type, availability.get(type) + 1);
    }

    public void displayInventory() {

        System.out.println("\nCurrent Inventory:");

        for (String type : availability.keySet()) {

            System.out.println(type + " : " + availability.get(type));
        }
    }
}


class BookingManager {

    private Queue<BookingRequest> queue;
    private RoomInventory inventory;

    private HashMap<String, String> roomBookings;
    private List<String> bookingHistory;

    private int roomCounter = 100;

    public BookingManager(RoomInventory inventory) {

        this.inventory = inventory;

        queue = new LinkedList<>();
        roomBookings = new HashMap<>();
        bookingHistory = new ArrayList<>();
    }

    public void addBookingRequest(String name, String roomType) {

        queue.add(new BookingRequest(name, roomType));

        System.out.println("Booking request added : " + name);
    }

    public void processBookings() {

        System.out.println("\nProcessing bookings...");

        while (!queue.isEmpty()) {

            BookingRequest request = queue.poll();

            try {

                if (inventory.bookRoom(request.roomType)) {

                    String roomId = generateRoomId(request.roomType);

                    roomBookings.put(roomId, request.roomType);

                    String record = request.customerName + " -> " + roomId;

                    bookingHistory.add(record);

                    System.out.println("Reservation confirmed for "
                            + request.customerName +
                            " | Room ID: " + roomId);
                }

                else {

                    System.out.println("No rooms available for " + request.customerName);
                }

            } catch (Exception e) {

                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    public void cancelBooking(String roomId) {

        if (!roomBookings.containsKey(roomId)) {

            System.out.println("Invalid Room ID: " + roomId);
            return;
        }

        String roomType = roomBookings.get(roomId);

        inventory.returnRoom(roomType);

        roomBookings.remove(roomId);

        System.out.println("Booking cancelled for Room ID: " + roomId);
    }

    private String generateRoomId(String type) {

        roomCounter++;

        return type.substring(0,1).toUpperCase() + roomCounter;
    }

    public void displayBookingHistory() {

        System.out.println("\nBooking History:");

        for (String record : bookingHistory) {

            System.out.println(record);
        }
    }
}


class BookingRequest {

    String customerName;
    String roomType;

    public BookingRequest(String name, String type) {

        this.customerName = name;
        this.roomType = type;
    }
}