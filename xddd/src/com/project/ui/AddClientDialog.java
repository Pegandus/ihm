/*package com.project.ui;

import com.project.database.DatabaseConnection;
import com.project.model.Client;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class AddClientDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    // Personal Information Fields
    private JTextField nomField, prenomField, emailField, addressField, phoneField, passwordField;
    
    // Driving License Fields
    private JTextField licensNumberField, licenseTypeField;
    private JTextField licenseDateIssuedField, licenseExpirationField;
    
    // Coordinates Fields
    private JTextField latitudeField, longitudeField;

    // License Document Upload
    private JLabel licensePreviewLabel;
    private File selectedLicenseFile;

    public AddClientDialog(JFrame parent) {
        super(parent, "Add New Client", true);
        setSize(600, 800);
        setLayout(new BorderLayout(10, 10));
        
        // Color scheme (consistent with other dialogs)
        Color backgroundColor = new Color(240, 246, 252);
        Color textColor = new Color(33, 43, 54);
        Color primaryColor = new Color(51, 102, 204);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(backgroundColor);
        
        // Personal Information Section
        mainPanel.add(createSectionHeader("Personal Information"));
        mainPanel.add(createFormField("First Name:", prenomField = new JTextField(20)));
        mainPanel.add(createFormField("Last Name:", nomField = new JTextField(20)));
        mainPanel.add(createFormField("Email:", emailField = new JTextField(20)));
        mainPanel.add(createFormField("Address:", addressField = new JTextField(20)));
        mainPanel.add(createFormField("Phone Number:", phoneField = new JTextField(20)));
        mainPanel.add(createFormField("Password:", passwordField = new JPasswordField(20)));
        
        // Driving License Section
        mainPanel.add(createSectionHeader("Driving License"));
        mainPanel.add(createFormField("License Number:", licensNumberField = new JTextField(20)));
        mainPanel.add(createFormField("License Type:", licenseTypeField = new JTextField(20)));
        mainPanel.add(createFormField("Date Issued:", licenseDateIssuedField = new JTextField(20)));
        mainPanel.add(createFormField("Expiration Date:", licenseExpirationField = new JTextField(20)));
        
        // License Document Upload
        JPanel licenseUploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        licenseUploadPanel.setBackground(backgroundColor);
        licensePreviewLabel = new JLabel("No license document selected");
        licensePreviewLabel.setPreferredSize(new Dimension(200, 150));
        licensePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JButton selectLicenseButton = new JButton("Upload License");
        selectLicenseButton.addActionListener(e -> selectLicenseDocument());
        
        licenseUploadPanel.add(new JLabel("License Document: "));
        licenseUploadPanel.add(licensePreviewLabel);
        licenseUploadPanel.add(selectLicenseButton);
        mainPanel.add(licenseUploadPanel);
        
        // Coordinates Section
        mainPanel.add(createSectionHeader("Coordinates (Optional)"));
        mainPanel.add(createFormField("Latitude:", latitudeField = new JTextField(20)));
        mainPanel.add(createFormField("Longitude:", longitudeField = new JTextField(20)));
        
        // Save Button
        JButton saveButton = new JButton("Save Client");
        saveButton.setBackground(primaryColor);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveClient());
        
        mainPanel.add(saveButton);
        
        // Scrollable for long forms
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getViewport().setBackground(backgroundColor);
        
        add(scrollPane, BorderLayout.CENTER);
        setLocationRelativeTo(parent);
        
        setVisible(true);
    }
    
    private JPanel createSectionHeader(String title) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(240, 246, 252));
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(headerLabel);
        return headerPanel;
    }
    
    private JPanel createFormField(String label, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(240, 246, 252));
        panel.add(new JLabel(label));
        panel.add(field);
        return panel;
    }
    
    private void selectLicenseDocument() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select License Document");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image and PDF Files", "jpg", "png", "gif", "jpeg", "pdf"
        );
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedLicenseFile = fileChooser.getSelectedFile();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(selectedLicenseFile.getAbsolutePath())
                .getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH));
            licensePreviewLabel.setIcon(imageIcon);
            licensePreviewLabel.setText("");
        }
    }
    
    private void saveClient() {
        try {
            // Validation
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() 
                || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, Email, and Password are required!");
                return;
            }
            
            // Create Client object
            Client newClient = new Client(
                0, // ID will be auto-generated
                nomField.getText(), 
                prenomField.getText(), 
                emailField.getText(),
                addressField.getText(),
                phoneField.getText(),
                passwordField.getText()
            );
            
            // Optional: Method to save additional license details
            // This might require extending the Client model or creating a separate LicenseDetails class
            if (!licensNumberField.getText().isEmpty()) {
                // You might want to create a method in DatabaseConnection to save license details
                // DatabaseConnection.saveLicenseDetails(newClient.getIdClient(), licenseDetails);
            }
            
            // Optional: Save coordinates
            if (!latitudeField.getText().isEmpty() && !longitudeField.getText().isEmpty()) {
                try {
                    double latitude = Double.parseDouble(latitudeField.getText());
                    double longitude = Double.parseDouble(longitudeField.getText());
                    // You might want to create a method to save coordinates
                    // DatabaseConnection.saveClientCoordinates(newClient.getIdClient(), latitude, longitude);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid coordinate format!");
                    return;
                }
            }
            
            // Save to database
            DatabaseConnection.addClient(newClient);
            
            // Optional: Handle license document upload
            if (selectedLicenseFile != null) {
                // Implement logic to save/store the license document
                // For example, copy to a specific directory and save the path
            }
            
            JOptionPane.showMessageDialog(this, "Client Added Successfully!");
            dispose(); // Close the dialog
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding client: " + e.getMessage());
        }
    }
}*/