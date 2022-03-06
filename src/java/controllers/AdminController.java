 package controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dao.UserDAO;
import dao.TripDAO;
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
import models.Payment;
import models.Trip;
import models.User;

/**
 *
 * @author Hisan
 */
@WebServlet(urlPatterns = {"/admin"})
public class AdminController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        UserDAO userdao = new UserDAO();
        request.setAttribute("nonAdmins", userdao.listOfNonAdmins());
        
        TripDAO tripdao = new TripDAO();
        request.setAttribute("trips", tripdao.listOfTrips());
                        
        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String remove = request.getParameter("remove");
        if (remove != null && !remove.isEmpty()) {
            UserDAO userdao = new UserDAO();
            userdao.deleteUser(Integer.parseInt(remove));
            
            response.sendRedirect("admin");
        }
        else {
            
        }
        
    }

}
