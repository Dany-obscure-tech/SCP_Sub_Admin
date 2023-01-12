package com.example.scpsubadmin;

class AdminDetails_class {
    // Static variable reference of single_instance
    // of type Singleton
    private static AdminDetails_class adminDetails_class = null;

    // Declaring a variable of type String
    public String name;
    public String parking;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private AdminDetails_class()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    // Static method
    // Static method to create instance of Singleton class
    public static AdminDetails_class getInstance()
    {
        if (adminDetails_class == null)
            adminDetails_class = new AdminDetails_class();

        return adminDetails_class;
    }
}
