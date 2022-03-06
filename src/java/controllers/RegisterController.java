package controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import models.User;

/**
 *
 * @author Hisan
 */
@WebServlet(urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // take username and password from index.html file
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = "customer";
        if (request.getParameter("role") != null && !request.getParameter("role").isEmpty()) {
            role = request.getParameter("role");
        }
        if (!confirmPassword.equals(password)) {
            out.println("Password doesnt match");
            return;
        }
        
        //connect to db
        try {
            User user = new User(null, username, password, role);
            UserDAO userDAO = new UserDAO();
            boolean res = userDAO.addCustomer(user);
            if (res) {
                response.sendRedirect("index.html");
            }
            else {
                out.println("Error occured in creatine user");
            }
            
            
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}
