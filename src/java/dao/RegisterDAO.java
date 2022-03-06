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
import models.RegisterCustomers;
import models.CustomerLogin;
import java.sql.ResultSet;
/**
 *
 * @author ahsan_764ad7k
 */
public class RegisterDAO {
     public String registerUser(RegisterCustomers register) throws ClassNotFoundException {
        String fullName = register.getFullName();
        String email = register.getEmail();
        String userName = register.getUsername();
        String password = register.getPassword();
        String address = register.getAddress();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "pitiri");
            String sql = "insert into Customer(Name,Address,email) values (?,?,?)"; 
            preparedStatement = connection.prepareStatement(sql); 
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, email);
            
        int i = preparedStatement.executeUpdate();

            if (i != 0) //Just to ensure data has been inserted into the database
            {
                System.out.println("SUCCESS added to customer table");
            }
            
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "pitiri");
            String query = "insert into users(fullName,email,userName,password,userRole) values (?,?,?,?,?)"; //Insert user details into the table 'USERS'
            preparedStatement = connection.prepareStatement(query); //Making use of prepared statements here to insert bunch of data
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, userName);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, "Customer");

            int i = preparedStatement.executeUpdate();

            if (i != 0) //Just to ensure data has been inserted into the database
            {
                ResultSet rs= null;
                setCustomerID(connection,preparedStatement,rs,register,email);
                return "SUCCESS";
            }
            
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Oops.. Something went wrong there..!";  // On failure, send a message from here.
    }
    
    private void setCustomerID(Connection connection,PreparedStatement preparedStatement, ResultSet resultSet,RegisterCustomers register,String email) throws ClassNotFoundException
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "pitiri");
            String query = "SELECT * FROM Customer WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                register.setCustomerID(resultSet.getInt("id"));
            }
            
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
