package com.project.ui;

import com.project.database.DatabaseConnection;
import com.project.model.Client;
import com.project.model.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReserveCar extends JDialog {
    private static final long serialVersionUID = 1L;

    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private static final Color PRIMARY_COLOR = new Color(63, 81, 181);
    private static final Color ACCENT_COLOR = new Color(0, 150, 136);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(97, 97, 97);

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FIELD_FONT = new Font("Roboto", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Roboto", Font.BOLD, 16);

    private JTextField carIdField;
    private JTextField emailField;
    private JButton reserveButton;
    private MainMenu mainMenu;

    public ReserveCar(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        setTitle("Reserve a Car");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initComponents();
        setupLayout();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        carIdField = createStyledTextField();
        emailField = createStyledTextField();
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Reserve a Car");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel carIdPanel = createFormField("Car ID", carIdField);
        mainPanel.add(carIdPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel emailPanel = createFormField("Your Email", emailField);
        mainPanel.add(emailPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        reserveButton = createButton("Reserve");
        reserveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(reserveButton);

        add(mainPanel);
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

        button.addActionListener(e -> reserveCar());
        return button;
    }

    private void reserveCar() {
        if (carIdField.getText().isEmpty() || emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both Car ID and Email", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int carId = Integer.parseInt(carIdField.getText());

            Vehicle vehicle = checkVehicleAvailability(carId);
            if (vehicle == null) {
                JOptionPane.showMessageDialog(this, 
                    "Car is not available or does not exist", 
                    "Reservation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Client client = findClient(emailField.getText());
            if (client == null) {
                JOptionPane.showMessageDialog(this, 
                    "Client not found. Please register first.", 
                    "Client Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            createReservation(client.getIdClient(), carId);

            updateVehicleStatus(carId, "Rented");

            JOptionPane.showMessageDialog(this, 
                "Car Reserved Successfully!", 
                "Reservation Confirmed", 
                JOptionPane.INFORMATION_MESSAGE);

            mainMenu.loadVehicles();

            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid Car ID. Please enter a valid number.", 
                "Input Error", 
                JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private Vehicle checkVehicleAvailability(int carId) throws SQLException {
        String sql = "SELECT * FROM Vehicule WHERE idVehicule = ? AND etat = 'Available'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, carId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Vehicle(
                        rs.getInt("idVehicule"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("annee"),
                        rs.getString("type"),
                        rs.getString("carburant"),
                        rs.getDouble("prixLocationJour"),
                        rs.getString("etat"),
                        rs.getString("image")
                    );
                }
            }
        }
        return null;
    }

    private Client findClient(String email) throws SQLException {
        String sql = "SELECT * FROM Client WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                        rs.getInt("idClient"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("licenseNumber"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("phoneNumber")
                    );
                }
            }
        }
        return null;
    }

    private void createReservation(int clientId, int vehicleId) throws SQLException {
        String sql = "INSERT INTO Reservation (idClient, idVehicule, dateDebut, dateFin, statut) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            LocalDate today = LocalDate.now();
            LocalDate returnDate = today.plusDays(7); 
            
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, vehicleId);
            pstmt.setString(3, today.toString());
            pstmt.setString(4, returnDate.toString());
            pstmt.setString(5, "Active");
            
            pstmt.executeUpdate();
        }
    }

    private void updateVehicleStatus(int vehicleId, String status) throws SQLException {
        String sql = "UPDATE Vehicule SET etat = ? WHERE idVehicule = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, vehicleId);
            
            pstmt.executeUpdate();
        }
    }
}
