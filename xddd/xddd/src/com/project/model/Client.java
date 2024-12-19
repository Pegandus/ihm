package com.project.model;

public class Client {
	private int idClient;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String licenseNumber;

    public Client(int idClient, String firstName, String lastName, String licenseNumber, String email, String address, String phoneNumber) {
        this.idClient = idClient;
    	this.firstName = firstName;
        this.lastName = lastName;
        this.licenseNumber = licenseNumber;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    
    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }
    
    public String getFirstName() { return firstName; }
    public void setfirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setlastName(String lastName) { this.lastName = lastName; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setlicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setphoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
