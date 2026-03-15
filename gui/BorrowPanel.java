package gui;

import controller.BorrowController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BorrowPanel extends JPanel {
    private JTextField itemField;
    private JTextField userField;
    private JButton borrowBtn;
    private JButton returnBtn;
    private BorrowController controller;
    private Runnable refreshCallback;

    public BorrowPanel(BorrowController controller, Runnable refreshCallback) {
        this.controller = controller;
        this.refreshCallback = refreshCallback;
        
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Transaction Console"));
        initComponents();
        layoutComponents();
        setupActions();
    }

    private void initComponents() {
        itemField = new JTextField(15);
        userField = new JTextField(15);
        borrowBtn = new JButton("Confirm Borrow");
        returnBtn = new JButton("Confirm Return");
        
        // Styling buttons
        borrowBtn.setBackground(new Color(40, 167, 69));
        borrowBtn.setForeground(Color.WHITE);
        returnBtn.setBackground(new Color(0, 123, 255));
        returnBtn.setForeground(Color.WHITE);
    }

    private void layoutComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Item ID
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Enter Item ID:"), gbc);
        gbc.gridx = 1;
        add(itemField, gbc);

        // Row 1: User ID
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Enter User ID:"), gbc);
        gbc.gridx = 1;
        add(userField, gbc);

        // Row 2: Actions
        gbc.gridx = 0; gbc.gridy = 2;
        add(borrowBtn, gbc);
        gbc.gridx = 1;
        add(returnBtn, gbc);
    }

    private void setupActions() {
        borrowBtn.addActionListener(e -> {
            boolean success = controller.processBorrow(itemField.getText(), userField.getText());
            handleResponse(success, "Item assigned to user.");
        });

        returnBtn.addActionListener(e -> {
            boolean success = controller.processReturn(itemField.getText());
            handleResponse(success, "Item returned to inventory.");
        });
    }

    private void handleResponse(boolean success, String successMsg) {
        if (success) {
            JOptionPane.showMessageDialog(this, "Success: " + successMsg);
            itemField.setText("");
            userField.setText("");
            // This triggers the global refresh in MainWindow
            if (refreshCallback != null) refreshCallback.run();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Invalid IDs or Item Status.", 
                "Transaction Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}