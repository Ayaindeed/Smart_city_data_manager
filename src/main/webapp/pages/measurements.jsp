<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="smartcity.model.*" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Mesures - SmartCity Data Manager</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #1a1a2e, #16213e, #0f3460);
            font-family: 'Poppins', sans-serif;
            color: #e5e5e5;
            padding: 40px;
            min-height: 100vh;
        }
        
        .control-panel {
            display: flex;
            flex-direction: column;
            gap: 25px;
            margin-bottom: 30px;
            padding: 25px;
            background: rgba(255,255,255,0.05);
            backdrop-filter: blur(10px);
            border-radius: 16px;
            border: 1px solid rgba(255,255,255,0.1);
        }
        
        .add-measurement-section h3 {
            color: #00f5d4;
            margin-bottom: 15px;
            font-size: 1.2em;
            display: flex;
            align-items: center;
            gap: 8px;
            font-style: normal;
        }

        .tools-section h3 {
            color: #00f5d4;
            margin-bottom: 15px;
            font-size: 1.2em;
            display: flex;
            align-items: center;
            gap: 8px;
            font-style: normal;
            justify-content: flex-end;
        }
        
        .add-form {
            display: flex;
            flex-direction: column;
        }
        
        .form-group {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
        }
        
        .form-group > * {
            flex: 1;
            min-width: 180px;
        }
        
        .form-group .btn {
            flex: 0 0 auto;
            min-width: 150px;
        }
        
        .form-input, .form-select {
            padding: 12px 16px;
            border-radius: 8px;
            border: 1px solid rgba(255,255,255,0.2);
            background: rgba(255,255,255,0.1);
            color: #fff;
            font-size: 14px;
            transition: all 0.3s ease;
        }
        
        .form-input:focus, .form-select:focus {
            outline: none;
            border-color: #00f5d4;
            box-shadow: 0 0 0 2px rgba(0,245,212,0.2);
        }
        
        .form-select option {
            background: #2c5364;
            color: #fff;
        }
        
        .tools-grid {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
            justify-content: flex-end;
        }
        
        .tools-grid .search-input {
            width: 250px;
            margin-left: auto;
        }
        
        .tools-grid .btn {
            width: auto;
            padding: 12px 20px;
        }
        
        .search-input {
            width: 100%;
            padding: 12px 16px;
            border-radius: 8px;
            border: 1px solid rgba(255,255,255,0.2);
            background: rgba(255,255,255,0.1);
            color: #fff;
            font-size: 14px;
            transition: all 0.3s ease;
        }
        
        .search-input:focus {
            outline: none;
            border-color: #00f5d4;
            box-shadow: 0 0 0 2px rgba(0,245,212,0.2);
        }
        
        .btn {
            padding: 12px 20px;
            border-radius: 8px;
            border: none;
            font-weight: 600;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
            justify-content: center;
            text-decoration: none;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #00f5d4, #00c9a7);
            color: #0f2027;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0,245,212,0.3);
        }
        
        .btn-refresh {
            background: linear-gradient(135deg, #00f5d4, #00c9a7);
            color: #0f2027;
        }
        
        .btn-refresh:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0,245,212,0.3);
        }
        
        .btn-export {
            background: linear-gradient(135deg, #00f5d4, #00c9a7);
            color: #0f2027;
        }
        
        .btn-export:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0,245,212,0.3);
        }

        .btn-stats {
            background: linear-gradient(135deg, #00f5d4, #00c9a7);
            color: #0f2027;
        }
        
        .btn-stats:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0,245,212,0.3);
        }

        .action-btn {
            padding: 8px;
            min-width: 40px;
            height: 40px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #00f5d4;
            color: #0f2027;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .icon {
            font-size: 16px;
            font-style: normal;
            font-family: 'Poppins', sans-serif;
        }
        
        @media (max-width: 768px) {
            .form-group > *, .tools-grid .search-input, .tools-grid .btn {
                flex: 1 1 100%;
                min-width: unset;
            }
        }
        h1 {
            text-align: center;
            color: #00f5d4;
            margin-bottom: 20px;
            text-shadow: 0 0 10px #00f5d4;
        }
        form {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 10px;
            margin-bottom: 40px;
        }
        input, select, button {
            padding: 10px 12px;
            border-radius: 6px;
            border: none;
        }
        input, select {
            background: rgba(255,255,255,0.1);
            color: #fff;
            border: 1px solid rgba(255,255,255,0.2);
        }
        button {
            background: linear-gradient(135deg, #00f5d4, #00c9a7);
            color: #0f2027;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
            justify-content: center;
        }
        
        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0,245,212,0.3);
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #00f5d4;
        }
        table {
            width: 90%;
            margin: auto;
            border-collapse: collapse;
            background: rgba(255,255,255,0.05);
            backdrop-filter: blur(8px);
            border-radius: 10px;
            overflow: hidden;
        }
        th, td {
            padding: 12px 16px;
            text-align: center;
        }
        th {
            background: rgba(255,255,255,0.15);
            color: #00f5d4;
        }
        tr:nth-child(even) {
            background: rgba(255,255,255,0.03);
        }
        .navigation {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin: 40px auto 0;
            flex-wrap: wrap;
        }
        
        .back {
            padding: 10px 20px;
            color: #00f5d4;
            text-decoration: none;
            background: rgba(255,255,255,0.05);
            border-radius: 8px;
            border: 1px solid rgba(0,245,212,0.2);
            transition: all 0.3s ease;
        }
        
        .back:hover { 
            background: rgba(0,245,212,0.1);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,245,212,0.2);
        }

        .back-to-top {
            position: fixed;
            bottom: 40px;
            right: 40px;
            font-size: 24px;
            padding: 15px 20px;
            background: rgba(0,245,212,0.15);
            border: 2px solid rgba(0,245,212,0.3);
            border-radius: 12px;
            color: #00f5d4;
            text-decoration: none;
            transition: all 0.3s ease;
            z-index: 1000;
        }

        .back-to-top:hover {
            background: rgba(0,245,212,0.25);
            transform: translateY(-5px);
            box-shadow: 0 5px 20px rgba(0,245,212,0.4);
        }

        .stats-btn {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            padding: 12px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .stats-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102,126,234,0.3);
        }

        .smooth-scroll {
            scroll-behavior: smooth;
        }
    </style>
