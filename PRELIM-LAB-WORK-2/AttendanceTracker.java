import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AttendanceTracker extends JFrame {
    
    private JTextField nameField, timeDisplayField;
    private JComboBox<String> courseDropdown;
    private JRadioButton timeInRadio, timeOutRadio;
    private SignaturePanel sigPanel;
    private JButton submitButton, clearButton;
    private JTextArea historyArea; 
    private Color maroon = new Color(122, 12, 12);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AttendanceTracker() {
        setTitle("CS Department Attendance Tracker");
        setSize(950, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel container = new JPanel(new BorderLayout(20, 0));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- LEFT SIDE: FORM ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        
        // Consistent spacing variable
        int verticalGap = 15;

        // 1. Name Field
        leftPanel.add(createLabelField("Student Name:", nameField = new JTextField()));
        leftPanel.add(Box.createRigidArea(new Dimension(0, verticalGap)));

        // 2. Course Dropdown
        JPanel courseGroup = new JPanel(new BorderLayout(0, 5));
        courseGroup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        courseGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
        courseGroup.add(new JLabel("Select Course:"), BorderLayout.NORTH);
        String[] csCourses = {"-- Select Course --", "BS Computer Science", "BS Information Technology", "BS Information Systems", "BS Computer Engineering", "Associate in Computer Tech"};
        courseDropdown = new JComboBox<>(csCourses);
        courseGroup.add(courseDropdown, BorderLayout.CENTER);
        leftPanel.add(courseGroup);
        
        leftPanel.add(Box.createRigidArea(new Dimension(0, verticalGap)));

        // 3. Action Type (Time In/Out)
        JPanel actionGroup = new JPanel(new BorderLayout(0, 5));
        actionGroup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        actionGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionGroup.add(new JLabel("Action Type:"), BorderLayout.NORTH);
        
        timeInRadio = new JRadioButton("Time In", true);
        timeOutRadio = new JRadioButton("Time Out");
        ButtonGroup actionGroupBtn = new ButtonGroup();
        actionGroupBtn.add(timeInRadio);
        actionGroupBtn.add(timeOutRadio);
        
        JPanel radioWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioWrapper.add(timeInRadio);
        radioWrapper.add(Box.createRigidArea(new Dimension(20, 0)));
        radioWrapper.add(timeOutRadio);
        actionGroup.add(radioWrapper, BorderLayout.CENTER);
        leftPanel.add(actionGroup);

        leftPanel.add(Box.createRigidArea(new Dimension(0, verticalGap)));

        // 4. Time Display
        leftPanel.add(createLabelField("Current Timestamp:", timeDisplayField = new JTextField()));
        timeDisplayField.setEditable(false);
        
        leftPanel.add(Box.createRigidArea(new Dimension(0, verticalGap)));

        // 5. Signature Area
        JLabel sigLabel = new JLabel("E-Signature:");
        sigLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(sigLabel);
        
        sigPanel = new SignaturePanel();
        sigPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(sigPanel);
        
        clearButton = new JButton("Clear Signature");
        clearButton.addActionListener(e -> sigPanel.clear());
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(clearButton);

        // This pushes the submit button to the bottom while keeping upper fields tight
        leftPanel.add(Box.createVerticalGlue()); 

        // 6. Submit Button
        submitButton = new JButton("SUBMIT RECORD");
        submitButton.setBackground(maroon);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        submitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitButton.addActionListener(e -> handleSubmit());
        leftPanel.add(submitButton);

        // --- RIGHT SIDE: HISTORY ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Attendance Log"));
        rightPanel.setPreferredSize(new Dimension(350, 0));

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(historyArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        container.add(leftPanel, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);
        add(container);

        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
        updateTime();
    }

    private JPanel createLabelField(String labelText, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        p.add(new JLabel(labelText), BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private void handleSubmit() {
        String name = nameField.getText().trim();
        String course = (String) courseDropdown.getSelectedItem();
        String type = timeInRadio.isSelected() ? "TIME-IN" : "TIME-OUT";
        if (name.isEmpty() || course.equals("-- Select Course --") || sigPanel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all fields and signature.");
            return;
        }
        historyArea.append(String.format("[%s] %s\nStudent: %s\nCourse: %s\n---\n", type, timeDisplayField.getText(), name, course));
        JOptionPane.showMessageDialog(this, type + " Successful!");
        nameField.setText("");
        courseDropdown.setSelectedIndex(0);
        sigPanel.clear();
    }

    private void updateTime() { timeDisplayField.setText(LocalDateTime.now().format(formatter)); }

    class SignaturePanel extends JPanel {
        private final List<List<Point>> paths = new ArrayList<>();
        private List<Point> currentPath;
        public SignaturePanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
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