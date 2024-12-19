package com.project.ui;

import com.project.database.DatabaseConnection;
import com.project.model.Vehicle;

import javax.swing.*;
import java.awt.*;

public class ModifyVehicleDialog extends JDialog {
    public ModifyVehicleDialog(Vehicle vehicle, JFrame parent) {
        super(parent, "Modify Vehicle", true);
        setLayout(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        setSize(500, 400); 

        JTextField marqueField = new JTextField(vehicle.getMarque(), 20);
        JTextField modeleField = new JTextField(vehicle.getModele(), 20);
        JTextField anneeField = new JTextField(String.valueOf(vehicle.getAnnee()), 20);
        JTextField carburantField = new JTextField(vehicle.getCarburant(), 20);
        JTextField prixField = new JTextField(String.valueOf(vehicle.getPrixLocationJour()), 20);

        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Marque:"), gbc);
        gbc.gridx = 1;
        add(marqueField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Modele:"), gbc);
        gbc.gridx = 1;
        add(modeleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        add(anneeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Fuel:"), gbc);
        gbc.gridx = 1;
        add(carburantField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Price/Day:"), gbc);
        gbc.gridx = 1;
        add(prixField, gbc);

        JCheckBox availableCheckBox = new JCheckBox("Available");
        JCheckBox underMaintenanceCheckBox = new JCheckBox("Under Maintenance");

        if (vehicle.getEtat().equals("Available")) {
            availableCheckBox.setSelected(true);
        } else if (vehicle.getEtat().equals("Under Maintenance")) {
            underMaintenanceCheckBox.setSelected(true);
        }

        availableCheckBox.addActionListener(e -> {
            if (availableCheckBox.isSelected()) {
                underMaintenanceCheckBox.setSelected(false);
            }
        });

        underMaintenanceCheckBox.addActionListener(e -> {
            if (underMaintenanceCheckBox.isSelected()) {
                availableCheckBox.setSelected(false);
            }
        });

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout()); 
        statusPanel.add(availableCheckBox);
        statusPanel.add(underMaintenanceCheckBox);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        add(statusPanel, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            vehicle.setMarque(marqueField.getText());
            vehicle.setModele(modeleField.getText());
            vehicle.setAnnee(Integer.parseInt(anneeField.getText()));
            vehicle.setCarburant(carburantField.getText());
            vehicle.setPrixLocationJour(Double.parseDouble(prixField.getText()));

            if (availableCheckBox.isSelected()) {
                vehicle.setEtat("Available");
            } else if (underMaintenanceCheckBox.isSelected()) {
                vehicle.setEtat("Under Maintenance");
            }

            DatabaseConnection.updateVehicle(vehicle); 
            dispose(); 
        });

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; 
        gbc.insets = new Insets(20, 10, 10, 10); 
        add(saveButton, gbc);

        setLocationRelativeTo(parent); 
        setVisible(true);
    }
}
