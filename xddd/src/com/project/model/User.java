package com.project.model;

public class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String address;
    protected String role;

    // Constructor
    public User(String firstName, String lastName, String email, String password, String address, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }
    

    public String getFirstName() { return firstName; }
    public void setfirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setlastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setpassword(String password) { this.password = password; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
