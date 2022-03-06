/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import models.RegisterDrivers;
import models.Drivers;
import models.Users;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author ahsan_764ad7k
 */
public class DriverDAO {
    public String createDriver(RegisterDrivers driverRegister) throws ClassNotFoundException {
        String fullName = driverRegister.getFullName();
        String email = driverRegister.getEmail();
        String userName = driverRegister.getUserName();
        String password = driverRegister.getPassword();

        Connection con = null;
        PreparedStatement stm = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            String sql = "insert into users(fullName,email,userName,password,userRole) values (?,?,?,?,?)"; 
            stm = con.prepareStatement(sql); 
            stm.setString(1, fullName);
            stm.setString(2, email);
            stm.setString(3, userName);
            stm.setString(4, password);
            stm.setString(5, "Driver");

            int i = stm.executeUpdate();

            if (i != 0) 
            {
                addToDriverTable(driverRegister);
                return "SUCCESS";
            }

            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Might wanna check that again";  
    }
    public String driversList(ArrayList<Users> drivers) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {

            String sql = "SELECT FULLNAME, USERNAME, EMAIL FROM USERS WHERE USERROLE = 'Driver'";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            
            while (rs.next()) {

                String fullName = rs.getString("FULLNAME");
                String userName = rs.getString("USERNAME");
                String email = rs.getString("EMAIL");

                Users driver = new Users();
                driver.setFirstName(fullName);
                driver.setLastName(userName);
                driver.setEmail(email);
               
                drivers.add(driver);
            }

            con.close();
            
            return "OK";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Oops.. Something went wrong there..!";  
    }
    
    public String deleteDriver(Users driver) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {

            String sql = "DELETE FROM USERS WHERE EMAIL = ?";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            stm = con.prepareStatement(sql);
            stm.setString(1,driver.getEmail() );
            
            int row = stm.executeUpdate();
            System.out.println(row);


            con.close();
            deleteFromDriverTable(driver);
            return "OK";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Oops.. Something went wrong there..!";  
    }
    
    public String updateDriver(Users driver) throws ClassNotFoundException {
        
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {

            String query = "UPDATE USERS SET FULLNAME=?,USERNAME=? WHERE EMAIL=?";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            stm = con.prepareStatement(query);
            
            stm.setString(1,driver.getFirstName());
            stm.setString(2,driver.getLastName());
            stm.setString(3,driver.getEmail());
            
            int row = stm.executeUpdate();
            System.out.println(row);

            
            
            con.close();
            
            return "OK";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Oops.. Something went wrong there..!";  // On failure, send a message from here.
    }
    
    public String specficDriverDetails(String email, Drivers dets) throws ClassNotFoundException
    {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
           
            String query = "SELECT * FROM Drivers WHERE email = ?";
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            stm = con.prepareStatement(query);
            stm.setString(1,email);
            rs = stm.executeQuery();
            
            if(rs.next())
            {
                String name = rs.getString("Name");
                String plateNumber = rs.getString("Registration");
                
                dets.setDriverLicense(plateNumber);
                dets.setName(name);
                dets.setEmail(email);
            }
            
            con.close();
            return "OK";

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "Oops.. Something went wrong there..!";
    }
    
    private void addToDriverTable(RegisterDrivers driverRegister) throws ClassNotFoundException
    {
        String fullName = driverRegister.getFullName();
        String email = driverRegister.getEmail();
        String pateNo = driverRegister.getRegistrationNumber();
        
        Connection con = null;
        PreparedStatement stm = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            String query = "insert into Drivers(email,Registration,Name) values (?,?,?)";
            stm = con.prepareStatement(query); //Making use of prepared statements here to insert bunch of data
            stm.setString(1,email);
            stm.setString(2,pateNo);
            stm.setString(3, fullName);
            
            
            int i = stm.executeUpdate();

            if (i != 0) 
            {
                System.out.println("Added to driver table");
            }

            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void deleteFromDriverTable(Users driver) throws ClassNotFoundException
    {
        Connection con = null;
        PreparedStatement stm = null;

        try {

            String query = "DELETE FROM Drivers WHERE EMAIL = ?";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            stm = con.prepareStatement(query);
            stm.setString(1,driver.getEmail());
            
            int row = stm.executeUpdate();
            System.out.println(row);
            
             con.close();
            
            System.out.println("Driver Removed form driver table");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
