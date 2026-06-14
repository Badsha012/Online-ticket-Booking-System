import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class IssueTicketForm extends JFrame {
    public IssueTicketForm() {
        this(null);
    }

    public IssueTicketForm(Runnable onSaved) {
        AppStyle.applyLookAndFeel();
        setTitle("Issue Ticket");
        setSize(540, 480);
        setMinimumSize(new Dimension(520, 450));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel page = AppStyle.pagePanel(new BorderLayout(0, 16));
        page.add(header("Issue Ticket", "Select route, seat and passenger information"), BorderLayout.NORTH);
        try {
            TicketDataStore.refreshRoutes();
            TicketDataStore.refreshPassengers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }

        JPanel form = AppStyle.surfacePanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<PassengerRecord> cbPassenger = new JComboBox<>(TicketDataStore.passengers.toArray(new PassengerRecord[0]));
        JComboBox<RouteRecord> cbRoute = new JComboBox<>(TicketDataStore.routes.toArray(new RouteRecord[0]));
        JTextField txtDate = new JTextField(LocalDate.now().toString(), 18);
        JComboBox<String> cbSeat = new JComboBox<>();
        JTextField txtPrice = new JTextField(18);
        txtPrice.setEditable(false);

        AppStyle.styleField(cbPassenger);
        AppStyle.styleField(cbRoute);
        AppStyle.styleField(txtDate);
        AppStyle.styleField(cbSeat);
        AppStyle.styleField(txtPrice);

        addRow(form, gbc, 0, "Passenger", cbPassenger);
        addRow(form, gbc, 1, "Route", cbRoute);
        addRow(form, gbc, 2, "Date", txtDate);
        addRow(form, gbc, 3, "Seat No", cbSeat);
        addRow(form, gbc, 4, "Total Price", txtPrice);

        Runnable updateRouteDetails = () -> {
            RouteRecord selected = (RouteRecord) cbRoute.getSelectedItem();
            cbSeat.removeAllItems();
            if (selected != null) {
                int limit = Math.min(selected.availableSeats, 20);
                for (int i = 1; i <= limit; i++) {
                    cbSeat.addItem(String.valueOf(i));
                }
                txtPrice.setText(String.format("BDT %.2f", selected.pricePerSeat));
            }
        };
        cbRoute.addActionListener(e -> updateRouteDetails.run());
        updateRouteDetails.run();

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setBackground(Color.WHITE);
        JButton btnIssue = AppStyle.primaryButton("Issue Ticket");
        JButton btnCancel = AppStyle.secondaryButton("Cancel");
        buttons.add(btnCancel);
        buttons.add(btnIssue);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(16, 8, 0, 8);
        form.add(buttons, gbc);

        page.add(form, BorderLayout.CENTER);
        add(page, BorderLayout.CENTER);

        btnCancel.addActionListener(e -> dispose());
        btnIssue.addActionListener(e -> {
            PassengerRecord passenger = (PassengerRecord) cbPassenger.getSelectedItem();
            RouteRecord selected = (RouteRecord) cbRoute.getSelectedItem();
            if (passenger == null || selected == null || cbSeat.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Passenger, route and seat are required.");
                return;
            }
            if (selected.availableSeats <= 0) {
                JOptionPane.showMessageDialog(this, "No seats available for this route.");
                return;
            }
            try {
                TicketDataStore.issueTicket(new TicketRecord(
                        TicketDataStore.nextTicketNo(),
                        passenger.name,
                        selected,
                        txtDate.getText().trim(),
                        String.valueOf(cbSeat.getSelectedItem()),
                        "Confirmed"
                ));
                if (onSaved != null) {
                    onSaved.run();
                }
                JOptionPane.showMessageDialog(this,
                        "Ticket issued successfully!\n\nRoute: " + selected.routeId + " - " + selected.from + " to " + selected.to
                                + "\nPassenger: " + passenger.name,
                        "Message",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });
    }

    private JPanel header(String title, String subtitle) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(AppStyle.BACKGROUND);
        panel.add(AppStyle.titleLabel(title, 20));
        JLabel sub = new JLabel(subtitle);
        sub.setForeground(AppStyle.MUTED);
        panel.add(sub);
        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridwidth = 1;
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(130, 30));
        lbl.setForeground(AppStyle.TEXT);
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }
}
