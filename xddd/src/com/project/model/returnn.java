package com.project.model;

import java.util.Date;

public class returnn {
    private int idRetour;
    private int idReservation;
    private Date dateRetour;
    private String etatRetour;
    private double fraisSupplementaires;

    // Constructor
    public returnn(int idRetour, int idReservation, Date dateRetour, String etatRetour, double fraisSupplementaires) {
        this.idRetour = idRetour;
        this.idReservation = idReservation;
        this.dateRetour = dateRetour;
        this.etatRetour = etatRetour;
        this.fraisSupplementaires = fraisSupplementaires;
    }

    // Getters and Setters
    public int getIdRetour() {
        return idRetour;
    }

    public void setIdRetour(int idRetour) {
        this.idRetour = idRetour;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public String getEtatRetour() {
        return etatRetour;
    }

    public void setEtatRetour(String etatRetour) {
        this.etatRetour = etatRetour;
    }

    public double getFraisSupplementaires() {
        return fraisSupplementaires;
    }

    public void setFraisSupplementaires() {
    	  this.fraisSupplementaires = fraisSupplementaires;
    }
}