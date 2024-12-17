package com.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.model.Client;
import com.project.model.User;
import com.project.model.Vehicle;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:C:/Users/pc/eclipse-workspace/xddd/src/DBfiles/vehicle_rental.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static List<Vehicle> getAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM Vehicule WHERE etat = 'Available'";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                    rs.getInt("idVehicule"),
                    rs.getString("marque"),
                    rs.getString("modele"),
                    rs.getInt("annee"),
                    rs.getString("type"),
                    rs.getString("carburant"),
                    rs.getDouble("prixLocationJour"),
                    rs.getString("etat")
                );
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public static void addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO Vehicule (marque, modele, annee, type, carburant, prixLocationJour, etat) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, vehicle.getMarque());
            pstmt.setString(2, vehicle.getModele());
            pstmt.setInt(3, vehicle.getAnnee());
            pstmt.setString(4, vehicle.getType());
            pstmt.setString(5, vehicle.getCarburant());
            pstmt.setDouble(6, vehicle.getPrixLocationJour());
            pstmt.setString(7, vehicle.getEtat());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public static List<Client> getAvailableClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client ";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Client client = new Client(
                	rs.getInt("idClient"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("licenseNumber"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("phoneNumber")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
    
    
    public static void addClient(Client client) {
        String sql = "INSERT INTO Client (firstName, lastName, licenseNumber, email, address, phoneNumber) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setString(3, client.getLicenseNumber());
            pstmt.setString(4, client.getEmail());
            pstmt.setString(5, client.getAddress());
            pstmt.setString(6, client.getPhoneNumber());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void addUser(User user) {
        String sql = "INSERT INTO Utilisateur (firstName, lastName, email, password, address, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFirstName()); // nom
            pstmt.setString(2, user.getLastName());  // prenom
            pstmt.setString(3, user.getEmail());     // email
            pstmt.setString(4, user.getPassword());  // mot_de_passe
            pstmt.setString(5, user.getAddress());   // adresse
            pstmt.setString(6, user.getRole());      // role

            pstmt.executeUpdate();
            System.out.println("User signed up successfully.");
        } catch (SQLException e) {
            System.err.println("Error while adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }


}