</head>
<body class="smooth-scroll">

<h1>Mesures & Statistiques</h1>

<% 
String error = (String) session.getAttribute("error");
if (error != null) {
%>
    <div style="background-color: rgba(255,0,0,0.1); color: #ff3333; padding: 10px; margin: 10px 0; border-radius: 5px; text-align: center;">
        <%= error %>
    </div>
<%
    session.removeAttribute("error");
}

String message = (String) session.getAttribute("message");
if (message != null) {
%>
    <div style="background-color: rgba(0,255,0,0.1); color: #00cc00; padding: 10px; margin: 10px 0; border-radius: 5px; text-align: center;">
        <%= message %>
    </div>
<%
    session.removeAttribute("message");
}
%>

<!-- Section d'ajout de mesure -->
<div class="control-panel">
    <div class="add-measurement-section">
        <h3><i class="icon">📊</i> Ajouter une nouvelle mesure</h3>
        <form method="post" action="<%= request.getContextPath() %>/measurements" class="add-form">
            <div class="form-group">
                <input name="typeMeasure" placeholder="Type de mesure" required class="form-input" />
                <input name="value" type="number" step="0.01" placeholder="Valeur numérique" required class="form-input" />
                <select name="sensorId" required class="form-select">
                    <option value="">-- Sélectionner un capteur --</option>
                    <% java.util.List sensors = (java.util.List) request.getAttribute("sensors");
                       if (sensors != null) {
                           for (Object obj : sensors) { 
                               smartcity.model.Sensor s = (smartcity.model.Sensor) obj; %>
                        <option value="<%= s.getId() %>"><%= s.getName() %> - <%= s.getZone() %></option>
                    <% } } %>
                </select>
                <button type="submit" class="btn btn-primary">
                    <i class="icon">➕</i> Ajouter Mesure
                </button>
            </div>
        </form>
    </div>
    
    <div class="add-measurement-section">
        <h3><i class="icon">📊</i> Collecter mesures réelles (API)</h3>
        <form method="post" action="<%= request.getContextPath() %>/measurements" class="add-form">
            <input type="hidden" name="action" value="fetchAPI">
            <div class="form-group">
                <input name="city" placeholder="Ville (ex: Paris, London)" class="form-input" />
                <input name="zone" placeholder="Zone (ex: Centre-Ville)" class="form-input" />
                <button type="submit" class="btn btn-primary">
                    <i class="icon">🌐</i> Collecter API
                </button>
                <small style="color: rgba(255,255,255,0.7);">Ajoute mesures: Météo + Pollution + Bruit</small>
            </div>
        </form>
    </div>
    
    <div class="tools-section">
        <h3><i class="icon">🔧</i> Outils d'analyse</h3>
        <div class="tools-grid">
            <div class="tool-item">
                <input type="text" id="searchInput" placeholder="🔍 Rechercher..." class="search-input"
                       oninput="filterTable(this.value)"/>
            </div>
            <div class="tool-item">
                <button onclick="toggleAutoRefresh()" class="btn btn-refresh" id="refreshBtn">
                    <i class="icon">🔄</i> Auto-Refresh
                </button>
            </div>
            <div class="tool-item">
                <button onclick="exportToCSV()" class="btn btn-export">
                    <i class="icon">📑</i> Export CSV
                </button>
            </div>
            <div class="tool-item">
                <a href="#statistics" class="btn btn-stats">
                    <i class="icon">📈</i> Stats
                </a>
            </div>
        </div>
    </div>
</div>

<script>
// Auto-refresh toutes les 3 secondes si activé
let autoRefresh = false;

