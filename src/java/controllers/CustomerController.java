package controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dao.TripDAO;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Payment;
import models.User;
import models.Trip;


/**
 *
 * @author Hisan
 */
@WebServlet(urlPatterns = {"/customer"})
public class CustomerController extends HttpServlet {

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
        String cancel = request.getParameter("cancel");
        if (cancel != null && !cancel.isEmpty()) {
            TripDAO tripdao = new TripDAO();
            tripdao.cancelTrip(Integer.parseInt(cancel));
            
            response.sendRedirect("customer");
        }
        else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            // take username and password from index.html file
            String pickup = request.getParameter("pickup");
            String desti = request.getParameter("desti");
            String distance = request.getParameter("distance");
            String fixedPrice = request.getParameter("fixedPrice");
            String farePrice = request.getParameter("farePrice");
            String totalPrice = request.getParameter("totalPrice");


            //connect to db
            try {
                UserDAO dao = new UserDAO();
                User user = dao.getUserByUsername(request.getSession().getAttribute("username").toString());
                Trip trip = new Trip();
                System.out.println(pickup);
                trip.setPickupLoc(pickup);
                System.out.println(desti);
                trip.setDestinationLoc(desti);
                trip.setDistance(Float.parseFloat(distance));
                trip.setPassenger(user);

                Payment payment = new Payment();
                payment.setFixedAmt(Float.parseFloat(fixedPrice));
                payment.setFareAmt(Float.parseFloat(farePrice));
                payment.setTotalAmt(Float.parseFloat(totalPrice));

                trip.setPayment(payment);


                TripDAO tripDAO = new TripDAO();

                boolean res = tripDAO.addTrip(trip);
                if (res) {
                    response.sendRedirect("customer");
                }
                else {
                    out.println("Error occured in creatine user");
                }


            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        TripDAO tripdao = new TripDAO();
        UserDAO userdao = new UserDAO();
        User currentUser = null;
        System.out.println(request.getSession().getAttribute("userId"));
        try {
            currentUser = userdao.getUserById((int) request.getSession().getAttribute("userId"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("trips", tripdao.listOfTripsByCustomer(currentUser));
                        
        request.getRequestDispatcher("customer.jsp").forward(request, response);
    }
}
