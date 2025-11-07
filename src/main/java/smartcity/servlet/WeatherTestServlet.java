package smartcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import smartcity.service.RealDataService;
import smartcity.model.Measurement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * WeatherTestServlet
 * ------------------
 * Quick test servlet to verify OpenWeatherMap API integration
 * Accessible at /weather-test
 */
@WebServlet("/weather-test")
public class WeatherTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Weather API Test</title></head><body>");
        out.println("<h1>🌤️ OpenWeatherMap API Test</h1>");
        
        try {
            RealDataService realDataService = new RealDataService();
            
            // Test fetching all real data for Paris
            out.println("<h2>Test Collecte Données Réelles - Paris...</h2>");
            List<Measurement> measurements = realDataService.fetchAllRealData("Paris", "Zone-Test");
            
            if (measurements.isEmpty()) {
                out.println("<p style='color:red;'>❌ Aucune mesure reçue. Vérifiez la clé API et la connexion.</p>");
            } else {
                out.println("<p style='color:green;'>✅ Succès! " + measurements.size() + " mesures de données réelles collectées:</p>");
                out.println("<ul>");
                for (Measurement m : measurements) {
                    out.println("<li><strong>" + m.getTypeMeasure() + ":</strong> " + m.getValue() + 
                              " (Type: " + m.getSensor().getType() + " - Capteur: " + m.getSensor().getName() + ")</li>");
                }
                out.println("</ul>");
            }
            
        } catch (Exception e) {
            out.println("<p style='color:red;'>❌ Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("<br><a href='/weather'>Go to Weather Dashboard</a>");
        out.println("<br><a href='/'>Back to Home</a>");
        out.println("</body></html>");
    }
}