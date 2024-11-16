// Shipment.java
package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idShipment;

    private String address;
    private String shipmentMethod;

    // Getters and Setters
    public Long getIdShipment() {
        return idShipment;
    }

    public void setIdShipment(Long idShipment) {
        this.idShipment = idShipment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShipmentMethod() {
        return shipmentMethod;
    }

    public void setShipmentMethod(String shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
    }

    public void trackShipment() {
        // Logic for tracking shipment
    }
}
