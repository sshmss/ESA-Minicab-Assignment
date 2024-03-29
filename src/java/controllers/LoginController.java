package controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        // take username and password from index.html file
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        //connect to db
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "dJw3426A@");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "root");
            //Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minicab?useSSL=false", "root", "pitiri");

            Statement stm = con.createStatement();
            String sql = "select id, username, password, role from users where username='"+username+"' and password='"+password+"'";
            ResultSet rs = stm.executeQuery(sql);
            
            if (rs.next()) {
                HttpSession session = request.getSession(); //Creating a session
                session.setMaxInactiveInterval(20 * 60);
                session.setAttribute("role", rs.getString("role"));
                session.setAttribute("userId", rs.getInt("id"));
                session.setAttribute("username", username);
                response.sendRedirect(rs.getString("role"));
            } else {
                out.println("Username or password incorrect");
            }
            con.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
