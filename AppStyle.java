import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AppStyle {
    public static final Color NAVY = new Color(18, 43, 86);
    public static final Color NAVY_HOVER = new Color(32, 72, 136);
    public static final Color BACKGROUND = new Color(245, 247, 251);
    public static final Color SURFACE = Color.WHITE;
    public static final Color BORDER = new Color(221, 226, 235);
    public static final Color TEXT = new Color(31, 41, 55);
    public static final Color MUTED = new Color(107, 114, 128);
    public static final Color SUCCESS = new Color(22, 128, 83);
    public static final Color WARNING = new Color(180, 83, 9);
    public static final Color DANGER = new Color(185, 28, 28);

    public static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 13));
    }

    public static JPanel pagePanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    public static JPanel surfacePanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return panel;
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, NAVY, NAVY_HOVER, Color.WHITE);
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, new Color(226, 232, 240), new Color(203, 213, 225), TEXT);
        return button;
    }

    private static void styleButton(JButton button, Color background, Color hoverBackground, Color foreground) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background.darker()),
                new EmptyBorder(9, 18, 9, 18)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverBackground);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(hoverBackground.darker()),
                        new EmptyBorder(9, 18, 9, 18)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(background);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(background.darker()),
                        new EmptyBorder(9, 18, 9, 18)
                ));
            }
        });
    }

    public static void styleField(JComponent field) {
        field.setPreferredSize(new Dimension(240, 34));
        field.setMinimumSize(new Dimension(220, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 213, 224)),
                new EmptyBorder(5, 8, 5, 8)
        ));
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT);
        table.setForeground(TEXT);
        table.setAutoCreateRowSorter(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(236, 241, 248));
        header.setForeground(TEXT);
        header.setBorder(BorderFactory.createLineBorder(BORDER));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 34));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(new EmptyBorder(0, 10, 0, 10));
        table.setDefaultRenderer(Object.class, renderer);
    }

    public static JLabel titleLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, size));
        label.setForeground(TEXT);
        return label;
    }
}
