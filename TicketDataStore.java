import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TicketDataStore {
    public static final List<RouteRecord> routes = new ArrayList<>();
    public static final List<PassengerRecord> passengers = new ArrayList<>();
    public static final List<TicketRecord> tickets = new ArrayList<>();

    static {
        try {
            initializeDatabase();
            refreshAll();
        } catch (SQLException ex) {
            System.err.println("Database connection failed: " + ex.getMessage());
        }
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS routes ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "route_id VARCHAR(50) NOT NULL,"
                    + "from_city VARCHAR(100) NOT NULL,"
                    + "to_city VARCHAR(100) NOT NULL,"
                    + "vehicle_type VARCHAR(50) NOT NULL,"
                    + "available_seats INT NOT NULL,"
                    + "price_per_seat DOUBLE NOT NULL"
                    + ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS passengers ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(100) NOT NULL,"
                    + "contact VARCHAR(30) NOT NULL,"
                    + "email VARCHAR(100),"
                    + "address VARCHAR(255)"
                    + ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS tickets ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "ticket_no VARCHAR(50) NOT NULL,"
                    + "passenger_name VARCHAR(100) NOT NULL,"
                    + "route_pk INT NOT NULL,"
                    + "travel_date VARCHAR(30) NOT NULL,"
                    + "seat_no VARCHAR(20) NOT NULL,"
                    + "status VARCHAR(30) NOT NULL,"
                    + "FOREIGN KEY (route_pk) REFERENCES routes(id)"
                    + ")");
        }
        seedIfEmpty();
    }

    private static void seedIfEmpty() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM routes")) {
            rs.next();
            if (rs.getInt(1) > 0) {
                return;
            }
        }

        addRoute(new RouteRecord("R001", "Dhaka", "Chittagong", "Bus", 300, 850));
        addRoute(new RouteRecord("R002", "Dhaka", "Sylhet", "Bus", 180, 700));
        addRoute(new RouteRecord("R003", "Chittagong", "Dhaka", "Train", 300, 650));
        addRoute(new RouteRecord("R004", "Dhaka", "Rajshahi", "Train", 220, 600));
        addRoute(new RouteRecord("R005", "Dhaka", "Khulna", "Bus", 160, 750));

        addPassenger(new PassengerRecord("Alice Johnson", "01710000001", "alice@example.com", "Dhaka"));
        addPassenger(new PassengerRecord("Mike Johnson", "01710000002", "mike@example.com", "Chittagong"));
        addPassenger(new PassengerRecord("Nabila Ahmed", "01710000003", "nabila@example.com", "Sylhet"));

        refreshRoutes();
        issueTicket(new TicketRecord("T001", "Alice Johnson", routes.get(0), "2026-06-15", "1", "Confirmed"));
        issueTicket(new TicketRecord("T002", "Mike Johnson", routes.get(1), "2026-06-16", "2", "Confirmed"));
        issueTicket(new TicketRecord("T003", "Alice Johnson", routes.get(2), "2026-06-17", "6", "Pending"));
    }

    public static void refreshAll() throws SQLException {
        refreshRoutes();
        refreshPassengers();
        refreshTickets();
    }

    public static void refreshRoutes() throws SQLException {
        routes.clear();
        String sql = "SELECT id, route_id, from_city, to_city, vehicle_type, available_seats, price_per_seat FROM routes ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                routes.add(new RouteRecord(
                        rs.getInt("id"),
                        rs.getString("route_id"),
                        rs.getString("from_city"),
                        rs.getString("to_city"),
                        rs.getString("vehicle_type"),
                        rs.getInt("available_seats"),
                        rs.getDouble("price_per_seat")
                ));
            }
        }
    }

    public static void refreshPassengers() throws SQLException {
        passengers.clear();
        String sql = "SELECT id, name, contact, email, address FROM passengers ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                passengers.add(new PassengerRecord(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("address")
                ));
            }
        }
    }

    public static void refreshTickets() throws SQLException {
        tickets.clear();
        String sql = "SELECT t.id, t.ticket_no, t.passenger_name, t.travel_date, t.seat_no, t.status, "
                + "r.id AS route_pk, r.route_id, r.from_city, r.to_city, r.vehicle_type, r.available_seats, r.price_per_seat "
                + "FROM tickets t INNER JOIN routes r ON t.route_pk = r.id ORDER BY t.id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RouteRecord route = new RouteRecord(
                        rs.getInt("route_pk"),
                        rs.getString("route_id"),
                        rs.getString("from_city"),
                        rs.getString("to_city"),
                        rs.getString("vehicle_type"),
                        rs.getInt("available_seats"),
                        rs.getDouble("price_per_seat")
                );
                tickets.add(new TicketRecord(
                        rs.getInt("id"),
                        rs.getString("ticket_no"),
                        rs.getString("passenger_name"),
                        route,
                        rs.getString("travel_date"),
                        rs.getString("seat_no"),
                        rs.getString("status")
                ));
            }
        }
    }

    public static void addPassenger(PassengerRecord passenger) throws SQLException {
        String sql = "INSERT INTO passengers (name, contact, email, address) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, passenger.name);
            ps.setString(2, passenger.contact);
            ps.setString(3, passenger.email);
            ps.setString(4, passenger.address);
            ps.executeUpdate();
        }
        refreshPassengers();
    }

    public static void deletePassenger(PassengerRecord passenger) throws SQLException {
        String sql = "DELETE FROM passengers WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, passenger.id);
            ps.executeUpdate();
        }
        refreshPassengers();
    }

    public static void addRoute(RouteRecord route) throws SQLException {
        String sql = "INSERT INTO routes (route_id, from_city, to_city, vehicle_type, available_seats, price_per_seat) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, route.routeId);
            ps.setString(2, route.from);
            ps.setString(3, route.to);
            ps.setString(4, route.vehicleType);
            ps.setInt(5, route.availableSeats);
            ps.setDouble(6, route.pricePerSeat);
            ps.executeUpdate();
        }
        refreshRoutes();
    }

    public static void issueTicket(TicketRecord ticket) throws SQLException {
        String insertSql = "INSERT INTO tickets (ticket_no, passenger_name, route_pk, travel_date, seat_no, status) VALUES (?, ?, ?, ?, ?, ?)";
        String seatSql = "UPDATE routes SET available_seats = available_seats - 1 WHERE id = ? AND available_seats > 0";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement seatPs = con.prepareStatement(seatSql);
                 PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                seatPs.setInt(1, ticket.route.id);
                int updated = seatPs.executeUpdate();
                if (updated == 0) {
                    con.rollback();
                    throw new SQLException("No seats available for this route.");
                }

                insertPs.setString(1, ticket.ticketNo);
                insertPs.setString(2, ticket.passengerName);
                insertPs.setInt(3, ticket.route.id);
                insertPs.setString(4, ticket.date);
                insertPs.setString(5, ticket.seatNo);
                insertPs.setString(6, ticket.status);
                insertPs.executeUpdate();
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
        refreshAll();
    }

    public static void cancelTicket(TicketRecord ticket) throws SQLException {
        String updateTicket = "UPDATE tickets SET status = 'Cancelled' WHERE id = ? AND status <> 'Cancelled'";
        String updateRoute = "UPDATE routes SET available_seats = available_seats + 1 WHERE id = ?";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ticketPs = con.prepareStatement(updateTicket);
                 PreparedStatement routePs = con.prepareStatement(updateRoute)) {
                ticketPs.setInt(1, ticket.id);
                int updated = ticketPs.executeUpdate();
                if (updated > 0) {
                    routePs.setInt(1, ticket.route.id);
                    routePs.executeUpdate();
                }
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
        refreshAll();
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
    int id;
    String routeId;
    String from;
    String to;
    String vehicleType;
    int availableSeats;
    double pricePerSeat;

    RouteRecord(String routeId, String from, String to, String vehicleType, int availableSeats, double pricePerSeat) {
        this(0, routeId, from, to, vehicleType, availableSeats, pricePerSeat);
    }

    RouteRecord(int id, String routeId, String from, String to, String vehicleType, int availableSeats, double pricePerSeat) {
        this.id = id;
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
    int id;
    String name;
    String contact;
    String email;
    String address;

    PassengerRecord(String name, String contact, String email, String address) {
        this(0, name, contact, email, address);
    }

    PassengerRecord(int id, String name, String contact, String email, String address) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
    }

    @Override
    public String toString() {
        return name + " - " + contact;
    }
}

class TicketRecord {
    int id;
    String ticketNo;
    String passengerName;
    RouteRecord route;
    String date;
    String seatNo;
    String status;

    TicketRecord(String ticketNo, String passengerName, RouteRecord route, String date, String seatNo, String status) {
        this(0, ticketNo, passengerName, route, date, seatNo, status);
    }

    TicketRecord(int id, String ticketNo, String passengerName, RouteRecord route, String date, String seatNo, String status) {
        this.id = id;
        this.ticketNo = ticketNo;
        this.passengerName = passengerName;
        this.route = route;
        this.date = date;
        this.seatNo = seatNo;
        this.status = status;
    }
}
