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

/**
 *
 * @author Hisan
 */
@WebServlet(urlPatterns = {"/driver"})
public class DriverController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        TripDAO tripdao = new TripDAO();
        request.setAttribute("trips", tripdao.listOfTrips());
                        
        request.getRequestDispatcher("driver.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String complete = request.getParameter("complete");
        if (complete != null && !complete.isEmpty()) {
            TripDAO tripdao = new TripDAO();
            tripdao.finishTrip(Integer.parseInt(complete));
            response.sendRedirect("driver");
        }
        else {
            TripDAO tripdao = new TripDAO();
            tripdao.updateTripStatus(Integer.parseInt(request.getParameter("tripId")), (int) request.getSession().getAttribute("userId"));
            request.setAttribute("trips", tripdao.listOfTrips());

            request.getRequestDispatcher("driver.jsp").forward(request, response);
        }
        
    }

}
