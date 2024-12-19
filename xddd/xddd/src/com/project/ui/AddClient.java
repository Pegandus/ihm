package com.project.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.project.model.*;
import com.project.database.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class AddClient extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private static final Color PRIMARY_COLOR = new Color(63, 81, 181);
    private static final Color ACCENT_COLOR = new Color(0, 150, 136);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(97, 97, 97);

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FIELD_FONT = new Font("Roboto", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Roboto", Font.BOLD, 16);

    private List<JTextField> inputFields;
    private List<String> fieldLabels;
    private MainMenu mainMenu;

    public AddClient(JFrame f, MainMenu mainMenu) {
        super(f, "Add New Client", true);
        this.mainMenu = mainMenu;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        initComponents();
        setupLayout();
        
        pack();
        setLocationRelativeTo(f);
        setVisible(true);
    }

    private void initComponents() {
        inputFields = Arrays.asList(
            createStyledTextField(),
            createStyledTextField(),
            createStyledTextField(),
            createStyledTextField(),
            createStyledTextField(),
            createStyledTextField()
        );

        fieldLabels = Arrays.asList(
            "First Name", "Last Name", "Driver License Number", 
            "Email Address", "Home Address", "Phone Number"
        );
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Create New Client Profile");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        for (int i = 0; i < fieldLabels.size(); i++) {
            mainPanel.add(createFormField(fieldLabels.get(i), inputFields.get(i)));
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JButton saveButton = createButton("Save Profile");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(saveButton);

        add(mainPanel);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(FIELD_FONT);
        textField.setForeground(TEXT_PRIMARY);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return textField;
    }

    private JPanel createFormField(String label, JTextField field) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 5));
        fieldPanel.setOpaque(false);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(FIELD_FONT);
        fieldLabel.setForeground(TEXT_SECONDARY);

        fieldPanel.add(fieldLabel, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        return fieldPanel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });

        button.addActionListener(e -> saveClient());
        return button;
    }

    private void saveClient() {
        if (!validateInputs()) {
            return;
        }

        String firstName = inputFields.get(0).getText().trim();
        String lastName = inputFields.get(1).getText().trim();
        String licenseNumber = inputFields.get(2).getText().trim();
        String email = inputFields.get(3).getText().trim();
        String address = inputFields.get(4).getText().trim();
        String phoneNumber = inputFields.get(5).getText().trim();

        if (isEmailInUse(email)) {
            showValidationError("The email address is already in use. Please choose another.");
            return;
        }

        try {
            Client client = new Client(0, firstName, lastName, licenseNumber, email, address, phoneNumber);
            
            DatabaseConnection.addClient(client);
            
            JOptionPane.showMessageDialog(this, 
                "Client Profile Created Successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
            mainMenu.loadClients();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error creating profile: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        JTextField firstNameField = inputFields.get(0);
        JTextField lastNameField = inputFields.get(1);
        JTextField licenseField = inputFields.get(2);
        JTextField emailField = inputFields.get(3);

        if (firstNameField.getText().trim().isEmpty() || 
            lastNameField.getText().trim().isEmpty()) {
            showValidationError("First Name and Last Name are required.");
            return false;
        }

        String licenseNumber = licenseField.getText().trim();
        if (!Pattern.matches("^[A-Z]\\d{8}$", licenseNumber)) {
            showValidationError("Driver License must start with a letter and have 8 digits.");
            return false;
        }

        String email = emailField.getText().trim();
        if (!email.isEmpty() && !isValidEmail(email)) {
            showValidationError("Please enter a valid email address.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }
    
    private boolean isEmailInUse(String email) {
        try {
            List<Client> clients = DatabaseConnection.getAvailableClients();
            
            for (Client client : clients) {
                if (client.getEmail().equalsIgnoreCase(email)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
            this, 
            message, 
            "Validation Error", 
            JOptionPane.WARNING_MESSAGE
        );
    }
}