function filterTable(query) {
    query = query.toLowerCase();
    const rows = document.querySelectorAll('table tbody tr');
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(query) ? '' : 'none';
    });
}

function toggleAutoRefresh() {
    autoRefresh = !autoRefresh;
    const btn = document.getElementById('refreshBtn');
    if (autoRefresh) {
        btn.innerHTML = '<i class="icon">⏹️</i> Arrêter Auto-Refresh';
        btn.style.background = 'linear-gradient(135deg, #ff4444, #cc0000)';
        btn.classList.add('active');
        setInterval(() => {
            if (autoRefresh) {
                location.reload();
            }
        }, 3000);
    } else {
        btn.innerHTML = '<i class="icon">🔄</i> Auto-Refresh Stats';
        btn.style.background = 'linear-gradient(135deg, #667eea, #764ba2)';
        btn.classList.remove('active');
    }
}

function exportToCSV() {
    const table = document.querySelector('table');
    let csv = [];
    const rows = table.querySelectorAll('tr');
    
    for (let i = 0; i < rows.length; i++) {
        const row = [], cols = rows[i].querySelectorAll('td, th');
        for (let j = 0; j < cols.length; j++) {
            row.push(cols[j].innerText.replace(/,/g, ';'));
        }
        csv.push(row.join(','));
    }
    
    const csvContent = 'data:text/csv;charset=utf-8,' + csv.join('\n');
    const link = document.createElement('a');
    link.setAttribute('href', encodeURI(csvContent));
    link.setAttribute('download', 'mesures.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}</script>

<h2>Liste des Mesures</h2>
<table>
    <thead>
    <tr>
        <th>ID</th><th>Horodatage</th><th>Type</th><th>Valeur</th><th>Capteur</th><th>Source</th>
    </tr>
    </thead>
    <tbody>
    <% java.util.List measurements = (java.util.List) request.getAttribute("measurements");
       if (measurements != null) {
           for (Object obj : measurements) { 
               smartcity.model.Measurement m = (smartcity.model.Measurement) obj; %>
        <tr>
            <td><%= m.getId() %></td>
            <td><%= m.getTimestamp() %></td>
            <td><%= m.getTypeMeasure() %></td>
            <td><%= m.getValue() %></td>
            <td><%= m.getSensor().getName() %></td>
            <td><%= m.getSourceBadge() %></td>
        </tr>
    <% } } %>
    </tbody>
</table>

<div id="statistics">
<h2>Statistiques par Type de Capteur</h2>
<table>
    <thead>
        <tr>
            <th>Type de Capteur</th>
            <th>Zone</th>
            <th>Nombre de Mesures</th>
            <th>Moyenne</th>
            <th>Minimum</th>
            <th>Maximum</th>
        </tr>
    </thead>
    <tbody>
    <% List<Object[]> statsType = (List<Object[]>) request.getAttribute("statsType");
       if (statsType != null) {
           for (Object[] stat : statsType) { %>
        <tr>
            <td><%= stat[0] %></td>
            <td><%= stat[5] %></td>
            <td><%= stat[1] %></td>
            <td><%= stat[2] != null ? String.format("%.2f", ((Number)stat[2]).doubleValue()) : "N/A" %></td>
            <td><%= stat[3] != null ? String.format("%.2f", ((Number)stat[3]).doubleValue()) : "N/A" %></td>
            <td><%= stat[4] != null ? String.format("%.2f", ((Number)stat[4]).doubleValue()) : "N/A" %></td>
        </tr>
    <% } } %>
    </tbody>
</table>

<h2>Statistiques Journalières (10 derniers jours)</h2>
<table>
    <thead>
        <tr>
            <th>Date</th>
            <th>Nombre de Mesures</th>
            <th>Moyenne</th>
            <th>Minimum</th>
            <th>Maximum</th>
        </tr>
    </thead>
    <tbody>
    <% List<Object[]> statsTime = (List<Object[]>) request.getAttribute("statsTime");
       if (statsTime != null) {
           for (Object[] stat : statsTime) { %>
        <tr>
            <td><%= stat[0] %>-<%= stat[1] %>-<%= stat[2] %></td>
            <td><%= stat[3] %></td>
            <td><%= stat[4] != null ? String.format("%.2f", ((Number)stat[4]).doubleValue()) : "N/A" %></td>
            <td><%= stat[5] != null ? String.format("%.2f", ((Number)stat[5]).doubleValue()) : "N/A" %></td>
            <td><%= stat[6] != null ? String.format("%.2f", ((Number)stat[6]).doubleValue()) : "N/A" %></td>
        </tr>
    <% } } %>
    </tbody>
</table>

<div class="navigation">
    <a href="<%= request.getContextPath() %>/" class="back">🏠 Accueil</a>
    <a href="<%= request.getContextPath() %>/sensors" class="back">📡 Capteurs</a>
</div>
<a href="#" class="back-to-top" onclick="window.scrollTo({top: 0, behavior: 'smooth'}); return false;">⇪</a>
</body>
</html>
