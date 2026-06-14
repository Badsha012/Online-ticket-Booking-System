import javax.swing.*;
import java.awt.*;

public class AddPassengerForm extends JFrame {
    public AddPassengerForm() {
        this(null);
    }

    public AddPassengerForm(Runnable onSaved) {
        AppStyle.applyLookAndFeel();
        setTitle("Add Passenger");
        setSize(500, 430);
        setMinimumSize(new Dimension(480, 400));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel page = AppStyle.pagePanel(new BorderLayout(0, 16));
        page.add(header("Add Passenger", "Register passenger contact information"), BorderLayout.NORTH);

        JPanel form = AppStyle.surfacePanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtName = new JTextField(18);
        JTextField txtContact = new JTextField(18);
        JTextField txtEmail = new JTextField(18);
        JTextField txtAddress = new JTextField(18);

        AppStyle.styleField(txtName);
        AppStyle.styleField(txtContact);
        AppStyle.styleField(txtEmail);
        AppStyle.styleField(txtAddress);

        addRow(form, gbc, 0, "Passenger Name", txtName);
        addRow(form, gbc, 1, "Contact Number", txtContact);
        addRow(form, gbc, 2, "Email", txtEmail);
        addRow(form, gbc, 3, "Address", txtAddress);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setBackground(Color.WHITE);
        JButton btnSave = AppStyle.primaryButton("Save Passenger");
        JButton btnCancel = AppStyle.secondaryButton("Cancel");
        buttons.add(btnCancel);
        buttons.add(btnSave);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(16, 8, 0, 8);
        form.add(buttons, gbc);

        page.add(form, BorderLayout.CENTER);
        add(page, BorderLayout.CENTER);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> {
            if (txtName.getText().trim().isEmpty() || txtContact.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and contact number are required.");
                return;
            }
            try {
                TicketDataStore.addPassenger(new PassengerRecord(
                        txtName.getText().trim(),
                        txtContact.getText().trim(),
                        txtEmail.getText().trim(),
                        txtAddress.getText().trim()
                ));
                if (onSaved != null) {
                    onSaved.run();
                }
                JOptionPane.showMessageDialog(this, "Passenger details registered!");
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
