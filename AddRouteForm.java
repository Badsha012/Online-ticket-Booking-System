import javax.swing.*;
import java.awt.*;

public class AddRouteForm extends JFrame {
    public AddRouteForm() {
        this(null);
    }

    public AddRouteForm(Runnable onSaved) {
        AppStyle.applyLookAndFeel();
        setTitle("Add Route");
        setSize(520, 500);
        setMinimumSize(new Dimension(500, 480));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel page = AppStyle.pagePanel(new BorderLayout(0, 16));
        page.add(header("Add Route", "Create a new route and vehicle capacity"), BorderLayout.NORTH);

        JPanel form = AppStyle.surfacePanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtRouteId = new JTextField(18);
        JTextField txtFrom = new JTextField(18);
        JTextField txtTo = new JTextField(18);
        JComboBox<String> cbVehicle = new JComboBox<>(new String[]{"Bus", "Train", "Minibus"});
        JTextField txtSeats = new JTextField(18);
        JTextField txtPrice = new JTextField(18);

        AppStyle.styleField(txtRouteId);
        AppStyle.styleField(txtFrom);
        AppStyle.styleField(txtTo);
        AppStyle.styleField(cbVehicle);
        AppStyle.styleField(txtSeats);
        AppStyle.styleField(txtPrice);

        addRow(form, gbc, 0, "Route ID", txtRouteId);
        addRow(form, gbc, 1, "From", txtFrom);
        addRow(form, gbc, 2, "To", txtTo);
        addRow(form, gbc, 3, "Vehicle Type", cbVehicle);
        addRow(form, gbc, 4, "Total Seats", txtSeats);
        addRow(form, gbc, 5, "Price per Seat", txtPrice);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setBackground(Color.WHITE);
        JButton btnSave = AppStyle.primaryButton("Save Route");
        JButton btnCancel = AppStyle.secondaryButton("Cancel");
        buttons.add(btnCancel);
        buttons.add(btnSave);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(16, 8, 0, 8);
        form.add(buttons, gbc);

        page.add(form, BorderLayout.CENTER);
        add(page, BorderLayout.CENTER);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> {
            try {
                if (txtRouteId.getText().trim().isEmpty() || txtFrom.getText().trim().isEmpty() || txtTo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Route ID, From and To are required.");
                    return;
                }
                int seats = Integer.parseInt(txtSeats.getText().trim());
                double price = Double.parseDouble(txtPrice.getText().trim());
                TicketDataStore.routes.add(new RouteRecord(
                        txtRouteId.getText().trim(),
                        txtFrom.getText().trim(),
                        txtTo.getText().trim(),
                        String.valueOf(cbVehicle.getSelectedItem()),
                        seats,
                        price
                ));
                if (onSaved != null) {
                    onSaved.run();
                }
                JOptionPane.showMessageDialog(this, "Route saved successfully!");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Seats and price must be valid numbers.");
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
