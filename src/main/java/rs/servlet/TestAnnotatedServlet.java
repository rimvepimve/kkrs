package rs.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

@WebServlet(name = "TestAnnotatedServlet", urlPatterns = {"/rim","/rimlip","/rim/lip"})
public class TestAnnotatedServlet extends HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        System.out.println("The get method has been called!");
        response.setContentType("text/html");

        response.getWriter().println("<h1>Here is a testline</h1>");
        response.getWriter().println("<h2>Here is another testlie</h2>");
    }
}
