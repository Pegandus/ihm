package com.project.ui;

import com.project.database.DatabaseConnection;
import com.project.model.Vehicle;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class AddCarDialog extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField marqueField, modeleField, anneeField, typeField, prixField;
    private JCheckBox gasolineCheckBox, dieselCheckBox, electricityCheckBox;
    private JCheckBox rentedCheckBox, availableCheckBox, maintenanceCheckBox;
    private JLabel imagePreviewLabel;
    private File selectedImageFile;

    public AddCarDialog(JFrame parent) {
        super(parent, "Add New Car", true);
        setSize(500, 600);
        setLayout(new BorderLayout(10, 10));
        
        // Color scheme
        Color backgroundColor = new Color(240, 246, 252);
        Color textColor = new Color(33, 43, 54);
        Color primaryColor = new Color(51, 102, 204);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(backgroundColor);
        
        // Image selection
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePanel.setBackground(backgroundColor);
        imagePreviewLabel = new JLabel("No image selected");
        imagePreviewLabel.setPreferredSize(new Dimension(200, 150));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JButton selectImageButton = new JButton("Select Image");
        selectImageButton.addActionListener(e -> selectImage());
        
        imagePanel.add(new JLabel("Car Image: "));
        imagePanel.add(imagePreviewLabel);
        imagePanel.add(selectImageButton);
        mainPanel.add(imagePanel);
        
        // Form fields
        mainPanel.add(createFormField("Brand (Marque):", marqueField = new JTextField(20)));
        mainPanel.add(createFormField("Model (Modele):", modeleField = new JTextField(20)));
        mainPanel.add(createFormField("Year (Annee):", anneeField = new JTextField(20)));
        mainPanel.add(createFormField("Type:", typeField = new JTextField(20)));
        mainPanel.add(createFormField("Price per Day:", prixField = new JTextField(20)));
        
        // Fuel Type Checkboxes
        JPanel fuelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fuelPanel.setBackground(backgroundColor);
        fuelPanel.add(new JLabel("Fuel Type: "));
        ButtonGroup fuelGroup = new ButtonGroup();
        gasolineCheckBox = new JCheckBox("Gasoline");
        dieselCheckBox = new JCheckBox("Diesel");
        electricityCheckBox = new JCheckBox("Electricity");
        fuelGroup.add(gasolineCheckBox);
        fuelGroup.add(dieselCheckBox);
        fuelGroup.add(electricityCheckBox);
        fuelPanel.add(gasolineCheckBox);
        fuelPanel.add(dieselCheckBox);
        fuelPanel.add(electricityCheckBox);
        mainPanel.add(fuelPanel);
        
        // State Checkboxes
        JPanel statePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statePanel.setBackground(backgroundColor);
        statePanel.add(new JLabel("State: "));
        ButtonGroup stateGroup = new ButtonGroup();
        rentedCheckBox = new JCheckBox("Rented");
        availableCheckBox = new JCheckBox("Available");
        maintenanceCheckBox = new JCheckBox("Under Maintenance");
        stateGroup.add(rentedCheckBox);
        stateGroup.add(availableCheckBox);
        stateGroup.add(maintenanceCheckBox);
        availableCheckBox.setSelected(true); // Default to Available
        statePanel.add(rentedCheckBox);
        statePanel.add(availableCheckBox);
        statePanel.add(maintenanceCheckBox);
        mainPanel.add(statePanel);
        
        // Save Button
        JButton saveButton = new JButton("Save Car");
        saveButton.setBackground(primaryColor);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveCar());
        
        mainPanel.add(saveButton);
        
        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(parent);
        
        setVisible(true);

    }
    
    private JPanel createFormField(String label, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(240, 246, 252));
        panel.add(new JLabel(label));
        panel.add(field);
        return panel;
    }
    
    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Car Image");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(selectedImageFile.getAbsolutePath())
                .getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH));
            imagePreviewLabel.setIcon(imageIcon);
            imagePreviewLabel.setText("");
        }
    }

    
    private void saveCar() {
        try {
            // Validate inputs
            if (marqueField.getText().isEmpty() || modeleField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Brand and Model are required!");
                return;
            }
            
            // Determine fuel type
            String fuelType = "";
            if (gasolineCheckBox.isSelected()) fuelType = "Gasoline";
            else if (dieselCheckBox.isSelected()) fuelType = "Diesel";
            else if (electricityCheckBox.isSelected()) fuelType = "Electricity";
            
            // Determine state
            String state = "";
            if (availableCheckBox.isSelected()) state = "Available";
            else if (rentedCheckBox.isSelected()) state = "Rented";
            else if (maintenanceCheckBox.isSelected()) state = "Under Maintenance";
            
            Vehicle newVehicle = new Vehicle(
                0, // ID will be auto-generated
                marqueField.getText(), 
                modeleField.getText(), 
                Integer.parseInt(anneeField.getText()),
                typeField.getText(), 
                fuelType, 
                Double.parseDouble(prixField.getText()), 
                state
            );
            
            // Save to database
            DatabaseConnection.addVehicle(newVehicle);
            
            // Optional: If you want to handle image upload, you'd do that here
            // You might want to save the image to a specific directory 
            // and store the path in the database
            
            JOptionPane.showMessageDialog(this, "Car Added Successfully!");
            dispose(); // Close the dialog
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Year and Price!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding car: " + e.getMessage());
        }
    }
}