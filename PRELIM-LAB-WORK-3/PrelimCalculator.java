import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrelimCalculator extends JFrame {
    // UI Components
    private JTextField txtAttendance, txtLab1, txtLab2, txtLab3;
    private JLabel lblLabAvg, lblStanding, lblReqPass, lblReqExcel, lblRemarks;
    private Color maroon = new Color(122, 12, 12);
    private Color gold = new Color(212, 175, 55);

    public PrelimCalculator() {
        // Window Settings
        setTitle("Perpetual Help Grade Calculator");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(maroon);
        JLabel title = new JLabel("PRELIM CALCULATOR");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Input Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(12, 1, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        mainPanel.add(new JLabel("Attendance Score:"));
        txtAttendance = new JTextField();
        mainPanel.add(txtAttendance);

        mainPanel.add(new JLabel("Lab Work 1:"));
        txtLab1 = new JTextField();
        mainPanel.add(txtLab1);

        mainPanel.add(new JLabel("Lab Work 2:"));
        txtLab2 = new JTextField();
        mainPanel.add(txtLab2);

        mainPanel.add(new JLabel("Lab Work 3:"));
        txtLab3 = new JTextField();
        mainPanel.add(txtLab3);

        JButton btnCompute = new JButton("COMPUTE STANDING");
        btnCompute.setBackground(maroon);
        btnCompute.setForeground(Color.WHITE);
        btnCompute.setFocusPainted(false);
        mainPanel.add(btnCompute);

        // Results Section
        lblLabAvg = new JLabel("Lab Work Average: --");
        lblStanding = new JLabel("Class Standing: --");
        lblReqPass = new JLabel("Req. Exam to Pass (75): --");
        lblReqExcel = new JLabel("Req. Exam for Excellent (100): --");
        lblRemarks = new JLabel("Remarks: --");
        lblRemarks.setFont(new Font("Arial", Font.BOLD, 12));

        mainPanel.add(lblLabAvg);
        mainPanel.add(lblStanding);
        mainPanel.add(lblReqPass);
        mainPanel.add(lblReqExcel);
        mainPanel.add(lblRemarks);

        add(mainPanel, BorderLayout.CENTER);

        // Calculation Logic
        btnCompute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double att = Double.parseDouble(txtAttendance.getText());
                    double l1 = Double.parseDouble(txtLab1.getText());
                    double l2 = Double.parseDouble(txtLab2.getText());
                    double l3 = Double.parseDouble(txtLab3.getText());

                    // Formulas
                    double labAvg = (l1 + l2 + l3) / 3;
                    double standing = (att * 0.40) + (labAvg * 0.60);
                    
                    // Required scores
                    double reqPass = (75 - (standing * 0.70)) / 0.30;
                    double reqExcel = (100 - (standing * 0.70)) / 0.30;

                    // Update Labels
                    lblLabAvg.setText(String.format("Lab Work Average: %.2f", labAvg));
                    lblStanding.setText(String.format("Class Standing: %.2f", standing));
                    lblReqPass.setText(String.format("Req. Exam to Pass (75): %.2f", reqPass));
                    lblReqExcel.setText(String.format("Req. Exam for Excellent (100): %.2f", reqExcel));

                    if (standing >= 75) {
                        lblRemarks.setText("Remarks: Passing Class Standing");
                        lblRemarks.setForeground(new Color(0, 128, 0));
                    } else {
                        lblRemarks.setText("Remarks: Below Passing Standing");
                        lblRemarks.setForeground(maroon);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numeric values.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PrelimCalculator().setVisible(true);
        });
    }
}