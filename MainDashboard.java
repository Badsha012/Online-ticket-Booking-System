import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainDashboard extends JFrame {
    private final JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
    private final DefaultTableModel recentModel;
    private final DefaultTableModel routesModel;

    public MainDashboard() {
        AppStyle.applyLookAndFeel();
        setTitle("Online Ticket Booking System");
        setSize(1180, 720);
        setMinimumSize(new Dimension(980, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        JPanel mainContent = AppStyle.pagePanel(new BorderLayout(18, 18));
        mainContent.add(createHeader(), BorderLayout.NORTH);

        cardsPanel.setBackground(AppStyle.BACKGROUND);

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        tablesPanel.setBackground(AppStyle.BACKGROUND);

        recentModel = new DefaultTableModel(new String[]{"Passenger", "Route", "Date", "Seat", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable recentTable = new JTable(recentModel);
        AppStyle.styleTable(recentTable);
        recentTable.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());

        routesModel = new DefaultTableModel(new String[]{"Route ID", "From", "To", "Vehicle", "Seats"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable routesTable = new JTable(routesModel);
        AppStyle.styleTable(routesTable);

        tablesPanel.add(createTablePanel("Recent Ticket Bookings", "Latest sales and cancellation status", recentTable));
        tablesPanel.add(createTablePanel("Active Routes", "Current vehicles and available capacity", routesTable));

        JPanel centerContainer = new JPanel(new BorderLayout(0, 18));
        centerContainer.setBackground(AppStyle.BACKGROUND);
        centerContainer.add(cardsPanel, BorderLayout.NORTH);
        centerContainer.add(tablesPanel, BorderLayout.CENTER);
        mainContent.add(centerContainer, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
        refreshDashboard();
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppStyle.BACKGROUND);

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 2));
        titleBlock.setBackground(AppStyle.BACKGROUND);
        JLabel title = AppStyle.titleLabel("Online Ticket Booking System", 24);
        JLabel subtitle = new JLabel("Manage routes, passengers, bookings and cancellations");
        subtitle.setForeground(AppStyle.MUTED);
        titleBlock.add(title);
        titleBlock.add(subtitle);

        JPanel adminPanel = AppStyle.surfacePanel(new BorderLayout(8, 0));
        adminPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppStyle.BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        JLabel admin = new JLabel("Admin");
        admin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        admin.setForeground(AppStyle.TEXT);
        JLabel role = new JLabel("System Operator");
        role.setForeground(AppStyle.MUTED);
        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setBackground(Color.WHITE);
        text.add(admin);
        text.add(role);
        adminPanel.add(text, BorderLayout.CENTER);

        headerPanel.add(titleBlock, BorderLayout.WEST);
        headerPanel.add(adminPanel, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(AppStyle.NAVY);
        sidebar.setPreferredSize(new Dimension(240, 720));
        sidebar.setBorder(BorderFactory.createEmptyBorder(18, 14, 18, 14));

        JPanel brand = new JPanel(new GridLayout(2, 1, 0, 2));
        brand.setBackground(AppStyle.NAVY);
        JLabel name = new JLabel("TicketPro");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel tagline = new JLabel("Booking Management");
        tagline.setForeground(new Color(190, 203, 224));
        brand.add(name);
        brand.add(tagline);

        JPanel menu = new JPanel(new GridLayout(10, 1, 0, 8));
        menu.setOpaque(false);
        menu.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));

        JButton btnHome = createSidebarButton("Home");
        JButton btnRoutes = createSidebarButton("Routes & Vehicles");
        JButton btnPassengers = createSidebarButton("Passengers");
        JButton btnIssued = createSidebarButton("Issued Tickets");
        JButton btnAddRoute = createSidebarButton("+ Add Route");
        JButton btnAddPassenger = createSidebarButton("+ Add Passenger");
        JButton btnIssueTicket = createSidebarButton("Issue Ticket");
        JButton btnCancelTicket = createSidebarButton("Cancel Ticket");
        JButton btnReports = createSidebarButton("Reports");
        JButton btnLogout = createSidebarButton("Logout");

        menu.add(btnHome);
        menu.add(btnRoutes);
        menu.add(btnPassengers);
        menu.add(btnIssued);
        menu.add(btnAddRoute);
        menu.add(btnAddPassenger);
        menu.add(btnIssueTicket);
        menu.add(btnCancelTicket);
        menu.add(btnReports);
        menu.add(btnLogout);

        sidebar.add(brand, BorderLayout.NORTH);
        sidebar.add(menu, BorderLayout.CENTER);

        btnHome.addActionListener(e -> refreshDashboard());
        btnAddRoute.addActionListener(e -> new AddRouteForm(this::refreshDashboard).setVisible(true));
        btnAddPassenger.addActionListener(e -> new AddPassengerForm(this::refreshDashboard).setVisible(true));
        btnIssueTicket.addActionListener(e -> new IssueTicketForm(this::refreshDashboard).setVisible(true));
        btnIssued.addActionListener(e -> new IssuedTicketsForm(this::refreshDashboard).setVisible(true));
        btnCancelTicket.addActionListener(e -> new IssuedTicketsForm(this::refreshDashboard).setVisible(true));
        btnRoutes.addActionListener(e -> JOptionPane.showMessageDialog(this, "Total active routes: " + TicketDataStore.routes.size()));
        btnPassengers.addActionListener(e -> JOptionPane.showMessageDialog(this, "Total passengers: " + TicketDataStore.passengers.size()));
        btnReports.addActionListener(e -> showReport());
        btnLogout.addActionListener(e -> dispose());

        return sidebar;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(new Color(241, 245, 249));
        button.setBackground(AppStyle.NAVY);
        button.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(AppStyle.NAVY_HOVER);
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(AppStyle.NAVY);
                button.setForeground(new Color(241, 245, 249));
            }
        });
        return button;
    }

    private JPanel createCard(String title, String value, String helper, Color accent) {
        JPanel card = AppStyle.surfacePanel(new BorderLayout(12, 0));
        card.setPreferredSize(new Dimension(180, 96));

        JPanel accentBox = new JPanel();
        accentBox.setBackground(accent);
        accentBox.setPreferredSize(new Dimension(8, 70));
        card.add(accentBox, BorderLayout.WEST);

        JPanel info = new JPanel(new GridLayout(3, 1, 0, 2));
        info.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(AppStyle.MUTED);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(AppStyle.TEXT);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        JLabel helperLabel = new JLabel(helper);
        helperLabel.setForeground(AppStyle.MUTED);
        helperLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        info.add(titleLabel);
        info.add(valueLabel);
        info.add(helperLabel);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JPanel createTablePanel(String title, String subtitle, JTable table) {
        JPanel panel = AppStyle.surfacePanel(new BorderLayout(0, 12));
        JPanel heading = new JPanel(new GridLayout(2, 1));
        heading.setBackground(Color.WHITE);
        JLabel titleLabel = AppStyle.titleLabel(title, 16);
        JLabel subLabel = new JLabel(subtitle);
        subLabel.setForeground(AppStyle.MUTED);
        heading.add(titleLabel);
        heading.add(subLabel);
        panel.add(heading, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void refreshDashboard() {
        cardsPanel.removeAll();
        cardsPanel.add(createCard("Total Routes", String.valueOf(TicketDataStore.routes.size()), "active destinations", new Color(37, 99, 235)));
        cardsPanel.add(createCard("Passengers", String.valueOf(TicketDataStore.passengers.size()), "registered users", new Color(22, 163, 74)));
        cardsPanel.add(createCard("Issued Tickets", String.valueOf(TicketDataStore.getIssuedCount()), "all time bookings", new Color(217, 119, 6)));
        cardsPanel.add(createCard("Available Seats", String.valueOf(TicketDataStore.getAvailableSeats()), "remaining capacity", new Color(220, 38, 38)));
        cardsPanel.revalidate();
        cardsPanel.repaint();

        recentModel.setRowCount(0);
        for (TicketRecord ticket : TicketDataStore.tickets) {
            recentModel.addRow(new Object[]{ticket.passengerName, ticket.route.to, ticket.date, ticket.seatNo, ticket.status});
        }

        routesModel.setRowCount(0);
        for (RouteRecord route : TicketDataStore.routes) {
            routesModel.addRow(new Object[]{route.routeId, route.from, route.to, route.vehicleType, route.availableSeats});
        }
    }

    private void showReport() {
        JOptionPane.showMessageDialog(this,
                "Routes: " + TicketDataStore.routes.size()
                        + "\nPassengers: " + TicketDataStore.passengers.size()
                        + "\nTickets: " + TicketDataStore.tickets.size()
                        + "\nAvailable Seats: " + TicketDataStore.getAvailableSeats(),
                "System Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboard().setVisible(true));
    }
}

class StatusRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String status = String.valueOf(value);
        if (!isSelected) {
            component.setBackground(Color.WHITE);
            if ("Confirmed".equals(status)) {
                component.setForeground(AppStyle.SUCCESS);
            } else if ("Pending".equals(status)) {
                component.setForeground(AppStyle.WARNING);
            } else {
                component.setForeground(AppStyle.DANGER);
            }
        }
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        return component;
    }
}
