/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
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
    public boolean cancelTrip(Trip trip) throws ClassNotFoundException {
        PreparedStatement  stm = null;
        ResultSet rs = null;

        try {
            String sql = "UPDATE trips SET status = ?";   
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");

            String status = "taken";
            stm = con.prepareStatement(sql);
            stm.setString(1, status);

            
            stm.executeUpdate();

            con.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // On failure, send a message from here.
    }
    
}
