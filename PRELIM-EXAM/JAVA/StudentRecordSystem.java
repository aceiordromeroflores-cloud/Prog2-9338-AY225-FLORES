import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class StudentRecordSystem extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField idField, nameField, searchField;
    // Specific Grade Fields
    private JTextField lab1Field, lab2Field, lab3Field, prelimField, attendanceField;
    private JComboBox<String> filterCombo;
    private JButton addButton, deleteButton;

    private final Color primaryBlue = new Color(26, 58, 95);
    private final Color lightBg = new Color(238, 242, 247);

    public StudentRecordSystem() {
        setTitle("Flores, John Aceiord R. (25-0473-342)");
        setSize(1100, 700); // Increased width for more columns
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(lightBg);
        setLayout(new BorderLayout(15, 15));

        // --- 1. Header ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryBlue);
        JLabel titleLabel = new JLabel("STUDENT GRADE MANAGEMENT SYSTEM");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Table & Sorter (Expanded Columns) ---
        String[] columns = {"ID", "Full Name", "Lab 1", "Lab 2", "Lab 3", "Prelim", "Attendance", "Final Grade"};
        model = new DefaultTableModel(columns, 0);
        sorter = new TableRowSorter<>(model);
        table = new JTable(model);
        table.setRowSorter(sorter);
        setupTableUI();
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. Utilities Panel ---
        JPanel utilPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        utilPanel.setOpaque(false);
        searchField = new JTextField(15);
        searchField.setBorder(BorderFactory.createTitledBorder("Search Name/ID"));
        searchField.addCaretListener(e -> applyFilters());

        String[] filterOptions = {"All Grades", "Passing (>= 75)", "Failing (< 75)", "Excellence (>= 90)"};
        filterCombo = new JComboBox<>(filterOptions);
        filterCombo.addActionListener(e -> applyFilters());

        utilPanel.add(searchField);
        utilPanel.add(new JLabel("Grade Filter:"));
        utilPanel.add(filterCombo);

        // --- 4. Input Panel (Two-Row Grid for many inputs) ---
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.add(utilPanel, BorderLayout.NORTH);
        bottomContainer.add(createInputArea(), BorderLayout.CENTER);
        
        add(bottomContainer, BorderLayout.SOUTH);

        loadCSV("MOCK_DATA.csv");
    }

    private JPanel createInputArea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Grid for inputs (2 rows to accommodate many fields)
        JPanel inputGrid = new JPanel(new GridLayout(2, 4, 10, 10));
        inputGrid.setOpaque(false);
        inputGrid.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        idField = createStyledField("ID");
        nameField = createStyledField("Full Name");
        lab1Field = createStyledField("Lab 1");
        lab2Field = createStyledField("Lab 2");
        lab3Field = createStyledField("Lab 3");
        prelimField = createStyledField("Prelim Exam");
        attendanceField = createStyledField("Attendance");

        inputGrid.add(idField);
        inputGrid.add(nameField);
        inputGrid.add(lab1Field);
        inputGrid.add(lab2Field);
        inputGrid.add(lab3Field);
        inputGrid.add(prelimField);
        inputGrid.add(attendanceField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);
        addButton = createStyledButton("ADD RECORD", new Color(40, 167, 69));
        deleteButton = createStyledButton("DELETE", new Color(220, 53, 69));
        
        addButton.addActionListener(e -> addRecord());
        deleteButton.addActionListener(e -> deleteRecord());

        btnPanel.add(addButton);
        btnPanel.add(deleteButton);

        panel.add(inputGrid, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void addRecord() {
        try {
            double l1 = Double.parseDouble(lab1Field.getText());
            double l2 = Double.parseDouble(lab2Field.getText());
            double l3 = Double.parseDouble(lab3Field.getText());
            double pre = Double.parseDouble(prelimField.getText());
            double att = Double.parseDouble(attendanceField.getText());

            // Simple average calculation
            double finalGrade = (l1 + l2 + l3 + pre + att) / 5.0;

            model.addRow(new Object[]{
                idField.getText(), nameField.getText(), 
                l1, l2, l3, pre, att, String.format("%.2f", finalGrade)
            });
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for all grades.");
        }
    }

    private void clearFields() {
        idField.setText(""); nameField.setText("");
        lab1Field.setText(""); lab2Field.setText(""); lab3Field.setText("");
        prelimField.setText(""); attendanceField.setText("");
    }

    private void applyFilters() {
        String text = searchField.getText();
        String gradeOption = (String) filterCombo.getSelectedItem();
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        
        if (text.trim().length() > 0) filters.add(RowFilter.regexFilter("(?i)" + text));

        filters.add(new RowFilter<Object, Object>() {
            public boolean include(Entry<?, ?> entry) {
                try {
                    // Index 7 is the Final Grade column
                    double grade = Double.parseDouble(entry.getStringValue(7).toString());
                    if (gradeOption.equals("Passing (>= 75)")) return grade >= 75;
                    if (gradeOption.equals("Failing (< 75)")) return grade < 75;
                    if (gradeOption.equals("Excellence (>= 90)")) return grade >= 90;
                } catch (Exception e) { return true; }
                return true;
            }
        });
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private void setupTableUI() {
        table.setRowHeight(30);
        table.getTableHeader().setBackground(primaryBlue);
        table.getTableHeader().setForeground(Color.WHITE);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JTextField createStyledField(String title) {
        JTextField f = new JTextField();
        f.setBorder(BorderFactory.createTitledBorder(title));
        return f;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(140, 35));
        return b;
    }

    private void deleteRecord() {
        int sel = table.getSelectedRow();
        if (sel != -1) model.removeRow(table.convertRowIndexToModel(sel));
    }

    private void loadCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); String line;
            while ((line = br.readLine()) != null) {
                String[] v = line.split(",");
                // Assuming CSV layout: ID, FName, LName, Lab1, Lab2, Lab3, Prelim, Att
                if (v.length >= 8) {
                    double l1 = Double.parseDouble(v[3]);
                    double l2 = Double.parseDouble(v[4]);
                    double l3 = Double.parseDouble(v[5]);
                    double pre = Double.parseDouble(v[6]);
                    double att = Double.parseDouble(v[7]);
                    double avg = (l1 + l2 + l3 + pre + att) / 5.0;
                    
                    model.addRow(new Object[]{v[0], v[1]+" "+v[2], l1, l2, l3, pre, att, String.format("%.2f", avg)});
                }
            }
        } catch (Exception e) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRecordSystem().setVisible(true));
    }
}