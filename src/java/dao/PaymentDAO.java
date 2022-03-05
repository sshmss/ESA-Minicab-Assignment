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
import java.util.ArrayList;
import models.Customers;
import models.User;
import models.Payment;


/**
 *
 * @author Hisan
 */
public class PaymentDAO {
    public boolean addPayment(Payment payment) throws ClassNotFoundException {
        PreparedStatement  stm = null;
        ResultSet rs = null;

        try {
            String sql = "INSERT into payments (fixed_amt, fare_amt, total_amt) VALUES (?,?,?)";   
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");

            stm = con.prepareStatement(sql);
            stm.setFloat(1, payment.getFixedAmt());
            stm.setFloat(2, payment.getFareAmt());
            stm.setFloat(3, payment.getTotalAmt());
            
            stm.executeUpdate();

            con.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // On failure, send a message from here.
    }
}
