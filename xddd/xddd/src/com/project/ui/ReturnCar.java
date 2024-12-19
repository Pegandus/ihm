package com.project.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.database.DatabaseConnection;

public class ReturnCar extends JDialog {
    private JPanel reservationsPanel;  
    private JPanel maintenancePanel;   
    private static final Color BACKGROUND_COLOR = new Color(240, 246, 252);
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(51, 102, 204);
    private MainMenu mainMenu;

    public ReturnCar(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        setTitle("Unavailable Cars");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);  
        splitPane.setBackground(BACKGROUND_COLOR);

        reservationsPanel = new JPanel();
        reservationsPanel.setLayout(new BoxLayout(reservationsPanel, BoxLayout.Y_AXIS));  
        reservationsPanel.setBackground(BACKGROUND_COLOR);
        JScrollPane reservationsScrollPane = new JScrollPane(reservationsPanel);
        reservationsScrollPane.setBackground(BACKGROUND_COLOR);
        
        maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new BoxLayout(maintenancePanel, BoxLayout.Y_AXIS));  
        maintenancePanel.setBackground(BACKGROUND_COLOR);
        JScrollPane maintenanceScrollPane = new JScrollPane(maintenancePanel);
        maintenanceScrollPane.setBackground(BACKGROUND_COLOR);

        splitPane.setLeftComponent(reservationsScrollPane);
        splitPane.setRightComponent(maintenanceScrollPane);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        
        loadReservations();
        loadMaintenanceCars();

        add(mainPanel);

        
        setVisible(true);
    }

    private void loadReservations() {
        
        String sql = "SELECT r.idReservation, r.idClient, r.idVehicule, r.dateDebut, r.dateFin, r.statut, " +
                     "v.marque, v.type, v.prixLocationJour, " + 
                     "c.firstName, c.lastName " +
                     "FROM Reservation r " +
                     "JOIN Vehicule v ON r.idVehicule = v.idVehicule " +
                     "JOIN Client c ON r.idClient = c.idClient " +
                     "WHERE r.statut != 'Returned'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            
            reservationsPanel.removeAll();

            while (rs.next()) {
                
                JPanel reservationCard = new JPanel();
                reservationCard.setLayout(new BoxLayout(reservationCard, BoxLayout.Y_AXIS));
                reservationCard.setBackground(PANEL_COLOR);
                reservationCard.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2)); 
                reservationCard.setMaximumSize(new Dimension(750, 150));
                reservationCard.setAlignmentX(Component.CENTER_ALIGNMENT);
                reservationCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                
                JLabel reservationIdLabel = new JLabel("Reservation ID: " + rs.getInt("idReservation"));
                JLabel clientNameLabel = new JLabel("Client: " + rs.getString("firstName") + " " + rs.getString("lastName"));
                JLabel vehicleInfoLabel = new JLabel("Vehicle: " + rs.getString("marque") + " (" + rs.getString("type") + ")");
                JLabel startDateLabel = new JLabel("Start Date: " + rs.getString("dateDebut"));
                JLabel endDateLabel = new JLabel("End Date: " + rs.getString("dateFin"));

                
                reservationIdLabel.setFont(new Font("Arial", Font.BOLD, 18));
                clientNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                vehicleInfoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                startDateLabel.setFont(new Font("Arial", Font.BOLD, 14));
                endDateLabel.setFont(new Font("Arial", Font.BOLD, 14));

                reservationCard.add(reservationIdLabel);
                reservationCard.add(clientNameLabel);
                reservationCard.add(vehicleInfoLabel);
                reservationCard.add(startDateLabel);
                reservationCard.add(endDateLabel);

               
                double dailyPrice = rs.getDouble("prixLocationJour");
                double totalPrice = dailyPrice * 7;
                JLabel totalPriceLabel = new JLabel("Total Price for 7 Days: " + totalPrice + "$");
                totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 14));
                reservationCard.add(totalPriceLabel); 

                
                JButton returnButton = new JButton("Return Selected Car");
                returnButton.setFont(new Font("Arial", Font.BOLD, 14));
                returnButton.setBackground(PRIMARY_COLOR);
                returnButton.setForeground(Color.WHITE);
                returnButton.setFocusPainted(false);
                returnButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                
                int reservationId = rs.getInt("idReservation");
                int vehicleId = rs.getInt("idVehicule");

                returnButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        returnSelectedCar(reservationId, vehicleId);
                    }
                });

                reservationCard.add(returnButton);

                
                reservationsPanel.add(reservationCard);

               
                JSeparator separator = new JSeparator();
                separator.setPreferredSize(new Dimension(750, 10));
                reservationsPanel.add(separator);
            }

           
            reservationsPanel.revalidate();
            reservationsPanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadMaintenanceCars() {
        String sql = "SELECT idVehicule, marque, modele, annee, type, carburant, prixLocationJour, etat " +
                     "FROM Vehicule WHERE etat = 'Under Maintenance'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            maintenancePanel.removeAll(); 

            while (rs.next()) {
                JPanel maintenanceCard = new JPanel();
                maintenanceCard.setLayout(new BoxLayout(maintenanceCard, BoxLayout.Y_AXIS));
                maintenanceCard.setBackground(PANEL_COLOR);
                maintenanceCard.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
                maintenanceCard.setMaximumSize(new Dimension(750, 200));
                maintenanceCard.setAlignmentX(Component.CENTER_ALIGNMENT);
                maintenanceCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JLabel vehicleInfoLabel = new JLabel("Vehicle: " + rs.getString("marque") + " (" + rs.getString("modele") + ")");
                JLabel yearLabel = new JLabel("Year: " + rs.getInt("annee"));
                JLabel typeLabel = new JLabel("Type: " + rs.getString("type"));
                JLabel fuelLabel = new JLabel("Fuel: " + rs.getString("carburant"));
                JLabel priceLabel = new JLabel("Rental Price (Per Day): " + rs.getDouble("prixLocationJour") + "$");

                vehicleInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
                yearLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                fuelLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                priceLabel.setFont(new Font("Arial", Font.BOLD, 14));

                maintenanceCard.add(vehicleInfoLabel);
                maintenanceCard.add(yearLabel);
                maintenanceCard.add(typeLabel);
                maintenanceCard.add(fuelLabel);
                maintenanceCard.add(priceLabel);

                
                JButton changeStatusButton = new JButton("Mark as Available");
                changeStatusButton.setFont(new Font("Arial", Font.BOLD, 14));
                changeStatusButton.setBackground(PRIMARY_COLOR);
                changeStatusButton.setForeground(Color.WHITE);
                changeStatusButton.setFocusPainted(false);
                changeStatusButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                
                int vehicleId = rs.getInt("idVehicule");

                changeStatusButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        markCarAsAvailable(vehicleId); 
                    }
                });

                maintenanceCard.add(changeStatusButton);
                maintenancePanel.add(maintenanceCard);

               
                JSeparator separator = new JSeparator();
                separator.setPreferredSize(new Dimension(750, 10));
                maintenancePanel.add(separator);
            }

            maintenancePanel.revalidate();
            maintenancePanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void markCarAsAvailable(int vehicleId) {
        String updateVehicleSQL = "UPDATE Vehicule SET etat = 'Available' WHERE idVehicule = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateVehicleSQL)) {

            pstmt.setInt(1, vehicleId);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Vehicle marked as available.", "Success", JOptionPane.INFORMATION_MESSAGE);

            
            mainMenu.loadVehicles();
            loadMaintenanceCars();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to mark the vehicle as available. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


   
    private void returnSelectedCar(int reservationId, int vehicleId) {
        
        String updateReservationSQL = "UPDATE Reservation SET statut = 'Returned' WHERE idReservation = ?";
        String updateVehicleSQL = "UPDATE Vehicule SET etat = 'Available' WHERE idVehicule = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(updateReservationSQL);
             PreparedStatement pstmt2 = conn.prepareStatement(updateVehicleSQL)) {

            
            pstmt1.setInt(1, reservationId);
            pstmt1.executeUpdate();

            
            pstmt2.setInt(1, vehicleId);
            pstmt2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car returned successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            
            loadReservations();
            loadMaintenanceCars();

            mainMenu.loadVehicles();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to return the car. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
}
