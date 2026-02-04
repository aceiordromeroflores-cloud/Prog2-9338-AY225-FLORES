import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AttendanceTracker extends JFrame {
    
    private JTextField nameField, courseField, timeInField;
    private SignaturePanel sigPanel;
    private JButton submitButton, clearButton;
    private JTextArea historyArea; 
    private Color maroon = new Color(122, 12, 12);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AttendanceTracker() {
        setTitle("Attendance Tracker (Fixed Layout)");
        setSize(900, 700); // Larger default size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Layout Container
        JPanel container = new JPanel(new BorderLayout(15, 0));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- LEFT SIDE: FORM ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        
        // Input Groups
        leftPanel.add(createLabelField("Attendance Name:", nameField = new JTextField()));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(createLabelField("Course / Year:", courseField = new JTextField()));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(createLabelField("Time In:", timeInField = new JTextField()));
        timeInField.setEditable(false);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Signature Area
        JLabel sigLabel = new JLabel("E-Signature (Draw below):");
        sigLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(sigLabel);
        
        sigPanel = new SignaturePanel();
        sigPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(sigPanel);
        
        clearButton = new JButton("Clear Signature");
        clearButton.addActionListener(e -> sigPanel.clear());
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(clearButton);

        // Spacer to push button to bottom
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Submit Button
        submitButton = new JButton("SUBMIT ATTENDANCE");
        submitButton.setBackground(maroon);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(350, 60));
        submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        submitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitButton.addActionListener(e -> handleSubmit());
        leftPanel.add(submitButton);

        // --- RIGHT SIDE: HISTORY ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Login History"));
        rightPanel.setPreferredSize(new Dimension(300, 0)); // Fixed width for sidebar

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(historyArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Assemble
        container.add(leftPanel, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);
        add(container);

        updateTime();
    }

    // Helper to keep labels and fields together
    private JPanel createLabelField(String labelText, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        p.add(new JLabel(labelText), BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private void handleSubmit() {
        if (nameField.getText().isEmpty() || courseField.getText().isEmpty() || sigPanel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields and signature are required!");
            return;
        }

        String logEntry = String.format("[%s] %s (%s)\n", 
                          timeInField.getText(), nameField.getText(), courseField.getText());
        historyArea.append(logEntry);
        
        // Success and Reset
        JOptionPane.showMessageDialog(this, "Success!");
        nameField.setText("");
        courseField.setText("");
        sigPanel.clear();
        updateTime();
    }

    private void updateTime() {
        timeInField.setText(LocalDateTime.now().format(formatter));
    }

    // --- DRAWING PANEL ---
    class SignaturePanel extends JPanel {
        private final List<List<Point>> paths = new ArrayList<>();
        private List<Point> currentPath;

        public SignaturePanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            setPreferredSize(new Dimension(350, 150));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    currentPath = new ArrayList<>();
                    currentPath.add(e.getPoint());
                    paths.add(currentPath);
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    currentPath.add(e.getPoint());
                    repaint();
                }
            });
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(2));
            for (List<Point> path : paths) {
                for (int i = 0; i < path.size() - 1; i++) {
                    g2d.drawLine(path.get(i).x, path.get(i).y, path.get(i+1).x, path.get(i+1).y);
                }
            }
        }
        public void clear() { paths.clear(); repaint(); }
        public boolean isEmpty() { return paths.isEmpty(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendanceTracker().setVisible(true));
    }
}