<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="smartcity.model.*" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Capteurs - SmartCity Data Manager</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #0f2027, #203a43, #2c5364);
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
        
        .add-sensor-section h3 {
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
            min-width: 200px;
        }
        
        .form-group .btn {
            flex: 0 0 auto;
            min-width: 150px;
        }
        
        .form-input {
            padding: 12px 16px;
            border-radius: 8px;
            border: 1px solid rgba(255,255,255,0.2);
            background: rgba(255,255,255,0.1);
            color: #fff;
            font-size: 14px;
            transition: all 0.3s ease;
        }
        
        .form-input:focus {
            outline: none;
            border-color: #00f5d4;
            box-shadow: 0 0 0 2px rgba(0,245,212,0.2);
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
            text-shadow: 0 0 10px #00f5d4;
            margin-bottom: 30px;
        }
        form {
            display: flex;
            justify-content: center;
            gap: 10px;
            flex-wrap: wrap;
            margin-bottom: 30px;
        }
        input, button {
            padding: 10px 12px;
            border-radius: 6px;
            border: none;
            outline: none;
        }
        input {
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
        
        table {
            width: 90%;
            margin: 0 auto;
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
    </style>
</head>
<body class="smooth-scroll">
<h1>Gestion des Capteurs</h1>

<!-- Section d'ajout de capteur -->
<div class="control-panel">
    <div class="add-sensor-section">
        <h3><i class="icon">🛰️</i> Ajouter un nouveau capteur</h3>
        <form method="post" action="<%= request.getContextPath() %>/sensors" class="add-form">
            <div class="form-group">
                <input name="name" placeholder="Nom du capteur" required class="form-input" />
                <input name="type" placeholder="Type (Pollution, Trafic...)" required class="form-input" />
                <input name="zone" placeholder="Zone géographique" required class="form-input" />
                <button type="submit" class="btn btn-primary">
                    <i class="icon">➕</i> Ajouter Capteur
                </button>
            </div>
        </form>
    </div>
    
    <div class="add-sensor-section">
        <h3><i class="icon">📊</i> Collecter données réelles (API)</h3>
        <form method="post" action="<%= request.getContextPath() %>/sensors" class="add-form">
            <input type="hidden" name="action" value="fetchAPI">
            <div class="form-group">
                <input name="city" placeholder="Ville (ex: Paris, London)" class="form-input" />
                <input name="zone" placeholder="Zone (ex: Centre-Ville)" class="form-input" />
                <button type="submit" class="btn btn-primary">
                    <i class="icon">🌐</i> Collecter API
                </button>
                <small style="color: rgba(255,255,255,0.7);">Ajoute capteurs: Météo + Pollution + Bruit</small>
            </div>
        </form>
    </div>

    <div class="tools-section">
        <h3><i class="icon">🔧</i> Outils de gestion</h3>
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
// Auto-refresh toutes les 5 secondes si on ajoute des données
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
    link.setAttribute('download', 'capteurs.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}</script>



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

<table>
    <thead>
    <tr>
        <th>ID</th><th>Nom</th><th>Type</th><th>Zone</th><th>Source</th><th>Statut</th><th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <% java.util.List sensors = (java.util.List) request.getAttribute("sensors");
       if (sensors != null) {
           for (Object obj : sensors) { 
               smartcity.model.Sensor s = (smartcity.model.Sensor) obj; %>
        <tr>
            <td><%= s.getId() %></td>
            <td><%= s.getName() %></td>
            <td><%= s.getType() %></td>
            <td><%= s.getZone() %></td>
            <td><%= s.getSourceBadge() %></td>
            <td>
                <% if (s.isActive()) { %>
                    🟢 Actif
                <% } else { %>
                    🔴 Inactif
                <% } %>
            </td>
            <td class="actions">
                <form method="post" action="<%= request.getContextPath() %>/sensors" style="display: inline;">
                    <input type="hidden" name="action" value="toggle">
                    <input type="hidden" name="id" value="<%= s.getId() %>">
                    <button type="submit" class="action-btn <%= s.isActive() ? "btn-warning" : "btn-success" %>">
                        <i class="icon"><%= s.isActive() ? "⏸️" : "▶️" %></i>
                    </button>
                </form>
                <form method="post" action="<%= request.getContextPath() %>/sensors" style="display: inline;" 
                      onsubmit="return confirm('Êtes-vous sûr de vouloir supprimer ce capteur ?');">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="<%= s.getId() %>">
                    <button type="submit" class="action-btn btn-danger">
                        <i class="icon">🗑️</i>
                    </button>
                </form>
            </td>
        </tr>
    <% } } %>
    </tbody>
</table>
<style>
.btn-danger {
    background: linear-gradient(135deg, #ff4444, #cc0000) !important;
    color: white !important;
}
.btn-warning {
    background: linear-gradient(135deg, #ffbb33, #ff8800) !important;
    color: white !important;
}
.btn-success {
    background: linear-gradient(135deg, #00C851, #007E33) !important;
    color: white !important;
}
.actions {
    white-space: nowrap;
    display: flex;
    gap: 8px;
    justify-content: center;
}
</style>

<div id="statistics">
    <h2>Statistiques des Capteurs par Zone</h2>
<table>
    <thead>
        <tr>
            <th>Type</th>
            <th>Zone</th>
            <th>Nb Mesures</th>
            <th>Valeur Min</th>
            <th>Valeur Max</th>
            <th>Moyenne</th>
        </tr>
    </thead>
    <tbody>
    <% java.util.List sensorStats = (java.util.List) request.getAttribute("sensorStats");
       if (sensorStats != null) {
           for (Object obj : sensorStats) { 
               Object[] stat = (Object[]) obj; %>
        <tr>
            <td><%= stat[0] %></td>
            <td><%= stat[1] %></td>
            <td><%= stat[2] != null ? stat[2] : "0" %></td>
            <td><%= stat[3] != null ? String.format("%.2f", ((Number)stat[3]).doubleValue()) : "N/A" %></td>
            <td><%= stat[4] != null ? String.format("%.2f", ((Number)stat[4]).doubleValue()) : "N/A" %></td>
            <td><%= stat[5] != null ? String.format("%.2f", ((Number)stat[5]).doubleValue()) : "N/A" %></td>
        </tr>
    <% } } %>
    </tbody>
</table>

<div class="navigation">
    <a href="<%= request.getContextPath() %>/" class="back">🏠 Accueil</a>
    <a href="<%= request.getContextPath() %>/measurements" class="back">📊 Mesures</a>
</div>
<a href="#" class="back-to-top" onclick="window.scrollTo({top: 0, behavior: 'smooth'}); return false;">⇪</a>
</body>
</html>
