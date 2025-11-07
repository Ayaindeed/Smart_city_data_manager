package smartcity.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import smartcity.dao.SensorDao;
import smartcity.dao.MeasurementDao;
import smartcity.model.Sensor;
import smartcity.model.Measurement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * RealDataService
 * ---------------
 * Service pour intégrer des données RÉELLES d'APIs dans vos modèles SmartCity existants
 * 
 * 🎯 OBJECTIF: Alimenter automatiquement vos Sensor et Measurement avec de vraies données
 * 📊 TYPES DE DONNÉES: Pollution, Météo, Bruit, Trafic
 * 
 * ✅ GARDE: L'ajout manuel existant + génération de données test
 * ➕ AJOUTE: Données réelles d'APIs gratuites
 */
public class RealDataService {
    
    // 🔑 Clés API
    private static final String OPENWEATHER_API_KEY = "f694a8fee5524a79aacb88ad2299255d";
    private static final String WAQI_API_KEY = "demo"; // Remplacer par vraie clé sur waqi.info
    
    // 🌐 URLs API
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String AIR_QUALITY_URL = "https://api.waqi.info/feed";
    
    private final SensorDao sensorDao;
    private final MeasurementDao measurementDao;
    private final ObjectMapper objectMapper;
    
    public RealDataService() {
        this.sensorDao = new SensorDao();
        this.measurementDao = new MeasurementDao();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 🌤️ MÉTÉO: Récupère données météo et les ajoute à vos capteurs existants
     */
    public List<Measurement> fetchWeatherData(String cityName, String zone) {
        List<Measurement> measurements = new ArrayList<>();
        
        try {
            String urlString = WEATHER_URL + "?q=" + cityName + "&appid=" + OPENWEATHER_API_KEY + "&units=metric";
            String jsonResponse = makeHttpRequest(urlString);
            
            if (jsonResponse != null) {
                JsonNode weatherData = objectMapper.readTree(jsonResponse);
                measurements.addAll(createWeatherMeasurements(weatherData, cityName, zone));
            }
        } catch (Exception e) {
            System.err.println("Erreur météo: " + e.getMessage());
        }
        
        return measurements;
    }
    
    /**
     * 🏭 POLLUTION: Récupère données de qualité de l'air (VRAIES données pollution !)
     */
    public List<Measurement> fetchPollutionData(String cityName, String zone) {
        List<Measurement> measurements = new ArrayList<>();
        
        try {
            // API World Air Quality Index (GRATUIT)
            String urlString = AIR_QUALITY_URL + "/" + cityName + "/?token=" + WAQI_API_KEY;
            String jsonResponse = makeHttpRequest(urlString);
            
            if (jsonResponse != null) {
                JsonNode pollutionData = objectMapper.readTree(jsonResponse);
                measurements.addAll(createPollutionMeasurements(pollutionData, cityName, zone));
            }
        } catch (Exception e) {
            System.err.println("Erreur pollution: " + e.getMessage());
        }
        
        return measurements;
    }
    
    /**
     * 🚦 DONNÉES COMPLÈTES: Récupère TOUTES les données réelles pour une ville
     */
    public List<Measurement> fetchAllRealData(String cityName, String zone) {
        List<Measurement> allMeasurements = new ArrayList<>();
        
        // Météo
        allMeasurements.addAll(fetchWeatherData(cityName, zone));
        
        // Pollution
        allMeasurements.addAll(fetchPollutionData(cityName, zone));
        
        // Simulation bruit urbain (basé sur population/trafic)
        allMeasurements.addAll(generateUrbanNoise(cityName, zone));
        
        return allMeasurements;
    }
    
    /**
     * 🌡️ Crée des mesures MÉTÉO dans vos modèles existants
     */
    private List<Measurement> createWeatherMeasurements(JsonNode weatherData, String cityName, String zone) {
        List<Measurement> measurements = new ArrayList<>();
        LocalDateTime timestamp = LocalDateTime.now();
        
        try {
            JsonNode main = weatherData.get("main");
            JsonNode wind = weatherData.get("wind");
            
            // 🌡️ Température - TYPE: Météo
            if (main.has("temp")) {
                Sensor tempSensor = getOrCreateSensor("Capteur-Temp-" + cityName, "Météo", zone);
                Measurement tempMeasurement = new Measurement(timestamp, main.get("temp").asDouble(), "Température", tempSensor);
                measurementDao.save(tempMeasurement);
                measurements.add(tempMeasurement);
            }
            
            // 💧 Humidité - TYPE: Météo  
            if (main.has("humidity")) {
                Sensor humiditySensor = getOrCreateSensor("Capteur-Humidity-" + cityName, "Météo", zone);
                Measurement humidityMeasurement = new Measurement(timestamp, main.get("humidity").asDouble(), "Humidité", humiditySensor);
                measurementDao.save(humidityMeasurement);
                measurements.add(humidityMeasurement);
            }
            
            // 🌬️ Vent - TYPE: Météo
            if (wind != null && wind.has("speed")) {
                Sensor windSensor = getOrCreateSensor("Capteur-Vent-" + cityName, "Météo", zone);
                Measurement windMeasurement = new Measurement(timestamp, wind.get("speed").asDouble(), "VitesseVent", windSensor);
                measurementDao.save(windMeasurement);
                measurements.add(windMeasurement);
            }
            
        } catch (Exception e) {
            System.err.println("Erreur création mesures météo: " + e.getMessage());
        }
        
        return measurements;
    }
    
    /**
     * 🏭 Crée des mesures POLLUTION dans vos modèles existants  
     */
    private List<Measurement> createPollutionMeasurements(JsonNode pollutionData, String cityName, String zone) {
        List<Measurement> measurements = new ArrayList<>();
        LocalDateTime timestamp = LocalDateTime.now();
        
        try {
            if (pollutionData.has("data") && pollutionData.get("status").asText().equals("ok")) {
                JsonNode data = pollutionData.get("data");
                
                // 🏭 Indice AQI global - TYPE: Pollution
                if (data.has("aqi")) {
                    Sensor aqiSensor = getOrCreateSensor("Capteur-AQI-" + cityName, "Pollution", zone);
                    Measurement aqiMeasurement = new Measurement(timestamp, data.get("aqi").asDouble(), "IndiceQualitéAir", aqiSensor);
                    measurementDao.save(aqiMeasurement);
                    measurements.add(aqiMeasurement);
                }
                
                // 🚗 Polluants spécifiques - TYPE: Pollution
                if (data.has("iaqi")) {
                    JsonNode pollutants = data.get("iaqi");
                    
                    // PM2.5 (particules fines)
                    if (pollutants.has("pm25")) {
                        Sensor pm25Sensor = getOrCreateSensor("Capteur-PM25-" + cityName, "Pollution", zone);
                        Measurement pm25Measurement = new Measurement(timestamp, pollutants.get("pm25").get("v").asDouble(), "PM2.5", pm25Sensor);
                        measurementDao.save(pm25Measurement);
                        measurements.add(pm25Measurement);
                    }
                    
                    // NO2 (dioxyde d'azote)
                    if (pollutants.has("no2")) {
                        Sensor no2Sensor = getOrCreateSensor("Capteur-NO2-" + cityName, "Pollution", zone);
                        Measurement no2Measurement = new Measurement(timestamp, pollutants.get("no2").get("v").asDouble(), "NO2", no2Sensor);
                        measurementDao.save(no2Measurement);
                        measurements.add(no2Measurement);
                    }
                    
                    // O3 (ozone)
                    if (pollutants.has("o3")) {
                        Sensor o3Sensor = getOrCreateSensor("Capteur-O3-" + cityName, "Pollution", zone);
                        Measurement o3Measurement = new Measurement(timestamp, pollutants.get("o3").get("v").asDouble(), "Ozone", o3Sensor);
                        measurementDao.save(o3Measurement);
                        measurements.add(o3Measurement);
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erreur création mesures pollution: " + e.getMessage());
        }
        
        return measurements;
    }
    
    /**
     * 🔊 Génère des mesures de BRUIT urbain réalistes
     */
    private List<Measurement> generateUrbanNoise(String cityName, String zone) {
        List<Measurement> measurements = new ArrayList<>();
        LocalDateTime timestamp = LocalDateTime.now();
        
        try {
            // Simulation réaliste basée sur l'heure et le type de zone
            double baseNoise = getBaseNoiseLevel(zone);
            double timeMultiplier = getTimeMultiplier();
            double noiseLevel = baseNoise * timeMultiplier + (Math.random() * 10 - 5); // +/- 5dB variation
            
            Sensor noiseSensor = getOrCreateSensor("Capteur-Bruit-" + cityName, "Bruit", zone);
            Measurement noiseMeasurement = new Measurement(timestamp, Math.round(noiseLevel * 10.0) / 10.0, "Décibels", noiseSensor);
            measurementDao.save(noiseMeasurement);
            measurements.add(noiseMeasurement);
            
        } catch (Exception e) {
            System.err.println("Erreur création mesures bruit: " + e.getMessage());
        }
        
        return measurements;
    }
    
    private double getBaseNoiseLevel(String zone) {
        if (zone.toLowerCase().contains("centre") || zone.toLowerCase().contains("downtown")) {
            return 70.0; // Zone urbaine dense
        } else if (zone.toLowerCase().contains("industriel") || zone.toLowerCase().contains("industrial")) {
            return 75.0; // Zone industrielle
        } else if (zone.toLowerCase().contains("résidentiel") || zone.toLowerCase().contains("residential")) {
            return 55.0; // Zone résidentielle
        } else {
            return 60.0; // Zone mixte
        }
    }
    
    private double getTimeMultiplier() {
        int hour = LocalDateTime.now().getHour();
        if (hour >= 7 && hour <= 9 || hour >= 17 && hour <= 19) {
            return 1.2; // Heures de pointe
        } else if (hour >= 22 || hour <= 6) {
            return 0.7; // Nuit
        } else {
            return 1.0; // Journée normale
        }
    }
    
    /**
     * Get existing sensor or create new one
     */
    private Sensor getOrCreateSensor(String sensorName, String type, String zone) {
        // Try to find existing sensor
        List<Sensor> existingSensors = sensorDao.findAll();
        for (Sensor sensor : existingSensors) {
            if (sensor.getName().equals(sensorName)) {
                return sensor;
            }
        }
        
        // Create new sensor if not found
        Sensor newSensor = new Sensor(sensorName, type, zone, true);
        sensorDao.save(newSensor);
        return newSensor;
    }
    
    /**
     * Make HTTP request to API
     */
    private String makeHttpRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                System.err.println("HTTP Error: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error making HTTP request: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 📊 Récupère tous les capteurs réels (Météo, Pollution, Bruit)
     */
    public List<Sensor> getRealDataSensors() {
        List<Sensor> allSensors = sensorDao.findAll();
        List<Sensor> realSensors = new ArrayList<>();
        
        for (Sensor sensor : allSensors) {
            String type = sensor.getType();
            if ("Météo".equals(type) || "Pollution".equals(type) || "Bruit".equals(type)) {
                realSensors.add(sensor);
            }
        }
        
        return realSensors;
    }
    
    /**
     * 🌍 Lance la collecte automatique pour plusieurs villes
     */
    public void collectRealDataForCities(String[] cities, String[] zones) {
        if (cities.length != zones.length) {
            System.err.println("Erreur: même nombre de villes et zones requis");
            return;
        }
        
        for (int i = 0; i < cities.length; i++) {
            System.out.println("🔄 Collecte données réelles pour: " + cities[i]);
            fetchAllRealData(cities[i], zones[i]);
        }
    }
    
    /**
     * 🎯 Méthodes de compatibilité avec l'ancien système
     */
    @Deprecated
    public List<Measurement> fetchAndStoreWeatherData(String cityName, String zone) {
        return fetchAllRealData(cityName, zone);
    }
    
    @Deprecated  
    public List<Sensor> getWeatherSensors() {
        return getRealDataSensors();
    }
    
    @Deprecated
    public void fetchWeatherForCities(String[] cities, String[] zones) {
        collectRealDataForCities(cities, zones);
    }
}