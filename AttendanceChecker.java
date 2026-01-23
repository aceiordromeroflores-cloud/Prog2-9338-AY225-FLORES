import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AttendanceSheet {

    // Perpetual colors
    static final Color MAROON = new Color(128, 0, 0);
    static final Color GOLD = new Color(255, 215, 0);

    public static void main(String[] args) {

        JFrame frame = new JFrame("Attendance Sheet");
        frame.setSize(950, 380);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(GOLD);

        JPanel panel = new JPanel(new GridLayout(6, 7, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(GOLD);

        // Headers
        String[] headers = {
                "Name (Time In)", "Course / Year", "Time In",
                "Name (Time Out)", "Time Out", "Signature", "Clear"
        };

        for (String h : headers) {
            JLabel label = new JLabel(h, SwingConstants.CENTER);
            label.setForeground(MAROON);
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            panel.add(label);
        }

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");

        for (int i = 0; i < 5; i++) {

            JTextField nameInField = styledField();
            JTextField courseField = styledField();
            JTextField timeInField = styledField();
            JTextField nameOutField = styledField();
            JTextField timeOutField = styledField();

            timeInField.setEditable(false);
            timeOutField.setEditable(false);

            SignaturePanel signaturePanel = new SignaturePanel();
            JButton clearBtn = new JButton("Clear");
            clearBtn.setBackground(MAROON);
            clearBtn.setForeground(GOLD);
            clearBtn.setFocusPainted(false);

            DocumentListener timeInListener = new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { setTimeIn(); }
                public void removeUpdate(DocumentEvent e) {}
                public void changedUpdate(DocumentEvent e) {}

                private void setTimeIn() {
                    if (!nameInField.getText().isEmpty()
                            && !courseField.getText().isEmpty()
                            && timeInField.getText().isEmpty()) {
                        timeInField.setText(LocalTime.now().format(timeFormat));
                    }
                }
            };

            DocumentListener timeOutListener = new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { setTimeOut(); }
                public void removeUpdate(DocumentEvent e) {}
                public void changedUpdate(DocumentEvent e) {}

                private void setTimeOut() {
                    if (!nameOutField.getText().isEmpty()
                            && timeOutField.getText().isEmpty()) {
                        timeOutField.setText(LocalTime.now().format(timeFormat));
                    }
                }
            };

            clearBtn.addActionListener(e -> signaturePanel.clear());

            nameInField.getDocument().addDocumentListener(timeInListener);
            courseField.getDocument().addDocumentListener(timeInListener);
            nameOutField.getDocument().addDocumentListener(timeOutListener);

            panel.add(nameInField);
            panel.add(courseField);
            panel.add(timeInField);
            panel.add(nameOutField);
            panel.add(timeOutField);
            panel.add(signaturePanel);
            panel.add(clearBtn);
        }

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JTextField styledField() {
        JTextField field = new JTextField();
        field.setBackground(Color.WHITE);
        field.setForeground(MAROON);
        field.setCaretColor(MAROON);
        field.setBorder(BorderFactory.createLineBorder(MAROON));
        return field;
    }
}

// Signature panel
class SignaturePanel extends JPanel {

    private Image image;
    private Graphics2D g2;
    private int x, y;

    public SignaturePanel() {
        setPreferredSize(new Dimension(120, 40));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(128, 0, 0)));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (g2 != null) {
                    g2.drawLine(x, y, e.getX(), e.getY());
                    repaint();
                    x = e.getX();
                    y = e.getY();
                }
            }
        });
    }

    public void clear() {
        if (g2 != null) {
            g2.clearRect(0, 0, getWidth(), getHeight());
            repaint();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            image = createImage(getWidth(), getHeight());
            g2 = (Graphics2D) image.getGraphics();
            g2.setStroke(new BasicStroke(2));
            g2.setColor(new Color(128, 0, 0));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.drawImage(image, 0, 0, null);
    }
}