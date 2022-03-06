/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import controllers.AdminController;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Customers;
import models.Payment;
import models.User;
import models.Trip;

/**
 *
 * @author Hisan
 */
public class TripDAO {
    public boolean addTrip(Trip trip) throws ClassNotFoundException {
        PreparedStatement  stm = null;

        try {
            Payment payment = trip.getPayment();
            
            String sql = "INSERT into payments (fixed_amt, fare_amt, total_amt) VALUES (?,?,?)";   
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");

            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setFloat(1, payment.getFixedAmt());
            stm.setFloat(2, payment.getFareAmt());
            stm.setFloat(3, payment.getTotalAmt());
            
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys();  
            int paymentId = rs.next() ? rs.getInt(1) : 0;
            payment.setId(paymentId);
            String sql1 = "INSERT into trips (pickup_loc, destination_loc, distance, status, payment_id, passenger_id) VALUES (?,?,?,?,?,?)"; 
            String status = "open";
            stm = con.prepareStatement(sql1);
            stm.setString(1, trip.getPickupLoc());
            stm.setString(2, trip.getDestinationLoc());
            stm.setFloat(3, trip.getDistance());
            stm.setString(4, status);
            stm.setInt(5, paymentId);
            stm.setInt(6, trip.getPassenger().getId());
            stm.executeUpdate();

            con.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // On failure, send a message from here.
    }
    public boolean updateTripStatus(int tripId, int driverId) {
        PreparedStatement  stm = null;
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
            }
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            String sql = "SELECT status FROM trips WHERE trip_id = ?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, tripId);
            
            ResultSet rs = stm.executeQuery();
            String status = null;
            Integer drive = null;
            while (rs.next()) {
                if (rs.getString("status").equals("taken")) {
                    status = "open";
                } else {
                    status = "taken";
                    drive = driverId;
                }
            }
            sql = "UPDATE trips SET status = ?, driver_id = ? where trip_id = ?";   
            
            stm = con.prepareStatement(sql);
            stm.setString(1, status);
            stm.setInt(2, drive);
            stm.setInt(3, tripId);
            stm.executeUpdate();
            
            con.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // On failure, send a message from here.
    }
    public boolean cancelTrip(int tripId) {
        PreparedStatement  stm = null;
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
            }
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            
            String sql = "UPDATE trips SET status = 'cancelled' where trip_id = ?";
            
            stm = con.prepareStatement(sql);
            stm.setInt(1, tripId);
            stm.executeUpdate();
            
            con.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // On failure, send a message from here.
    }
    public boolean finishTrip(int tripId) {
        PreparedStatement  stm = null;
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
            }
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            
            String sql = "UPDATE trips SET status = 'finished' where trip_id = ?";
            
            stm = con.prepareStatement(sql);
            stm.setInt(1, tripId);
            stm.executeUpdate();
            
            con.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // On failure, send a message from here.
    }
    public ArrayList<Trip> listOfTrips() throws ServletException, IOException {
        Connection con = null;
        ArrayList<Trip> trips = new ArrayList<>();
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
            }
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            Statement stm = con.createStatement();
            String sql = "SELECT * FROM trips INNER JOIN payments ON (trips.payment_id=payments.payment_id) LEFT JOIN users ON (trips.driver_id=users.id) ORDER BY request_time DESC";
            ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
                Payment payment = new Payment(rs.getFloat("fixed_amt"), rs.getFloat("fare_amt"), rs.getFloat("total_amt"));
                User driver = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
                Trip trip = new Trip(rs.getInt("trip_id"), rs.getString("pickup_loc"), rs.getString("destination_loc"), rs.getString("status"), rs.getDate("request_time"), payment, driver);
                trips.add(trip);
            }
            
        con.close();
        System.out.println(trips);

        return trips;
        } catch (SQLException e) {
             e.printStackTrace();
        }
        
        return trips;
        
    }
    
    public ArrayList<Trip> listOfTripsByCustomer(User passenger) throws ServletException, IOException {
        Connection con = null;
        ArrayList<Trip> trips = new ArrayList<>();
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
            }
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            String sql = "SELECT * FROM trips INNER JOIN payments ON (trips.payment_id=payments.payment_id) LEFT JOIN users ON (trips.driver_id=users.id) WHERE passenger_id = ? ORDER BY request_time DESC";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, passenger.getId());
            ResultSet rs = stm.executeQuery();
        while (rs.next()) {
                Payment payment = new Payment(rs.getFloat("fixed_amt"), rs.getFloat("fare_amt"), rs.getFloat("total_amt"));
                User driver = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
                Trip trip = new Trip(rs.getInt("trip_id"), rs.getString("pickup_loc"), rs.getString("destination_loc"), rs.getString("status"), rs.getDate("request_time"), payment, driver);
                trips.add(trip);
            }
            
        con.close();
        System.out.println(trips);

        return trips;
        } catch (SQLException e) {
             e.printStackTrace();
        }
        
        return trips;
        
    }
}
