<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>SmartCity Data Manager</title>

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary: #00c9a7;
            --secondary: #3a3a3a;
            --bg-gradient: linear-gradient(135deg, #0f2027, #203a43, #2c5364);
            --card-bg: rgba(255, 255, 255, 0.08);
            --text-light: #e5e5e5;
            --accent: #00f5d4;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }

        body {
            background: var(--bg-gradient);
            min-height: 100vh;
            color: var(--text-light);
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 40px;
        }

        h1 {
            font-size: 2.5em;
            font-weight: 600;
            margin-bottom: 10px;
            color: var(--accent);
            text-shadow: 0 0 10px var(--accent);
        }

        h2 {
            color: #b4b4b4;
            font-weight: 400;
            margin-bottom: 40px;
        }

        .card-container {
            display: flex;
            gap: 30px;
            flex-wrap: wrap;
            justify-content: center;
        }

        .card {
            background: var(--card-bg);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255,255,255,0.15);
            border-radius: 16px;
            width: 280px;
            padding: 25px;
            text-align: center;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            transition: all 0.3s ease;
        }

        .card:hover {
            transform: translateY(-5px);
            border-color: var(--primary);
            box-shadow: 0 20px 40px rgba(0, 201, 167, 0.2);
        }

        .card h3 {
            color: var(--accent);
            margin-bottom: 10px;
            font-size: 1.4em;
        }

        .card p {
            font-size: 0.95em;
            color: #ccc;
            margin-bottom: 20px;
        }

        .btn {
            display: inline-block;
            padding: 10px 20px;
            border-radius: 30px;
            border: 2px solid var(--primary);
            color: var(--primary);
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn:hover {
            background: var(--primary);
            color: #0f2027;
        }

        footer {
            margin-top: 60px;
            font-size: 0.9em;
            color: #888;
        }

        footer span {
            color: var(--accent);
        }
    </style>
</head>
<body>

<h1>SmartCity Data Manager</h1>
<h2>Analyse et gestion Big Data des capteurs urbains</h2>

<% String message = (String) session.getAttribute("message"); 
   if (message != null) { %>
    <div style="background: rgba(0, 201, 167, 0.2); border: 1px solid var(--primary); border-radius: 8px; padding: 15px; margin-bottom: 20px; text-align: center;">
        <%= message %>
    </div>
<% session.removeAttribute("message"); 
   } %>

<div class="card-container">
    <div class="card">
        <h3>🛰️ Capteurs</h3>
        <p>Gérez et visualisez vos capteurs urbains (trafic, pollution, bruit...).</p>
        <a href="<%= request.getContextPath() %>/sensors" class="btn">Accéder</a>
    </div>

    <div class="card">
        <h3>📊 Mesures</h3>
        <p>Consultez les mesures, ajoutez de nouvelles données, et affichez les statistiques analytiques.</p>
        <a href="<%= request.getContextPath() %>/measurements" class="btn">Accéder</a>
    </div>



    <div class="card">
        <h3>⚙️ Génération</h3>
        <p>Générez massivement des données de test pour observer les performances.</p>
        <form method="post" action="<%= request.getContextPath() %>/generate">
            <input type="hidden" name="n" value="10000">
            <input type="hidden" name="nomCapteur" value="CapteurBatch">
            <input type="hidden" name="typeCapteur" value="Pollution">
            <input type="hidden" name="locCapteur" value="Centre-Ville">
            <button type="submit" class="btn">Générer 10K mesures</button>
        </form>
    </div>
</div>

<footer>
    © Built by <span>Aya R.</span> – Powered by JEE / Hibernate / WildFly
</footer>

</body>
</html>
