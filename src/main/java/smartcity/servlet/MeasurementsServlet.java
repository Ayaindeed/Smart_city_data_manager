package smartcity.servlet;

import smartcity.dao.MeasurementDao;
import smartcity.dao.SensorDao;
import smartcity.model.Measurement;
import smartcity.model.Sensor;
import smartcity.service.RealDataService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Servlet Measurements
 * -------------------
 * Contrôleur de gestion des mesures de capteurs urbains.
 * Ajout de nouvelles mesures + statistiques temporelles et par type.
 *
 * Dépend de : MeasurementDao, SensorDao, measurements.jsp
 */
@WebServlet(name = "MeasurementsServlet", urlPatterns = {"/measurements"})
public class MeasurementsServlet extends HttpServlet {

    private final MeasurementDao measurementDao = new MeasurementDao();
    private final SensorDao sensorDao = new SensorDao();
    private final RealDataService realDataService = new RealDataService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("sensors", sensorDao.findAll());
        req.setAttribute("measurements", measurementDao.findAll());
        
        try {
            req.setAttribute("statsType", measurementDao.getStatsByType());
            req.setAttribute("statsTime", measurementDao.getStatsByTimeRange());
        } catch (Exception e) {
            // Si les tables n'existent pas encore, statistiques vides
            req.setAttribute("statsType", new java.util.ArrayList<>());
            req.setAttribute("statsTime", new java.util.ArrayList<>());
        }
        
        req.getRequestDispatcher("/pages/measurements.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        
        // Gestion collecte API
        if ("fetchAPI".equals(action)) {
            String city = req.getParameter("city");
            String zone = req.getParameter("zone");
            
            try {
                if (city == null || city.trim().isEmpty()) {
                    String[] cities = {"Paris", "London", "Beijing"};
                    String[] zones = {"Centre-Ville", "Downtown", "Centre"};
                    realDataService.collectRealDataForCities(cities, zones);
                    req.getSession().setAttribute("message", "✅ Mesures API collectées pour: " + String.join(", ", cities));
                } else {
                    if (zone == null || zone.trim().isEmpty()) zone = "Centre-Ville";
                    realDataService.fetchAllRealData(city.trim(), zone.trim());
                    req.getSession().setAttribute("message", "✅ Mesures API collectées pour " + city);
                }
            } catch (Exception e) {
                req.getSession().setAttribute("error", "Erreur collecte API: " + e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/measurements");
            return;
        }
        
        // Gestion ajout manuel de mesure
        String typeMeasure = req.getParameter("typeMeasure");
        String valueStr = req.getParameter("value");
        String sensorIdStr = req.getParameter("sensorId");

        try {
            if (typeMeasure == null || typeMeasure.trim().isEmpty()) {
                req.getSession().setAttribute("error", "Le type de mesure est requis");
                resp.sendRedirect(req.getContextPath() + "/measurements");
                return;
            }

            if (valueStr == null || valueStr.trim().isEmpty()) {
                req.getSession().setAttribute("error", "La valeur est requise");
                resp.sendRedirect(req.getContextPath() + "/measurements");
                return;
            }

            if (sensorIdStr == null || sensorIdStr.trim().isEmpty()) {
                req.getSession().setAttribute("error", "Le capteur est requis");
                resp.sendRedirect(req.getContextPath() + "/measurements");
                return;
            }

            double value = Double.parseDouble(valueStr);
            Long sensorId = Long.parseLong(sensorIdStr);
            Sensor sensor = sensorDao.findById(sensorId);

            if (sensor == null) {
                req.getSession().setAttribute("error", "Capteur non trouvé");
                resp.sendRedirect(req.getContextPath() + "/measurements");
                return;
            }

            Measurement m = new Measurement(LocalDateTime.now(), value, typeMeasure, sensor);
            measurementDao.save(m);
            
            req.getSession().setAttribute("message", "Mesure ajoutée avec succès");
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "Format de nombre invalide");
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Erreur lors de l'ajout de la mesure: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/measurements");
    }
}
