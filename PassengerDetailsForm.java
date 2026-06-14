import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PassengerDetailsForm extends JFrame {
    private final DefaultTableModel model;
    private final Runnable onChanged;

    public PassengerDetailsForm() {
        this(null);
    }

    public PassengerDetailsForm(Runnable onChanged) {
        this.onChanged = onChanged;
        AppStyle.applyLookAndFeel();
        setTitle("Passenger Details");
        setSize(760, 430);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel page = AppStyle.pagePanel(new BorderLayout(0, 16));
        page.add(header("Passenger Details", "View registered passenger information"), BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Contact", "Email", "Address", "Action"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        JTable table = new JTable(model);
        AppStyle.styleTable(table);
        table.setRowHeight(36);
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new RemoveButtonEditor(table));
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setMaxWidth(110);

        JPanel content = AppStyle.surfacePanel(new BorderLayout(0, 14));
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setBackground(Color.WHITE);
        JButton btnRefresh = AppStyle.secondaryButton("Refresh");
        JButton btnDelete = AppStyle.dangerButton("Remove");
        JButton btnClose = AppStyle.primaryButton("Close");
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnClose);
        content.add(bottomPanel, BorderLayout.SOUTH);

        page.add(content, BorderLayout.CENTER);
        add(page, BorderLayout.CENTER);
        loadPassengers();

        btnRefresh.addActionListener(e -> loadPassengers());
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a passenger first.");
                return;
            }

            int modelRow = table.convertRowIndexToModel(selectedRow);
            PassengerRecord passenger = TicketDataStore.passengers.get(modelRow);
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Remove passenger: " + passenger.name + "?",
                    "Confirm Remove",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            try {
                TicketDataStore.deletePassenger(passenger);
                loadPassengers();
                notifyChanged();
                JOptionPane.showMessageDialog(this, "Passenger removed successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });
        btnClose.addActionListener(e -> dispose());
    }

    private void loadPassengers() {
        try {
            TicketDataStore.refreshPassengers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }

        model.setRowCount(0);
        int id = 1;
        for (PassengerRecord passenger : TicketDataStore.passengers) {
            model.addRow(new Object[]{
                    id++,
                    passenger.name,
                    passenger.contact,
                    passenger.email,
                    passenger.address,
                    "Remove"
            });
        }
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

    private void notifyChanged() {
        if (onChanged != null) {
            onChanged.run();
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        ButtonRenderer() {
            setText("Remove");
            setFocusPainted(false);
            setOpaque(true);
            setContentAreaFilled(true);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setForeground(Color.WHITE);
            setBackground(AppStyle.DANGER);
            return this;
        }
    }

    private class RemoveButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private final JTable table;
        private final JButton button = new JButton("Remove");
        private PassengerRecord passenger;

        RemoveButtonEditor(JTable table) {
            this.table = table;
            button.setFocusPainted(false);
            button.setForeground(Color.WHITE);
            button.setBackground(AppStyle.DANGER);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.addActionListener(this);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            int modelRow = table.convertRowIndexToModel(row);
            passenger = TicketDataStore.passengers.get(modelRow);
            return button;
        }

        public Object getCellEditorValue() {
            return "Remove";
        }

        public void actionPerformed(ActionEvent e) {
            if (passenger == null) {
                return;
            }
            String passengerName = passenger.name;
            int choice = JOptionPane.showConfirmDialog(
                    PassengerDetailsForm.this,
                    "Remove passenger: " + passengerName + "?",
                    "Confirm Remove",
                    JOptionPane.YES_NO_OPTION
            );
            fireEditingStopped();
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            try {
                TicketDataStore.deletePassenger(passenger);
                loadPassengers();
                notifyChanged();
                JOptionPane.showMessageDialog(PassengerDetailsForm.this, "Passenger removed successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PassengerDetailsForm.this, "Database error: " + ex.getMessage());
            }
        }
    }
}
