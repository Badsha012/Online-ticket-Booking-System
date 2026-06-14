import java.util.ArrayList;
import java.util.List;

public class TicketDataStore {
    public static final List<RouteRecord> routes = new ArrayList<>();
    public static final List<PassengerRecord> passengers = new ArrayList<>();
    public static final List<TicketRecord> tickets = new ArrayList<>();

    static {
        routes.add(new RouteRecord("R001", "Dhaka", "Chittagong", "Bus", 300, 850));
        routes.add(new RouteRecord("R002", "Dhaka", "Sylhet", "Bus", 180, 700));
        routes.add(new RouteRecord("R003", "Chittagong", "Dhaka", "Train", 300, 650));
        routes.add(new RouteRecord("R004", "Dhaka", "Rajshahi", "Train", 220, 600));
        routes.add(new RouteRecord("R005", "Dhaka", "Khulna", "Bus", 160, 750));

        passengers.add(new PassengerRecord("Alice Johnson", "01710000001", "alice@example.com", "Dhaka"));
        passengers.add(new PassengerRecord("Mike Johnson", "01710000002", "mike@example.com", "Chittagong"));
        passengers.add(new PassengerRecord("Nabila Ahmed", "01710000003", "nabila@example.com", "Sylhet"));

        tickets.add(new TicketRecord("T001", "Alice Johnson", routes.get(0), "2026-06-15", "1", "Confirmed"));
        tickets.add(new TicketRecord("T002", "Mike Johnson", routes.get(1), "2026-06-16", "2", "Confirmed"));
        tickets.add(new TicketRecord("T003", "Alice Johnson", routes.get(2), "2026-06-17", "6", "Pending"));
    }

    public static int getIssuedCount() {
        return tickets.size();
    }

    public static int getAvailableSeats() {
        int total = 0;
        for (RouteRecord route : routes) {
            total += route.availableSeats;
        }
        return total;
    }

    public static String nextTicketNo() {
        return String.format("T%03d", tickets.size() + 1);
    }
}

class RouteRecord {
    String routeId;
    String from;
    String to;
    String vehicleType;
    int availableSeats;
    double pricePerSeat;

    RouteRecord(String routeId, String from, String to, String vehicleType, int availableSeats, double pricePerSeat) {
        this.routeId = routeId;
        this.from = from;
        this.to = to;
        this.vehicleType = vehicleType;
        this.availableSeats = availableSeats;
        this.pricePerSeat = pricePerSeat;
    }

    @Override
    public String toString() {
        return routeId + " - " + from + " to " + to;
    }
}

class PassengerRecord {
    String name;
    String contact;
    String email;
    String address;

    PassengerRecord(String name, String contact, String email, String address) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
    }
}

class TicketRecord {
    String ticketNo;
    String passengerName;
    RouteRecord route;
    String date;
    String seatNo;
    String status;

    TicketRecord(String ticketNo, String passengerName, RouteRecord route, String date, String seatNo, String status) {
        this.ticketNo = ticketNo;
        this.passengerName = passengerName;
        this.route = route;
        this.date = date;
        this.seatNo = seatNo;
        this.status = status;
    }
}
