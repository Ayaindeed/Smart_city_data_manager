<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SmartCity - Weather Data</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1400px;
            margin: 0 auto;
        }

        .header {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
        }

        .header h1 {
            color: white;
            font-size: 2.5em;
            text-align: center;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
        }

        .header p {
            color: rgba(255, 255, 255, 0.9);
            text-align: center;
            font-size: 1.1em;
        }

        .actions {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 30px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
        }

        .actions h2 {
            color: white;
            margin-bottom: 20px;
            font-size: 1.5em;
        }

        .fetch-form {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
            align-items: end;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            min-width: 200px;
        }

        .form-group label {
            color: white;
            margin-bottom: 5px;
            font-weight: 500;
        }

        .form-group input {
            padding: 12px;
            border: none;
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.9);
            font-size: 1em;
            transition: all 0.3s ease;
        }

        .form-group input:focus {
            outline: none;
            background: white;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }

        .btn {
            padding: 12px 25px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1em;
            font-weight: 500;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.3s ease;
            text-align: center;
        }

        .btn-primary {
            background: linear-gradient(45deg, #FF6B6B, #4ECDC4);
            color: white;
            border: 2px solid transparent;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(255, 107, 107, 0.4);
        }

        .btn-secondary {
            background: rgba(255, 255, 255, 0.2);
            color: white;
            border: 2px solid rgba(255, 255, 255, 0.3);
        }

        .btn-secondary:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-2px);
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 25px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
            text-align: center;
            transition: transform 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-5px);
        }

        .stat-card h3 {
            color: white;
            font-size: 2.5em;
            margin-bottom: 10px;
        }

        .stat-card p {
            color: rgba(255, 255, 255, 0.8);
            font-size: 1.1em;
        }

        .content-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
        }

        .section {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 25px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
        }

        .section h2 {
            color: white;
            margin-bottom: 20px;
            font-size: 1.5em;
            border-bottom: 2px solid rgba(255, 255, 255, 0.2);
            padding-bottom: 10px;
        }

        .sensor-list, .measurement-list {
            max-height: 400px;
            overflow-y: auto;
            padding-right: 10px;
        }

        .sensor-item, .measurement-item {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 10px;
            border: 1px solid rgba(255, 255, 255, 0.1);
            transition: all 0.3s ease;
        }

        .sensor-item:hover, .measurement-item:hover {
            background: rgba(255, 255, 255, 0.2);
            transform: translateX(5px);
        }

        .sensor-item h4, .measurement-item h4 {
            color: white;
            margin-bottom: 8px;
            font-size: 1.1em;
        }

        .sensor-item p, .measurement-item p {
            color: rgba(255, 255, 255, 0.8);
            font-size: 0.9em;
            margin-bottom: 3px;
        }

        .sensor-status {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.8em;
            font-weight: 500;
            margin-top: 5px;
        }

        .status-active {
            background: rgba(46, 204, 113, 0.3);
            color: #2ecc71;
            border: 1px solid rgba(46, 204, 113, 0.5);
        }

        .status-inactive {
            background: rgba(231, 76, 60, 0.3);
            color: #e74c3c;
            border: 1px solid rgba(231, 76, 60, 0.5);
        }

        .measurement-value {
            font-weight: bold;
            color: #4ECDC4;
            font-size: 1.1em;
        }

        .measurement-type {
            background: rgba(255, 255, 255, 0.2);
            padding: 2px 8px;
            border-radius: 12px;
            font-size: 0.8em;
            color: white;
        }

        .message {
            background: rgba(46, 204, 113, 0.2);
            color: white;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border: 1px solid rgba(46, 204, 113, 0.4);
        }

        .error {
            background: rgba(231, 76, 60, 0.2);
            color: white;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border: 1px solid rgba(231, 76, 60, 0.4);
        }

        .nav-links {
            text-align: center;
            margin-bottom: 20px;
        }

        .nav-links a {
            color: white;
            text-decoration: none;
            margin: 0 15px;
            padding: 8px 16px;
            border-radius: 20px;
            background: rgba(255, 255, 255, 0.1);
            transition: all 0.3s ease;
        }

        .nav-links a:hover {
            background: rgba(255, 255, 255, 0.2);
            transform: translateY(-2px);
        }

        @media (max-width: 768px) {
            .content-grid {
                grid-template-columns: 1fr;
            }
            
            .fetch-form {
                flex-direction: column;
            }
            
            .form-group {
                min-width: 100%;
            }
        }

        /* Scrollbar styling */
        ::-webkit-scrollbar {
            width: 8px;
        }

        ::-webkit-scrollbar-track {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 4px;
        }

        ::-webkit-scrollbar-thumb {
            background: rgba(255, 255, 255, 0.3);
            border-radius: 4px;
        }

        ::-webkit-scrollbar-thumb:hover {
            background: rgba(255, 255, 255, 0.5);
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Navigation -->
        <div class="nav-links">
            <a href="/">🏠 Home</a>
            <a href="/sensors">📡 Sensors</a>
            <a href="/measurements">📊 Measurements</a>
            <a href="/weather">🌤️ Weather</a>
        </div>

        <!-- Header -->
        <div class="header">
            <h1>📊 SmartCity - Données Réelles</h1>
            <p>🌤️ Météo • 🏭 Pollution • 🔊 Bruit urbain - Intégration API en temps réel</p>
        </div>

        <!-- Messages -->
        <c:if test="${not empty message}">
            <div class="message">
                <strong>✅ Success:</strong> ${message}
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="error">
                <strong>❌ Error:</strong> ${error}
            </div>
        </c:if>

        <!-- Collecte Données Réelles -->
        <div class="actions">
            <h2>🌍 Collecte de Données Réelles</h2>
            <p style="color: rgba(255,255,255,0.8); margin-bottom: 20px;">
                ✅ <strong>Garde votre système existant</strong> (ajout manuel + génération) <br>
                ➕ <strong>Ajoute des vraies données</strong> d'APIs gratuites dans vos modèles Sensor/Measurement
            </p>
            
            <form method="post" action="weather" class="fetch-form">
                <input type="hidden" name="action" value="fetch">
                
                <div class="form-group">
                    <label for="city">Nom de la ville:</label>
                    <input type="text" id="city" name="city" placeholder="ex: Paris, London, Beijing" />
                </div>
                
                <div class="form-group">
                    <label for="zone">Zone/Quartier:</label>
                    <input type="text" id="zone" name="zone" placeholder="ex: Centre-Ville, Industriel" />
                </div>
                
                <button type="submit" class="btn btn-primary">
                    📊 Collecter Données Complètes
                </button>
            </form>
            
            <div style="margin-top: 20px;">
                <form method="post" action="weather" style="display: inline;">
                    <input type="hidden" name="action" value="fetch">
                    <button type="submit" class="btn btn-secondary">
                        🌍 Villes Par Défaut
                    </button>
                </form>
                <small style="color: rgba(255,255,255,0.8); margin-left: 10px;">
                    (Paris, London, Beijing, Tokyo - Météo + Pollution + Bruit)
                </small>
            </div>
            
            <div style="margin-top: 15px; padding: 15px; background: rgba(255,255,255,0.1); border-radius: 8px;">
                <strong style="color: #4ECDC4;">Types de données collectées :</strong><br>
                🌤️ <strong>Météo:</strong> Température, Humidité, Vent<br>
                🏭 <strong>Pollution:</strong> PM2.5, NO2, Ozone, Indice AQI<br>
                🔊 <strong>Bruit:</strong> Niveaux sonores urbains (simulation réaliste)
            </div>
        </div>

        <!-- Statistics -->
        <c:if test="${not empty totalRealSensors}">
            <div class="stats-grid">
                <div class="stat-card">
                    <h3>${totalRealSensors}</h3>
                    <p>Capteurs API Réels</p>
                </div>
                <div class="stat-card">
                    <h3>${totalRealMeasurements}</h3>
                    <p>Mesures Récentes</p>
                </div>
                <div class="stat-card">
                    <h3>${meteoSensors}</h3>
                    <p>🌤️ Météo</p>
                </div>
                <div class="stat-card">
                    <h3>${pollutionSensors}</h3>
                    <p>🏭 Pollution</p>
                </div>
                <div class="stat-card">
                    <h3>${noiseSensors}</h3>
                    <p>🔊 Bruit</p>
                </div>
            </div>
        </c:if>

        <!-- Content Grid -->
        <div class="content-grid">
            <!-- Capteurs Données Réelles -->
            <div class="section">
                <h2>📡 Capteurs de Données Réelles</h2>
                <div class="sensor-list">
                    <c:choose>
                        <c:when test="${not empty realSensors}">
                            <c:forEach var="sensor" items="${realSensors}">
                                <div class="sensor-item">
                                    <h4>${sensor.name}</h4>
                                    <p><strong>Type:</strong> 
                                        <c:choose>
                                            <c:when test="${sensor.type == 'Météo'}">🌤️ ${sensor.type}</c:when>
                                            <c:when test="${sensor.type == 'Pollution'}">🏭 ${sensor.type}</c:when>
                                            <c:when test="${sensor.type == 'Bruit'}">🔊 ${sensor.type}</c:when>
                                            <c:otherwise>${sensor.type}</c:otherwise>
                                        </c:choose>
                                    </p>
                                    <p><strong>Zone:</strong> ${sensor.zone}</p>
                                    <p><strong>ID:</strong> #${sensor.id}</p>
                                    <span class="sensor-status ${sensor.active ? 'status-active' : 'status-inactive'}">
                                        ${sensor.active ? '🟢 Actif' : '🔴 Inactif'}
                                    </span>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="sensor-item">
                                <p style="text-align: center; color: rgba(255,255,255,0.6);">
                                    Aucun capteur de données réelles trouvé.<br>
                                    Cliquez sur "Collecter Données" pour créer des capteurs API.
                                </p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Mesures Récentes de Données Réelles -->
            <div class="section">
                <h2>📊 Mesures Récentes (APIs)</h2>
                <div class="measurement-list">
                    <c:choose>
                        <c:when test="${not empty realMeasurements}">
                            <c:forEach var="measurement" items="${realMeasurements}">
                                <div class="measurement-item">
                                    <h4>
                                        <span class="measurement-value">
                                            <fmt:formatNumber value="${measurement.value}" maxFractionDigits="1"/>
                                        </span>
                                        <span class="measurement-type">${measurement.typeMeasure}</span>
                                    </h4>
                                    <p><strong>Capteur:</strong> ${measurement.sensor.name}</p>
                                    <p><strong>Type:</strong> 
                                        <c:choose>
                                            <c:when test="${measurement.sensor.type == 'Météo'}">🌤️ ${measurement.sensor.type}</c:when>
                                            <c:when test="${measurement.sensor.type == 'Pollution'}">🏭 ${measurement.sensor.type}</c:when>
                                            <c:when test="${measurement.sensor.type == 'Bruit'}">🔊 ${measurement.sensor.type}</c:when>
                                            <c:otherwise>${measurement.sensor.type}</c:otherwise>
                                        </c:choose>
                                    </p>
                                    <p><strong>Zone:</strong> ${measurement.sensor.zone}</p>
                                    <p><strong>Heure:</strong> 
                                        <fmt:formatDate value="${measurement.timestamp}" pattern="dd/MM/yyyy HH:mm"/>
                                    </p>
                                    <p><strong>Unités:</strong> 
                                        <c:choose>
                                            <c:when test="${measurement.typeMeasure == 'Température'}">°C</c:when>
                                            <c:when test="${measurement.typeMeasure == 'Humidité'}">%</c:when>
                                            <c:when test="${measurement.typeMeasure == 'VitesseVent'}">m/s</c:when>
                                            <c:when test="${measurement.typeMeasure == 'IndiceQualitéAir'}">AQI</c:when>
                                            <c:when test="${measurement.typeMeasure == 'PM2.5'}">μg/m³</c:when>
                                            <c:when test="${measurement.typeMeasure == 'NO2'}">μg/m³</c:when>
                                            <c:when test="${measurement.typeMeasure == 'Ozone'}">μg/m³</c:when>
                                            <c:when test="${measurement.typeMeasure == 'Décibels'}">dB</c:when>
                                            <c:otherwise>unités</c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="measurement-item">
                                <p style="text-align: center; color: rgba(255,255,255,0.6);">
                                    Aucune mesure de données réelles trouvée.<br>
                                    Collectez des données pour voir les mesures ici.
                                </p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Auto-refresh every 5 minutes for live weather updates
        setTimeout(function() {
            if (confirm('Refresh page to get latest weather data?')) {
                window.location.reload();
            }
        }, 300000); // 5 minutes

        // Add smooth scrolling for better UX
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                document.querySelector(this.getAttribute('href')).scrollIntoView({
                    behavior: 'smooth'
                });
            });
        });
    </script>
</body>
</html>