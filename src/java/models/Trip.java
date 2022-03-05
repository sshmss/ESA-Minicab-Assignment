/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;

/**
 *
 * @author Hisan
 */
public class Trip {
    private String pickupLoc, destinationLoc, status;
    private float distance;
    private Date requestTime;
    private User passenger, driver;
    private Payment payment;

    public Trip() {
    }

    public String getStatus() {
        return status;
    }

    public Trip(String status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    

    public String getPickupLoc() {
        return pickupLoc;
    }

    public void setPickupLoc(String pickupLoc) {
        this.pickupLoc = pickupLoc;
    }

    public String getDestinationLoc() {
        return destinationLoc;
    }

    public void setDestinationLoc(String desinationLoc) {
        this.destinationLoc = desinationLoc;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
}
