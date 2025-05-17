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
    public static final String HTML_INDEX = """
        <!DOCTYPE html>
        <html lang="es">
        <head>
          <meta charset="UTF-8">
          <title>Servidor HTTPS Chikito Wiki</title>
          <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@1.0.4/css/bulma.min.css">
          <style>
            html, body {
              height: 100%;
            }
            .full-height {
              min-height: 100vh;
              display: flex;
              flex-direction: column;
              justify-content: space-between;
            }
          </style>
        </head>
        <body class="has-background-light full-height">

          <section class="section">
            <div class="container has-text-centered">
              <h1 class="title is-2 has-text-primary">¡Bienvenid@ a Chikito Wiki!</h1>
              <h3 class="subtitle is-4">¡Pulsa el botón para obtener una frase!</h3>
              <form action="/" method="POST">
                <input type="hidden" name="frase" value="true">
                <button class="button is-danger is-large mt-4" type="submit">
                  ¡¡¡AL ATAQUEERRR!!!
                </button>
              </form>
            </div>
          </section>

          <footer class="footer has-background-dark has-text-light py-2">
            <div class="content has-text-centered">
              <p>© 2025 Servidor ChikitoWiki | Andrés Samuel Podadera González | Módulo PSP</p>
            </div>
          </footer>

        </body>
        </html>
        """;

    //Contenido para página no encontrada
    public static final String HTML_NO_ENCONTRADO = """
        <!DOCTYPE html>
        <html lang="es">
        <head>
          <meta charset="UTF-8">
          <title>Página no encontrada</title>
          <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@1.0.4/css/bulma.min.css">
          <style>
            html, body {
              height: 100%;
            }
            .full-height {
              min-height: 100vh;
              display: flex;
              flex-direction: column;
              justify-content: space-between;
            }
          </style>
        </head>
        <body class="has-background-light full-height">

          <section class="section">
            <div class="container has-text-centered">
              <div class="notification is-danger is-light">
                <h1 class="title is-2">404 - Página no encontrada</h1>
                <h3 class="subtitle is-4">Lo sentimos, la página que buscas no está disponible.</h3>
              </div>

              <a href="/" class="button is-link is-medium mt-4">
                Volver a inicio
              </a>
            </div>
          </section>

          <footer class="footer has-background-dark has-text-light py-2">
            <div class="content has-text-centered">
              <p>© 2025 Servidor ChikitoWiki | Andrés Samuel Podadera González | Módulo PSP</p>
            </div>
          </footer>

        </body>
        </html>
        """;

    // Contenido que envia el servidor cuando se envia información incorrecta
    public static final String HTML_INFORMACION_INVALIDA = """
        <!DOCTYPE html>
        <html lang="es">
        <head>
          <meta charset="UTF-8">
          <title>Información incorrecta</title>
          <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@1.0.4/css/bulma.min.css">
          <style>
            html, body {
              height: 100%;
            }
            .full-height {
              min-height: 100vh;
              display: flex;
              flex-direction: column;
              justify-content: space-between;
            }
          </style>
        </head>
        <body class="has-background-light full-height">

          <section class="section">
            <div class="container has-text-centered">
              <div class="notification is-danger is-light">
                <h1 class="title is-2">400 - Petición Incorrecta</h1>
                <h3 class="subtitle is-4">Lo sentimos, la información suministrada no es correcta.</h3>
              </div>

              <a href="/" class="button is-link is-medium mt-4">
                Volver a inicio
              </a>
            </div>
          </section>

          <footer class="footer has-background-dark has-text-light py-2">
            <div class="content has-text-centered">
              <p>© 2025 Servidor ChikitoWiki | Andrés Samuel Podadera González | Módulo PSP</p>
            </div>
          </footer>

        </body>
        </html>
        """;

    public static String generarHtmlConFrase(String frase) {
        return """
       <!DOCTYPE html>
       <html lang="es">
       <head>
         <meta charset="UTF-8">
         <title>Servidor HTTPS Chikito Wiki</title>
         <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@1.0.4/css/bulma.min.css">
         <style>
           html, body {
             height: 100%;
           }
           .full-height {
             min-height: 100vh;
             display: flex;
             flex-direction: column;
             justify-content: space-between;
           }
           .custom-shadow {
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
           }
         </style>
       </head>
       <body class="has-background-light full-height">
   
         <section class="section">
           <div class="container has-text-centered">
             <h1 class="title is-2 has-text-primary">¡Bienvenid@ a Chikito Wiki!</h1>
             <h3 class="subtitle is-4">¡Pulsa el botón para obtener una frase!</h3>
             <form action="/" method="POST">
               <input type="hidden" name="frase" value="true">
               <button class="button is-danger is-large mt-4" type="submit">
                 ¡¡¡AL ATAQUEERRR!!!
               </button>
             </form>
           </div>
         </section>
   
       <section class="section">
         <div class="container">
           <div class="notification is-info is-light has-text-centered custom-shadow">
             <p class="is-size-3 has-text-weight-semibold">"""
                + frase
                + "</p>"
                + "</div>"
                + "</div>"
                + "</section>"
                + "<footer class=\"footer has-background-dark has-text-light py-2\">"
                + "<div class=\"content has-text-centered\">"
                + "<p>© 2025 Servidor ChikitoWiki | Andrés Samuel Podadera González | Módulo PSP</p>"
                + "</footer>"
                + "</body>"
                + "</html>";
    }

}
