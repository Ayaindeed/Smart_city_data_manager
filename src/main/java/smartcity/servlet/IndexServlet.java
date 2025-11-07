package smartcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Index
 * -------------
 * Contrôleur de la page d'accueil de l'application SmartCity.
 * Redirection simple vers index.jsp.
 *
 * Mapping : "/" et ""
 */
@WebServlet(name = "IndexServlet", urlPatterns = {"", "/"})
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward to index.jsp
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}