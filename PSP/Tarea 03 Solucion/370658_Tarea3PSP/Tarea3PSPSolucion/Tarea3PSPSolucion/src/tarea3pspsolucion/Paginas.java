package tarea3pspsolucion;

import java.util.Random;

public class Paginas {

    public static final String html_index = "<html><head><title>Inicio</title><meta charset=UTF-8><link rel=icon href=data:,/></head><body>"
            + "<h1>Bienvenido al Servidor de Juegos</h1>"
            + "<ul>"
            + "<li><a href='/adivina'>Adivina el Número</a></li>"
            + "<li><a href='/dados'>Lanza Dados</a></li>"
            + "<li><a href='/ppt'>Piedra, Papel o Tijera</a></li>"
            + "</ul></body></html>";

    public static final String html_noEncontrado = "<html><head><title>Error 404</title><link rel=icon href=data:,/></head><body>"
            + "<h1>404 Página No Encontrada</h1>"
            + "<p>La página solicitada no existe.</p>"
            + "</body></html>";

    public static String generarHtmlAdivina(String resultado) {
        return "<html><head><title>Adivina el Número</title><link rel=icon href=data:,/></head><body>"
                + "<h1>¡Adivina el Número!</h1>"
                + "<form action='/adivina' method='POST'>"
                + "<label for='numero'>Introduce un número del 1 al 100:</label>"
                + "<input type='number' id='numero' name='numero' min='1' max='100' required></input>"
                + "<button type='submit'>Enviar</button>"
                + "</form>"
                + resultado
                + "</body></html>";
    }

    public static String generarHtmlPpt(String resultado) {
        return "<html><head><title>Piedra, Papel o Tijera</title><link rel=icon href=data:,/></head><body>"
                + "<h1>¡Juega a Piedra, Papel o Tijera!</h1>"
                + "<form action='/ppt' method='POST'>"
                + "<button name='opcion' value='Piedra'>Piedra</button>"
                + "<button name='opcion' value='Papel'>Papel</button>"
                + "<button name='opcion' value='Tijeras'>Tijeras</button>"
                + "</form>"
                + resultado
                + "</body></html>";
    }

    public static String generarHtmlDados(String resultado) {
        Random random = new Random();
        int dadoCliente = random.nextInt(6) + 1;
        return "<html><head><title>Lanza Dados</title><link rel=icon href=data:,/></head><body>"
                + "<h1>¡Lanza Dados!</h1>"
                + "<form action='/dados' method='POST'>"
                + "<button name='lanzar' value='" + dadoCliente + "'>Lanzar Dados</button>"
                + "</form>"
                + resultado
                + "</body></html>";
    }
}
