/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;
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
import models.Users;

/**
 *
 * @author ahsan_764ad7k
 */
public class Customer {
    public String listOfCustomers(ArrayList<Users> customers) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement  stm = null;
        ResultSet rs = null;
        String fullName, userName, email = null;
        
        try {
            String sql = "SELECT FULLNAME, USERNAME, EMAIL FROM USERS WHERE USERROLE = 'Customer'";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "pitiri");
            stm = con.prepareStatement(sql);
            
        while (rs.next()) {

                fullName = rs.getString("FULLNAME");
                userName = rs.getString("USERNAME");
                email = rs.getString("EMAIL");

                Users add = new Users();
                add.setFirstName(fullName);
                add.setLastName(userName);
                add.setEmail(email);
               
                customers.add(add);
            }   
            
        con.close();
        
        return "HM";
        } catch (SQLException e) {
             e.printStackTrace();
        }
        
        
        return "Maybe you want to check that again";
        
    }
    
    public String deleteCustomer(Users customer) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement  stm = null;
        ResultSet rs = null;

        try {

            String sql = "DELETE FROM USERS WHERE EMAIL = ?";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "pitiri");
            stm = con.prepareStatement(sql);
            stm.setString(1,customer.getEmail() );
            
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
    
    public String updateCustomer(Users customer) throws ClassNotFoundException {
        
        Connection con = null;
        PreparedStatement  stm = null;
        ResultSet rs = null;

        try {

            String sql = "UPDATE USERS SET FULLNAME=?,USERNAME=? WHERE EMAIL=?";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "pitiri");
            stm = con.prepareStatement(sql);
            
            stm.setString(1,customer.getFirstName());
            stm.setString(2,customer.getLastName());
            stm.setString(3,customer.getEmail());
            
            int row = stm.executeUpdate();
            System.out.println(row);

           
            
            con.close();
            
            return "OK";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Oops.. Something went wrong there..!";  // On failure, send a message from here.
    }
    
    private void deleteFromCustomerTable(Users customer) throws ClassNotFoundException
    {
        Connection con = null;
        PreparedStatement  stm = null;

        try {
            String sql = "DELETE FROM Customer WHERE EMAIL = ?";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "pitiri");
            stm = con.prepareStatement(sql);
            stm.setString(1,customer.getEmail() );
            
            int row = stm.executeUpdate();
            System.out.println(row);
            con.close();
            
            System.out.println("Removed from customer Table");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}
