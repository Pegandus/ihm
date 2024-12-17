package com.project.model;

public class Vehicle {
    private int idVehicule;
    private String marque;
    private String modele;
    private int annee;
    private String type;
    private String carburant;
    private double prixLocationJour;
    private String etat;

    public Vehicle(int idVehicule, String marque, String modele, int annee, 
                   String type, String carburant, double prixLocationJour, String etat) {
        this.idVehicule = idVehicule;
        this.marque = marque;
        this.modele = modele;
        this.annee = annee;
        this.type = type;
        this.carburant = carburant;
        this.prixLocationJour = prixLocationJour;
        this.etat = etat;
    }

    // Getters and setters
    public int getIdVehicule() { return idVehicule; }
    public void setIdVehicule(int idVehicule) { this.idVehicule = idVehicule; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCarburant() { return carburant; }
    public void setCarburant(String carburant) { this.carburant = carburant; }
    public double getPrixLocationJour() { return prixLocationJour; }
    public void setPrixLocationJour(double prixLocationJour) { this.prixLocationJour = prixLocationJour; }
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
}