import java.util.*;

public class BookMyStay{
//UC9
    public static void main(String[] args) {

        System.out.println("===== Book My Stay - UC9 Error Handling =====");

        RoomInventory inventory = new RoomInventory();
        BookingManager manager = new BookingManager(inventory);

        manager.addBookingRequest("Rohan", "Single");
        manager.addBookingRequest("Aryan", "Double");
        manager.addBookingRequest("Kiran", "Suite");
        manager.addBookingRequest("Vijay", "Luxury"); // invalid type

        manager.processBookings();

        inventory.displayInventory();
        manager.displayServices();
        manager.displayBookingHistory();
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

    public void displayInventory() {

        System.out.println("\nRemaining Rooms:");

        for (String type : availability.keySet()) {

            System.out.println(type + " : " + availability.get(type));
        }
    }
}


class BookingManager {

    private Queue<BookingRequest> queue;
    private RoomInventory inventory;

    private Set<String> allocatedRoomIds;
    private HashMap<String, Set<String>> roomTypeMap;
    private HashMap<String, List<String>> roomServices;

    private List<String> bookingHistory;

    private int roomCounter = 100;

    public BookingManager(RoomInventory inventory) {

        this.inventory = inventory;

        queue = new LinkedList<>();
        allocatedRoomIds = new HashSet<>();
        roomTypeMap = new HashMap<>();
        roomServices = new HashMap<>();
        bookingHistory = new ArrayList<>();
    }

    public void addBookingRequest(String name, String roomType) {

        if (name == null || name.isEmpty()) {

            System.out.println("Invalid customer name.");
            return;
        }

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

                    allocatedRoomIds.add(roomId);

                    roomTypeMap.putIfAbsent(request.roomType, new HashSet<>());
                    roomTypeMap.get(request.roomType).add(roomId);

                    addDefaultServices(roomId);

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

                System.out.println("Error processing booking for "
                        + request.customerName + ": " + e.getMessage());
            }
        }
    }

    private String generateRoomId(String type) {

        roomCounter++;

        return type.substring(0, 1).toUpperCase() + roomCounter;
    }


    private void addDefaultServices(String roomId) {

        List<String> services = new ArrayList<>();

        services.add("WiFi");
        services.add("Breakfast");

        roomServices.put(roomId, services);
    }

    public void displayServices() {

        System.out.println("\nRoom Services:");

        for (String roomId : roomServices.keySet()) {

            System.out.println(roomId + " -> " + roomServices.get(roomId));
        }
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