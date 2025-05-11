package chikitowiki_servidorhttps;

/**
 *
 * @author andres
 */
public class Paginas {

    // Cabecera con el tipo de contenido y el encoding de la página
    public static final String PRIMERA_CABECERA
            = "Content-Type: text/html; charset=UTF-8";

    //Contenido home
    public static final String HTML_INDEX = "<!DOCTYPE html>"
            + "<html lang=\"es\">"
            + "<head>"
            + "<meta charset=\"UTF-8\">"
            + "<title>Servidor HTTPS de Farmacia Viva</title>"
            + "</head>"
            + "<body>"
            + "<h1>¡Bienvenid@ a Farmacia Viva!</h1>"
            + "<h3>¡Ingresa la cantidad deseada y pulsa para comprar el medicamento!</h3>"
            + "<ul>"
            + "<li><form action=\"/\" method=\"POST\">"
            + "<input type=\"hidden\" name=\"producto\" value=\"Paracetamol\">"
            + "<label>Cantidad: <input type=\"number\" name=\"cantidad\" min=\"1\" value=\"1\"></label>"
            + "<button type=\"submit\">Comprar Paracetamol</button>"
            + "</form></li>"
            + "<li><form action=\"/\" method=\"POST\">"
            + "<input type=\"hidden\" name=\"producto\" value=\"Ibuprofeno\">"
            + "<label>Cantidad: <input type=\"number\" name=\"cantidad\" min=\"1\" value=\"1\"></label>"
            + "<button type=\"submit\">Comprar Ibuprofeno</button>"
            + "</form></li>"
            + "<li><form action=\"/\" method=\"POST\">"
            + "<input type=\"hidden\" name=\"producto\" value=\"Vitamina_C\">"
            + "<label>Cantidad: <input type=\"number\" name=\"cantidad\" min=\"1\" value=\"1\"></label>"
            + "<button type=\"submit\">Comprar Vitamina C</button>"
            + "</form></li>"
            + "</ul>"
            + "<footer>"
            + "© 2025 Servidor de Farmacia Viva | Andrés Samuel Podadera González | Módulo PSP"
            + "</footer>"
            + "</body>"
            + "</html>";

    //Contenido para página no encontrada
    public static final String HTML_NO_ENCONTRADO = "<!DOCTYPE html>"
            + "<head>"
            + "<meta charset=\"UTF-8\">"
            + "<title>Página no encontrada</title>"
            + "</head>"
            + "<body>"
            + "<a href=\"/\"><button type=\"button\">Volver a inicio</button></a>"
            + "<div class=\"error-container\">"
            + "<h1>404 - Página no encontrada</h1>"
            + "<h3>Lo sentimos, la página que buscas no está disponible.</h3>"
            + "</div>"
            + "<footer>"
            + "© 2025 Servidor de Farmacia Viva | Andrés Samuel Podadera González | Módulo PSP"
            + "</footer>"
            + "</body>"
            + "</html>";
   
}
