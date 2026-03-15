package gui;

import model.User;
import controller.LibraryManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {
    private LibraryManager manager;
    private DefaultTableModel userTableModel;
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> roleCombo;
    private JButton addBtn;

    public AdminPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        layoutComponents();
        setupActions();
    }

    private void initComponents() {
        // Form Fields
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        roleCombo = new JComboBox<>(new String[]{"Member", "Staff", "Admin"});
        addBtn = new JButton("Add User Account");
        addBtn.setBackground(new Color(0, 0, 0));
        addBtn.setForeground(Color.WHITE);

        // Table Setup
        String[] columns = {"User ID", "Full Name", "Email Address", "Access Level"};
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private void layoutComponents() {
        // --- Input Section ---
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Registration Console"));
        
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Role:"));
        inputPanel.add(roleCombo);
        inputPanel.add(addBtn);

        add(inputPanel, BorderLayout.NORTH);

        // --- Table Section ---
        JTable userTable = new JTable(userTableModel);
        userTable.setRowHeight(25);
        userTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Active System Users"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupActions() {
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            
            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Logic: Manager generates the ID internally
            manager.addUser(name, email); 
            updateUserTable();
            
            // Clear fields
            nameField.setText("");
            emailField.setText("");
            JOptionPane.showMessageDialog(this, "User added successfully!");
        });
    }

    /**
     * Synchronizes the table with the manager's current user list.
     * This is called by MainWindow during a global refresh.
     */
    public void updateUserTable() {
        userTableModel.setRowCount(0);
        for (User user : manager.getUsers()) {
            userTableModel.addRow(new Object[]{
                user.getUserId(), 
                user.getName(), 
                user.getEmail(), 
                user.getRole()
            });
        }
    }
}