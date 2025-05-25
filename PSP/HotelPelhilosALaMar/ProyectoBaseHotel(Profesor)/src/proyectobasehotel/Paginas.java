package proyectobasehotel;

public class Paginas {
    
    public static final String html_noEncontrado = 
            "<html><head><title>Error 404</title><link rel=icon href=data:,/></head><body>"
        + "<h1>404 Página No Encontrada</h1>"
        + "<p>La página solicitada no existe.</p>"
        + "</body></html>";
    
    public static final String html_reservas = "<!DOCTYPE html>" +
    "<html lang=\"es\">" +
    "<head>" +
    "   <meta charset=\"UTF-8\">" +
    "   <link rel=icon href=data:,/>" +
    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
    "    <title>Hotel – Reservas</title>" +
    "    <style>" +
    "        body {" +
    "            font-family: 'Arial', sans-serif;" +
    "            background-color: #eef2f3;" +
    "            margin: 0; padding: 0;" +
    "        }" +
    "        header {" +
    "            background-color: #4CAF50;" +
    "            color: white;" +
    "            text-align: center;" +
    "            padding: 20px 0;" +
    "        }" +
    "        .container {" +
    "            max-width: 450px;" +
    "            margin: 40px auto;" +
    "            background-color: white;" +
    "            padding: 25px;" +
    "            box-shadow: 0 0 12px rgba(0,0,0,0.1);" +
    "            border-radius: 8px;" +
    "       }" +
    "        label {" +
    "            display: block;" +
    "            font-weight: bold;" +
    "            margin-top: 10px;" +
    "        }" +
    "        select, input[type=\"number\"] {" +
    "            width: 100%;" +
    "            padding: 10px;" +
    "            margin-top: 5px;" +
    "            margin-bottom: 15px;" +
    "            border: 1px solid #ccc;" +
    "            border-radius: 4px;" +
    "            font-size: 15px;" +
    "        }" +
    "        button {" +
    "            width: 100%;" +
    "            padding: 12px;" +
    "            background-color: #4CAF50;" +
    "            color: white;" +
    "            font-size: 16px;" +
    "            border: none;" +
    "            border-radius: 4px;" +
    "            cursor: pointer;" +
    "            transition: background-color 0.3s ease;" +
    "        }" +
    "        button:hover {" +
    "            background-color: #45a049;" +
    "        }" +
    "    </style>" +
    "</head>" +
    "<body>" +
    "    <header>" +
    "        <h1>Hotel PelHilos a la mar</h1>" +
    "        <p>Registro de reservas por día</p>" +
    "    </header>" +
    "    <div class=\"container\">" +
    "        <form action=\"/\" method=\"POST\">" +
    "            <label for=\"dia\">Día de la semana:</label>" +
    "            <select id=\"dia\" name=\"dia\">" +
    "                <option value=\"lunes\">Lunes</option>" +
    "                <option value=\"martes\">Martes</option>" +
    "                <option value=\"miercoles\">Miércoles</option>" +
    "                <option value=\"jueves\">Jueves</option>" +
    "                <option value=\"viernes\">Viernes</option>" +
    "                <option value=\"sabado\">Sábado</option>" +
    "                <option value=\"domingo\">Domingo</option>" +
    "            </select>" +
    "            <label for=\"cantidad\">Número de habitaciones:</label>" +
    "            <input type=\"number\" id=\"cantidad\" name=\"cantidad\" min=\"1\" required autofocus>" +
    "            <button type=\"submit\">Reservar</button>" +
    "        </form>" +
    "    </div>" +
    "</body>" +
    "</html>";

    public static final String html_reservas1 = "<!DOCTYPE html>\\n" +
    "<html lang=\\\"es\\\">\\n" +
    "<head>\\n" +
    "    <meta charset=\\\"UTF-8\\\">\\n" +
    "    <link rel=icon href=data:,/>\\n" +
    "    <meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1.0\\\">\\n" +
    "    <title>Hotel – Reservas</title>\\n" +
    "    <style>\\n" +
    "        body {\\n" +
    "            font-family: 'Arial', sans-serif;\\n" +
    "            background-color: #eef2f3;\\n" +
    "            margin: 0; padding: 0;\\n" +
    "        }\\n" +
    "        header {\\n" +
    "            background-color: #4CAF50;\\n" +
    "            color: white;\\n" +
    "            text-align: center;\\n" +
    "            padding: 20px 0;\\n" +
    "        }\\n" +
    "        .container {\\n" +
    "            max-width: 450px;\\n" +
    "            margin: 40px auto;\\n" +
    "            background-color: white;\\n" +
    "            padding: 25px;\\n" +
    "            box-shadow: 0 0 12px rgba(0,0,0,0.1);\\n" +
    "            border-radius: 8px;\\n" +
    "        }\\n" +
    "        label {\\n" +
    "            display: block;\\n" +
    "            font-weight: bold;\\n" +
    "            margin-top: 10px;\\n" +
    "        }\\n" +
    "        select, input[type=\\\"number\\\"] {\\n" +
    "            width: 100%;\\n" +
    "            padding: 10px;\\n" +
    "            margin-top: 5px;\\n" +
    "            margin-bottom: 15px;\\n" +
    "            border: 1px solid #ccc;\\n" +
    "            border-radius: 4px;\\n" +
    "            font-size: 15px;\\n" +
    "        }\\n" +
    "        button {\\n" +
    "            width: 100%;\\n" +
    "            padding: 12px;\\n" +
    "            background-color: #4CAF50;\\n" +
    "            color: white;\\n" +
    "            font-size: 16px;\\n" +
    "            border: none;\\n" +
    "            border-radius: 4px;\\n" +
    "            cursor: pointer;\\n" +
    "            transition: background-color 0.3s ease;\\n" +
    "        }\\n" +
    "        button:hover {\\n" +
    "            background-color: #45a049;\\n" +
    "        }\\n" +
    "    </style>\\n" +
    "</head>\\n" +
    "<body>\\n" +
    "    <header>\\n" +
    "        <h1>Hotel PelHilos a la mar</h1>\\n" +
    "        <p>Registro de reservas por día</p>\\n" +
    "    </header>\\n" +
    "    <div class=\\\"container\\\">\\n" +
    "        <form action=\\\"/\\\" method=\\\"POST\\\">\\n" +
    "            <label for=\\\"dia\\\">Día de la semana:</label>\\n" +
    "            <select id=\\\"dia\\\" name=\\\"dia\\\">\\n" +
    "                <option value=\\\"lunes\\\">Lunes</option>\\n" +
    "                <option value=\\\"martes\\\">Martes</option>\\n" +
    "                <option value=\\\"miercoles\\\">Miércoles</option>\\n" +
    "                <option value=\\\"jueves\\\">Jueves</option>\\n" +
    "                <option value=\\\"viernes\\\">Viernes</option>\\n" +
    "                <option value=\\\"sabado\\\">Sábado</option>\\n" +
    "                <option value=\\\"domingo\\\">Domingo</option>\\n" +
    "            </select>\\n" +
    "            <label for=\\\"cantidad\\\">Número de habitaciones:</label>\\n" +
    "            <input type=\\\"number\\\" id=\\\"cantidad\\\" name=\\\"cantidad\\\" min=\\\"1\\\" required autofocus>\\n" +
    "            <button type=\\\"submit\\\">Reservar</button>\\n" +
    "        </form>\\n" +
    "    </div>\\n" +
    "</body>\\n" +
    "</html>\";";
}
