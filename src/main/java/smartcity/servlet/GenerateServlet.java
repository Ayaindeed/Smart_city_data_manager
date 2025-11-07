package smartcity.servlet;

import smartcity.dao.JpaUtil;
import smartcity.dao.SensorDao;
import smartcity.model.Measurement;
import smartcity.model.Sensor;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;


/**
 * Servlet Generate
 * ----------------
 * Contrôleur de génération massive de données de test.
 * Appel DataGenerator pour insertion batch de milliers de mesures.
 *
 * Dépend de : DataGenerator
 */
@WebServlet(name = "GenerateServlet", urlPatterns = {"/generate"})
public class GenerateServlet extends HttpServlet {

    private final SensorDao sensorDao = new SensorDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        
        try {
            // Utiliser notre DataGenerator amélioré
            smartcity.util.DataGenerator.generateData();
            
            long duration = System.currentTimeMillis() - start;
            req.getSession().setAttribute("message", 
                "✅ Données de test générées en " + duration + " ms");
            
        } catch (Exception e) {
            req.getSession().setAttribute("error", 
                "❌ Erreur lors de la génération des données : " + e.getMessage());
        }
        
        resp.sendRedirect(req.getContextPath() + "/");
    }
}