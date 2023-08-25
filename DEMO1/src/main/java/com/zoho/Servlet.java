package com.zoho;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class Servlet
 */

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

		//response.getWriter().print("printed");
		
		int order_id = Integer.parseInt(request.getParameter("orderid"));
		String foodItem = request.getParameter("foodItem");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ps", "root", "root");
            
            PreparedStatement insertStatement = con.prepareStatement(
                    "INSERT INTO Food (order_id,food_item,quantity) VALUES (?, ?,?)");
            insertStatement.setInt(1, order_id);
            
            insertStatement.setString(2, foodItem);
            insertStatement.setInt(3, quantity);
           
            int insertResult = insertStatement.executeUpdate();

            PrintWriter out = response.getWriter();
            if (insertResult > 0) {
                out.print("You have successfully ordered");
            } else {
                out.print("Order failed");
            }

          

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		try {
	        

	        Class.forName("com.mysql.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ps", "root", "root");

	        Statement statement = con.createStatement();
	        ResultSet resultSet = statement.executeQuery("SELECT * FROM Menu");

	        List<Map<String, Object>> menuList = new ArrayList<>();

	        while (resultSet.next()) {
	            Map<String, Object> menuItem = new HashMap<>();
	            menuItem.put("id", resultSet.getInt("id"));
	            menuItem.put("name", resultSet.getString("name"));
	            menuItem.put("price", resultSet.getDouble("price"));
	            // Add more properties as needed

	            menuList.add(menuItem);
	        }

	        con.close();

	        // Convert the menuList to JSON format
	        String jsonMenu = new Gson().toJson(menuList);

	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out = response.getWriter();
	        out.print(jsonMenu);

	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
           
            request.getSession().setAttribute("orderid", "");
            request.getSession().setAttribute("foodItem", "");
            request.getSession().setAttribute("quantity", "");
            
            response.getWriter().print("Form reset successfully");
        } catch (Exception e) {
            response.getWriter().print("Error resetting form: " + e.getMessage());
        }
    }
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int orderID = Integer.parseInt(request.getParameter("orderid"));
            response.getWriter().print("Order deleted successfully");
        } catch (Exception e) {
            response.getWriter().print("Error deleting order: " + e.getMessage());
        }
    }
	}
	