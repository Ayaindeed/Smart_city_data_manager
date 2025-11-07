package smartcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import smartcity.service.RealDataService;
import smartcity.model.Sensor;
import smartcity.model.Measurement;
import smartcity.dao.SensorDao;
import smartcity.dao.MeasurementDao;

import java.io.IOException;
import java.util.List;

/**
 * WeatherServlet
 * --------------
 * Handles weather-related requests including:
 * - Fetching weather data from OpenWeatherMap API
 * - Displaying weather sensors and measurements
 * - Managing weather data integration
 */
@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {
    
    private RealDataService realDataService;
    private SensorDao sensorDao;
    private MeasurementDao measurementDao;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.realDataService = new RealDataService();
        this.sensorDao = new SensorDao();
        this.measurementDao = new MeasurementDao();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "fetch":
                handleFetchWeather(req, resp);
                break;
            case "list":
            default:
                handleListWeather(req, resp);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
    /**
     * Fetch weather data for specified cities
     */
    private void handleFetchWeather(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String city = req.getParameter("city");
        String zone = req.getParameter("zone");
        
        if (city == null || city.trim().isEmpty()) {
            // Villes par défaut avec données réelles complètes
            String[] cities = {"Paris", "London", "Beijing", "Tokyo"};
            String[] zones = {"Centre-Ville", "Downtown", "Centre", "Shibuya"};
            realDataService.collectRealDataForCities(cities, zones);
            req.setAttribute("message", "✅ Données réelles collectées pour: " + String.join(", ", cities) + " (Météo + Pollution + Bruit)");
        } else {
            if (zone == null || zone.trim().isEmpty()) {
                zone = "Centre-Ville";
            }
            List<Measurement> measurements = realDataService.fetchAllRealData(city.trim(), zone.trim());
            req.setAttribute("message", "✅ Données collectées pour " + city + " - " + measurements.size() + " nouvelles mesures ajoutées");
        }
        
        // Redirect to list view
        resp.sendRedirect("weather?action=list");
    }
    
    /**
     * List all weather sensors and their recent measurements
     */
    private void handleListWeather(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Récupère TOUS les capteurs de données réelles
            List<Sensor> realSensors = realDataService.getRealDataSensors();
            req.setAttribute("realSensors", realSensors);
            
            // Récupère les mesures récentes (Météo + Pollution + Bruit)
            List<Measurement> recentRealMeasurements = measurementDao.findAll();
            // Filtre pour ne garder que les données réelles
            recentRealMeasurements.removeIf(m -> {
                String type = m.getSensor().getType();
                return !("Météo".equals(type) || "Pollution".equals(type) || "Bruit".equals(type));
            });
            // Limite aux 50 dernières
            if (recentRealMeasurements.size() > 50) {
                recentRealMeasurements = recentRealMeasurements.subList(0, 50);
            }
            req.setAttribute("realMeasurements", recentRealMeasurements);
            
            // Statistiques données réelles
            if (!realSensors.isEmpty()) {
                req.setAttribute("totalRealSensors", realSensors.size());
                req.setAttribute("totalRealMeasurements", recentRealMeasurements.size());
                
                // Statistiques par type
                long meteoCount = realSensors.stream().filter(s -> "Météo".equals(s.getType())).count();
                long pollutionCount = realSensors.stream().filter(s -> "Pollution".equals(s.getType())).count();
                long noiseCount = realSensors.stream().filter(s -> "Bruit".equals(s.getType())).count();
                
                req.setAttribute("meteoSensors", meteoCount);
                req.setAttribute("pollutionSensors", pollutionCount);
                req.setAttribute("noiseSensors", noiseCount);
            }
            
            // Forward to weather JSP
            req.getRequestDispatcher("/pages/weather.jsp").forward(req, resp);
            
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error loading weather data: " + e.getMessage());
            req.getRequestDispatcher("/pages/weather.jsp").forward(req, resp);
        }
    }
}