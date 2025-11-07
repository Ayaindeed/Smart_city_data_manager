package smartcity.servlet;

import smartcity.dao.SensorDao;
import smartcity.model.Sensor;
import smartcity.service.RealDataService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet Sensors
 * ---------------
 * Contrôleur de gestion des capteurs urbains (IoT sensors).
 * CRUD complet + affichage statistiques agrégées par zone.
 * Dépend de : SensorDao, sensors.jsp
 */
@WebServlet(name = "SensorsServlet", urlPatterns = {"/sensors"})
public class SensorsServlet extends HttpServlet {

    private final SensorDao sensorDao = new SensorDao();
    private final RealDataService realDataService = new RealDataService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Sensor> sensors = sensorDao.findAll();
        req.setAttribute("sensors", sensors);
        
        try {
            req.setAttribute("sensorStats", sensorDao.getSensorStats());
        } catch (Exception e) {
            // Si les tables n'existent pas encore, statistiques vides
            req.setAttribute("sensorStats", new java.util.ArrayList<>());
        }
        
        req.getRequestDispatcher("/pages/sensors.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        
        try {
            if ("fetchAPI".equals(action)) {
                // Nouvelle action : collecte données API
                String city = req.getParameter("city");
                String zone = req.getParameter("zone");
                
                if (city == null || city.trim().isEmpty()) {
                    // Villes par défaut
                    String[] cities = {"Paris", "London", "Beijing"};
                    String[] zones = {"Centre-Ville", "Downtown", "Centre"};
                    realDataService.collectRealDataForCities(cities, zones);
                    req.getSession().setAttribute("message", "✅ Données API collectées pour: " + String.join(", ", cities));
                } else {
                    if (zone == null || zone.trim().isEmpty()) zone = "Centre-Ville";
                    realDataService.fetchAllRealData(city.trim(), zone.trim());
                    req.getSession().setAttribute("message", "✅ Données API collectées pour " + city);
                }
            } else if (action == null) {
                // Create new sensor
                String name = req.getParameter("name");
                String type = req.getParameter("type");
                String zone = req.getParameter("zone");

                if (name != null && type != null) {
                    Sensor s = new Sensor();
                    s.setName(name);
                    s.setType(type);
                    s.setZone(zone);
                    s.setActive(true);
                    sensorDao.save(s);
                    req.getSession().setAttribute("message", "Capteur créé avec succès");
                }
            } else {
                // Handle other actions
                String idStr = req.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    Long id = Long.parseLong(idStr);
                    
                    switch (action) {
                        case "toggle":
                            sensorDao.toggleActive(id);
                            req.getSession().setAttribute("message", "État du capteur modifié");
                            break;
                        case "delete":
                            sensorDao.delete(id);
                            req.getSession().setAttribute("message", "Capteur supprimé avec succès");
                            break;
                        default:
                            req.getSession().setAttribute("error", "Action non reconnue");
                    }
                }
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Erreur: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/sensors");
    }
}
