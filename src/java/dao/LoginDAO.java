/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import models.CustomerLogin;


/**
 *
 * @author ahsan_764ad7k
 */
public class LoginDAO {
    public String authenticateUser(CustomerLogin login) throws ClassNotFoundException {
        String email = login.getEmail();
        String password = login.getPassword();

        Connection con = null;
        Statement stm = null;
        PreparedStatement prpStm = null;
        ResultSet rs = null;

        String emailDB = "";
        String passwordDB = "";
        String userNameDB = "";
        String roleDB = "";
        
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "pitiri");
            stm = con.createStatement();
            rs = stm.executeQuery("select email,password,userRole,userName from users");
            
            while (rs.next()) {
                emailDB = rs.getString("email");
                passwordDB = rs.getString("password");
                roleDB = rs.getString("userRole");
                userNameDB = rs.getString("userName");

                
                
                if (email.equals(emailDB) && password.equals(passwordDB) && roleDB.equals("Admin")) {
                    login.setUsername(userNameDB);
                    return "Admin_Role";
                } else if (email.equals(emailDB) && password.equals(passwordDB) && roleDB.equals("Driver")) {
                    login.setUsername(userNameDB);
                    return "Driver_Role";
                } else if (email.equals(emailDB) && password.equals(passwordDB) && roleDB.equals("Customer")) {
                    login.setUsername(userNameDB);
                    setCustomerID(con,prpStm,rs,login,email);
                    return "Customer_Role";
                }
            }
            
            con.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Invalid user credentials";
    }
    
    private void setCustomerID(Connection con,PreparedStatement preparedStatement, ResultSet resultSet,CustomerLogin login,String email) throws ClassNotFoundException
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "pitiri");
            String query = "SELECT * FROM Customer WHERE email = ?";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                login.setId(resultSet.getInt("id"));
            }
            
            con.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
