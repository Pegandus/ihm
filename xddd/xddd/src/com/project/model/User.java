package com.project.model;

public class User {
	protected int id_utilisateur;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String address;
    protected String role;

    public User(int id_utilisateur, String firstName, String lastName, String email, String password, String address, String role) {
        this.id_utilisateur = id_utilisateur;
    	this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }
    
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role); 
    }
    
    public int getId() { return id_utilisateur; }
    public void setId(int id_utilisateur) { this.id_utilisateur = id_utilisateur; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
