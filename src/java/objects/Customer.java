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

                Users driver = new Users();
                driver.setFirstName(fullName);
                driver.setLastName(userName);
                driver.setEmail(email);
               
                customers.add(driver);
            }   
            
        con.close();
        
        return "HM";
        } catch (SQLException e) {
             e.printStackTrace();
        }
        
        
        return "Maybe you want to check that again";
        
    }
    
    
    
}
