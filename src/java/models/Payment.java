/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Hisan
 */
public class Payment {
    private float fixedAmt, fareAmt, totalAmt;
    private int id;
    
    public Payment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getFixedAmt() {
        return fixedAmt;
    }

    public void setFixedAmt(float fixedAmt) {
        this.fixedAmt = fixedAmt;
    }

    public float getFareAmt() {
        return fareAmt;
    }

    public void setFareAmt(float fareAmt) {
        this.fareAmt = fareAmt;
    }

    public float getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(float totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Payment(float fixedAmt, float fareAmt, float totalAmt) {
        this.fixedAmt = fixedAmt;
        this.fareAmt = fareAmt;
        this.totalAmt = totalAmt;
    }
    
}
