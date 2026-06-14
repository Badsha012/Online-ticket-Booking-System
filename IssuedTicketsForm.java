import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class IssuedTicketsForm extends JFrame {
    private final DefaultTableModel model;

    public IssuedTicketsForm() {
        this(null);
    }

    public IssuedTicketsForm(Runnable onChanged) {
        AppStyle.applyLookAndFeel();
        setTitle("Issued Tickets");
        setSize(780, 430);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel page = AppStyle.pagePanel(new BorderLayout(0, 16));
        page.add(header("Issued Tickets", "Review bookings and cancel tickets when needed"), BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Ticket No.", "Passenger", "Route", "Date", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        AppStyle.styleTable(table);
        table.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());

        JPanel content = AppStyle.surfacePanel(new BorderLayout(0, 14));
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setBackground(Color.WHITE);
        JButton btnRefresh = AppStyle.secondaryButton("Refresh");
        JButton btnCancel = AppStyle.dangerButton("Cancel Ticket");
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnCancel);
        content.add(bottomPanel, BorderLayout.SOUTH);

        page.add(content, BorderLayout.CENTER);
        add(page, BorderLayout.CENTER);
        loadTickets();

        btnCancel.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a row first.");
                return;
            }

            int modelRow = table.convertRowIndexToModel(selectedRow);
            TicketRecord ticket = TicketDataStore.tickets.get(modelRow);
            if ("Cancelled".equals(ticket.status)) {
                JOptionPane.showMessageDialog(this, "This ticket is already cancelled.");
                return;
            }

            try {
                TicketDataStore.cancelTicket(ticket);
                loadTickets();
                if (onChanged != null) {
                    onChanged.run();
                }
                JOptionPane.showMessageDialog(this, "Ticket marked as Cancelled!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        btnRefresh.addActionListener(e -> loadTickets());
    }

    private void loadTickets() {
        try {
            TicketDataStore.refreshTickets();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
        model.setRowCount(0);
        int id = 1;
        for (TicketRecord ticket : TicketDataStore.tickets) {
            model.addRow(new Object[]{
                    id++,
                    ticket.ticketNo,
                    ticket.passengerName,
                    ticket.route.from + " to " + ticket.route.to,
                    ticket.date,
                    ticket.status
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
}
