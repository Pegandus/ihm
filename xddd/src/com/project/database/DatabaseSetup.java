package com.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSetup {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/Users/pc/eclipse-workspace/xddd/src/DBfiles/vehicle_rental.db";
        
        String[] sqlStatements = {
            """
            CREATE TABLE IF NOT EXISTS Vehicule (
                idVehicule INTEGER PRIMARY KEY AUTOINCREMENT,
                marque TEXT NOT NULL,
                modele TEXT NOT NULL,
                annee INTEGER NOT NULL,
                type TEXT NOT NULL,
                carburant TEXT NOT NULL,
                prixLocationJour REAL NOT NULL,
                etat TEXT NOT NULL,
        		image TEXT
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS Utilisateur (
                id_utilisateur INTEGER PRIMARY KEY AUTOINCREMENT,
                firstName TEXT,
                lastName TEXT,
                email TEXT,
                password TEXT,
                address TEXT,
                role TEXT
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS Client (
                idClient INTEGER PRIMARY KEY AUTOINCREMENT,
                firstName TEXT,
                lastName TEXT,
                licenseNumber TEXT,
                email TEXT,
                address TEXT,
                phoneNumber TEXT
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS Reservation (
                idReservation INTEGER PRIMARY KEY AUTOINCREMENT,
                idClient INTEGER NOT NULL,
                idVehicule INTEGER NOT NULL,
                dateDebut DATE NOT NULL,
                dateFin DATE NOT NULL,
                statut TEXT NOT NULL,
                FOREIGN KEY (idClient) REFERENCES Client(idClient),
                FOREIGN KEY (idVehicule) REFERENCES Vehicule(idVehicule)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS Retour (
                idRetour INTEGER PRIMARY KEY AUTOINCREMENT,
                idReservation INTEGER NOT NULL,
                dateRetour DATE NOT NULL,
                montant REAL NOT NULL,
                FOREIGN KEY (idReservation) REFERENCES Reservation(idReservation)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS Paiement (
                idPaiement INTEGER PRIMARY KEY AUTOINCREMENT,
                idReservation INTEGER NOT NULL,
                datePaiement DATE NOT NULL,
                montant REAL NOT NULL,
                FOREIGN KEY (idReservation) REFERENCES Reservation(idReservation)
            );
            """
        };

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            for (String sql : sqlStatements) {
                stmt.execute(sql);
            }
            System.out.println("All tables created or already exist.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
