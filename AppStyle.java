import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class AppStyle {
    // Professional blue color scheme
    public static final Color NAVY = new Color(11, 37, 69);
    public static final Color NAVY_HOVER = new Color(20, 54, 96);
    public static final Color BACKGROUND = new Color(248, 250, 252);
    public static final Color SURFACE = Color.WHITE;
    public static final Color BORDER = new Color(219, 224, 231);
    public static final Color TEXT = new Color(15, 23, 35);
    public static final Color MUTED = new Color(107, 119, 140);
    public static final Color ACCENT = new Color(25, 103, 210);          // Professional blue
    public static final Color ACCENT_HOVER = new Color(37, 99, 235);     // Lighter blue on hover
    public static final Color SUCCESS = new Color(34, 197, 94);          // Professional green
    public static final Color SUCCESS_HOVER = new Color(22, 163, 74);
    public static final Color WARNING = new Color(217, 119, 6);          // Professional orange
    public static final Color WARNING_HOVER = new Color(180, 83, 9);
    public static final Color DANGER = new Color(239, 68, 68);           // Professional red
    public static final Color DANGER_HOVER = new Color(220, 38, 38);
    public static final Color INFO = new Color(59, 130, 246);            // Info blue
    public static final Color INFO_HOVER = new Color(37, 99, 235);

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
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 12));
    }

    public static JPanel pagePanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(22, 22, 22, 22));
        return panel;
    }

    public static JPanel surfacePanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 18, 18, 18)
        ));
        return panel;
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, ACCENT, ACCENT_HOVER, Color.BLACK);
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        styleSecondaryButton(button);
        return button;
    }

    public static JButton successButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, SUCCESS, SUCCESS_HOVER, Color.BLACK);
        return button;
    }

    public static JButton warningButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, WARNING, WARNING_HOVER, Color.BLACK);
        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, DANGER, DANGER_HOVER, Color.BLACK);
        return button;
    }

    private static void styleButton(JButton button, Color background, Color hoverBackground, Color foreground) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background, 1),
                new EmptyBorder(10, 18, 10, 18)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverBackground);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(hoverBackground, 1),
                        new EmptyBorder(10, 18, 10, 18)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(background);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(background, 1),
                        new EmptyBorder(10, 18, 10, 18)
                ));
            }
        });
    }

    private static void styleSecondaryButton(JButton button) {
        Color background = new Color(237, 242, 247);
        Color hoverBackground = new Color(226, 235, 245);
        Color borderColor = new Color(203, 213, 225);
        Color foreground = Color.BLACK;
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                new EmptyBorder(10, 18, 10, 18)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverBackground);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(165, 180, 200), 1),
                        new EmptyBorder(10, 18, 10, 18)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(background);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, 1),
                        new EmptyBorder(10, 18, 10, 18)
                ));
            }
        });
    }

    public static void styleField(JComponent field) {
        field.setPreferredSize(new Dimension(260, 38));
        field.setMinimumSize(new Dimension(230, 38));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 219, 232), 1),
                new EmptyBorder(7, 12, 7, 12)
        ));
        
        // Add focus listener for better visual feedback
        if (field instanceof JTextField) {
            ((JTextField) field).addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    field.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(ACCENT, 2),
                            new EmptyBorder(7, 12, 7, 12)
                    ));
                }
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    field.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(210, 219, 232), 1),
                            new EmptyBorder(7, 12, 7, 12)
                    ));
                }
            });
        }
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT);
        table.setForeground(TEXT);
        table.setBackground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setAutoCreateRowSorter(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(240, 244, 250));
        header.setForeground(TEXT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(new EmptyBorder(0, 12, 0, 12));
        renderer.setForeground(TEXT);
        renderer.setBackground(Color.WHITE);
        table.setDefaultRenderer(Object.class, renderer);
    }

    public static JLabel titleLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, size));
        label.setForeground(TEXT);
        return label;
    }

    public static JLabel accentLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, size));
        label.setForeground(ACCENT);
        return label;
    }

    public static JLabel mutedLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, size));
        label.setForeground(MUTED);
        return label;
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setPreferredSize(new Dimension(260, 38));
        comboBox.setMinimumSize(new Dimension(230, 38));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT);
    }
}
