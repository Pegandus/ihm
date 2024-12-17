package com.project.model;

public class Payment {
    private int idPaiement;
    private int idReservation;
    private double montant;
    private String methodePaiement;
    private String statut;

    // Constructor
    public Payment(int idPaiement, int idReservation, double montant, String methodePaiement, String statut) {
        this.idPaiement = idPaiement;
        this.idReservation = idReservation;
        this.montant = montant;
        this.methodePaiement = methodePaiement;
        this.statut = statut;
    }

    // Getters and Setters
    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getMethodePaiement() {
        return methodePaiement;
    }

    public void setMethodePaiement(String methodePaiement) {
        this.methodePaiement = methodePaiement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
