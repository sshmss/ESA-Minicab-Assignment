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

/**
 *
 * @author ahsan_764ad7k
 */
public class UserDAO {
    public String listOfCustomers(ArrayList<User> customers) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement  stm = null;
        ResultSet rs = null;
        String fullName, userName, email = null;
        
        try {
            String sql = "SELECT FULLNAME, USERNAME, EMAIL FROM USERS WHERE USERROLE = 'Customer'";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            stm = con.prepareStatement(sql);
            
        while (rs.next()) {

                fullName = rs.getString("FULLNAME");
                userName = rs.getString("USERNAME");
                email = rs.getString("EMAIL");
            }   
            
        con.close();
        
        return "HM";
        } catch (SQLException e) {
             e.printStackTrace();
        }
        
        
        return "Maybe you want to check that again";
        
    }
    
    public User getUserByUsername(String username) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement  stm = null;
        String password = null, role = null;
        int id = 0;
        
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            stm = con.prepareStatement(sql);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            
        while (rs.next()) {
                
                password = rs.getString("password");
                role = rs.getString("role");
                id = rs.getInt("id");

            }   
            
        con.close();
        
        User user = new User(username, password, role);
        user.setId(id);
        return user;
        } catch (SQLException e) {
             e.printStackTrace();
        }
        
        
        return null;
        
    }
    
    public boolean addCustomer(User customer) throws ClassNotFoundException {
        PreparedStatement  stm = null;
        ResultSet rs = null;

        try {
            String sql = "INSERT into users (username, password, role) VALUES (?,?,?)";   
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");

            stm = con.prepareStatement(sql);
            stm.setString(1, customer.getUsername());
            stm.setString(2, customer.getPassword());         
            stm.setString(3, "customer");

            
            stm.executeUpdate();

            con.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // On failure, send a message from here.
    }
    
    public String deleteCustomer(User customer) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement  stm = null;
        ResultSet rs = null;

        try {

            String sql = "DELETE FROM USERS WHERE username = ?";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            stm = con.prepareStatement(sql);
            stm.setString(1,customer.getUsername() );
            
            int row = stm.executeUpdate();
            System.out.println(row);

            
            

            con.close();
            deleteFromCustomerTable(customer);
            return "OK";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Oops.. Something went wrong there..!";  // On failure, send a message from here.
    }
    
    public String updateCustomer(User customer) throws ClassNotFoundException {
        
        Connection con = null;
        PreparedStatement  stm = null;
        ResultSet rs = null;

        try {

            String sql = "UPDATE USERS SET password=? WHERE username=?";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            stm = con.prepareStatement(sql);
            
            stm.setString(1,customer.getPassword());
            stm.setString(2,customer.getUsername());            
            
            int row = stm.executeUpdate();
            System.out.println(row);

           
            
            con.close();
            
            return "OK";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Oops.. Something went wrong there..!";  // On failure, send a message from here.
    }
    
    private void deleteFromCustomerTable(User customer) throws ClassNotFoundException
    {
        Connection con = null;
        PreparedStatement  stm = null;

        try {
            String sql = "DELETE FROM Customer WHERE EMAIL = ?";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            stm = con.prepareStatement(sql);
            stm.setString(1,customer.getUsername() );
            
            int row = stm.executeUpdate();
            System.out.println(row);
            con.close();
            
            System.out.println("Removed from customer Table");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}
