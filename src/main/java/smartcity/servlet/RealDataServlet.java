package smartcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import smartcity.service.RealDataService;
import smartcity.model.Sensor;
import smartcity.model.Measurement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * RealDataServlet
 * ---------------
 * Servlet pour tester l'intégration complète de données réelles
 * Accessible à /real-data-demo
 */
@WebServlet("/real-data-demo")
public class RealDataServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<title>SmartCity - Démonstration Données Réelles</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 40px; background: #f0f0f0; }");
        out.println(".success { color: green; } .error { color: red; }");
        out.println(".measurement { background: white; padding: 15px; margin: 10px 0; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }");
        out.println(".type-meteo { border-left: 5px solid #4ECDC4; }");
        out.println(".type-pollution { border-left: 5px solid #FF6B6B; }");
        out.println(".type-bruit { border-left: 5px solid #FFE66D; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<h1>🎯 SmartCity - Démonstration Intégration Données Réelles</h1>");
        out.println("<h2>📋 Ce que fait cette démonstration :</h2>");
        out.println("<ul>");
        out.println("<li>✅ <strong>GARDE</strong> votre système existant (ajout manuel + génération)</li>");
        out.println("<li>➕ <strong>AJOUTE</strong> de vraies données d'APIs dans vos modèles Sensor/Measurement</li>");
        out.println("<li>🌤️ <strong>Météo :</strong> Température, Humidité, Vent</li>");
        out.println("<li>🏭 <strong>Pollution :</strong> PM2.5, NO2, Ozone, AQI</li>");
        out.println("<li>🔊 <strong>Bruit :</strong> Niveaux sonores urbains</li>");
        out.println("</ul>");
        
        try {
            RealDataService realDataService = new RealDataService();
            
            // Test pour Paris
            out.println("<h2>🔄 Collecte en cours pour Paris...</h2>");
            List<Measurement> measurements = realDataService.fetchAllRealData("Paris", "Centre-Ville");
            
            if (measurements.isEmpty()) {
                out.println("<div class='error'>❌ Aucune mesure collectée. Vérifiez les APIs.</div>");
            } else {
                out.println("<div class='success'>✅ " + measurements.size() + " mesures collectées et sauvegardées dans votre base !</div>");
                
                out.println("<h3>📊 Détail des mesures ajoutées à vos modèles :</h3>");
                
                for (Measurement m : measurements) {
                    String cssClass = "measurement";
                    if ("Météo".equals(m.getSensor().getType())) {
                        cssClass += " type-meteo";
                    } else if ("Pollution".equals(m.getSensor().getType())) {
                        cssClass += " type-pollution";
                    } else if ("Bruit".equals(m.getSensor().getType())) {
                        cssClass += " type-bruit";
                    }
                    
                    out.println("<div class='" + cssClass + "'>");
                    out.println("<strong>🔢 Valeur:</strong> " + m.getValue() + " ");
                    out.println("<strong>📊 Type Mesure:</strong> " + m.getTypeMeasure() + " ");
                    out.println("<strong>📡 Capteur:</strong> " + m.getSensor().getName() + " ");
                    out.println("<strong>🏷️ Type Capteur:</strong> " + m.getSensor().getType() + " ");
                    out.println("<strong>📍 Zone:</strong> " + m.getSensor().getZone());
                    out.println("</div>");
                }
                
                out.println("<h3>💡 Comment ça marche :</h3>");
                out.println("<ul>");
                out.println("<li><strong>Vos modèles restent identiques</strong> - Sensor(name, type, zone, active) + Measurement(value, typeMeasure, timestamp)</li>");
                out.println("<li><strong>Les APIs alimentent automatiquement</strong> vos tables avec de vraies données</li>");
                out.println("<li><strong>Vous gardez</strong> l'ajout manuel ET la génération de données test</li>");
                out.println("<li><strong>Vous visualisez tout</strong> dans vos pages sensors.jsp et measurements.jsp existantes</li>");
                out.println("</ul>");
            }
            
        } catch (Exception e) {
            out.println("<div class='error'>❌ Erreur: " + e.getMessage() + "</div>");
            e.printStackTrace();
        }
        
        out.println("<br><br>");
        out.println("<a href='/weather' style='padding: 10px 20px; background: #4ECDC4; color: white; text-decoration: none; border-radius: 5px;'>Interface Données Réelles</a> ");
        out.println("<a href='/sensors' style='padding: 10px 20px; background: #667eea; color: white; text-decoration: none; border-radius: 5px;'>Voir Tous Capteurs</a> ");
        out.println("<a href='/measurements' style='padding: 10px 20px; background: #FF6B6B; color: white; text-decoration: none; border-radius: 5px;'>Voir Toutes Mesures</a> ");
        out.println("<a href='/' style='padding: 10px 20px; background: #333; color: white; text-decoration: none; border-radius: 5px;'>Accueil</a>");
        
        out.println("</body></html>");
    }
}