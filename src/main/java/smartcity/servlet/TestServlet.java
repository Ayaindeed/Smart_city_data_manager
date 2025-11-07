package smartcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Servlet Test
 * ------------
 * Contrôleur de diagnostic et test du déploiement.
 * Page simple pour vérifier le bon fonctionnement de l'application.
 *
 * Mapping : "/test"
 */
@WebServlet(name = "TestServlet", urlPatterns = {"/test"})
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>SmartCity Test Page</h1>");
        out.println("<p>If you see this, the app is deployed correctly!</p>");
        out.println("<p>Context Path: " + req.getContextPath() + "</p>");
        out.println("<p>Servlet Path: " + req.getServletPath() + "</p>");
        out.println("<a href='" + req.getContextPath() + "/index.jsp'>Go to Index</a>");
        out.println("</body></html>");
    }
}