package servidorhttps_newsletter;

/**
 *
 * @author ANDRÉS SAMUEL PODADERA GONZÁLEZ
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
      <title>Servidor HTTPS Newsletter</title>
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
          <h1 class="title is-2 has-text-primary">¡Suscríbete a la newsletter PSP!</h1>
          <h3 class="subtitle is-4">Por favor, ingresa tu usuario y correo electrónico</h3>
          <form action="/" method="POST" class="box" style="max-width: 400px; margin: auto;">
            <div class="field">
              <label class="label">Nombre</label>
              <div class="control">
                <input class="input" type="text" name="usuario" placeholder="Tu usuario" required>
              </div>
            </div>

            <div class="field">
              <label class="label">Email</label>
              <div class="control">
                <input class="input" type="email" name="email" placeholder="email@example.com" required>
              </div>
            </div>

            <div class="field">
              <div class="control">
                <button class="button is-primary is-fullwidth" type="submit">Confirmar suscripción</button>
              </div>
            </div>
          </form>
        </div>
      </section>

      <footer class="footer has-background-dark has-text-light py-2">
        <div class="content has-text-centered">
          <p>© 2025 Servidor Newsletter | Andrés Samuel Podadera González | Módulo PSP</p>
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
              <p>© 2025 Servidor Newsletter | Andrés Samuel Podadera González | Módulo PSP</p>
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
              <p>© 2025 Servidor Newsletter | Andrés Samuel Podadera González | Módulo PSP</p>
            </div>
          </footer>

        </body>
        </html>
        """;

    public static String generarHtmlConMensaje(String frase) {
        // Reemplaza saltos de línea por etiquetas <br>
        String fraseFormateada = frase.replace("\n", "<br>");

        return String.format("""
        <!DOCTYPE html>
        <html lang="es">
        <head>
          <meta charset="UTF-8">
          <title>Servidor HTTPS Newsletter</title>
          <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@1.0.4/css/bulma.min.css">
          <style>
            html, body {
              height: 100%%;
            }
            .full-height {
              min-height: 100vh;
              display: flex;
              flex-direction: column;
              justify-content: space-between;
            }
            .custom-shadow {
              box-shadow: 0 6px 16px rgba(0, 0, 0, 0.25);
            }
          </style>
        </head>
        <body class="has-background-light full-height">

          <section class="section">
            <div class="container has-text-centered">
              <h1 class="title is-2 has-text-primary">¡Suscríbete a la newsletter PSP!</h1>
              <h3 class="subtitle is-4">Por favor, ingresa tu usuario y correo electrónico</h3>
              <form action="/" method="POST" class="box" style="max-width: 400px; margin: auto;">
                <div class="field">
                  <label class="label">Nombre</label>
                  <div class="control">
                    <input class="input" type="text" name="usuario" placeholder="Tu usuario" required>
                  </div>
                </div>

                <div class="field">
                  <label class="label">Email</label>
                  <div class="control">
                    <input class="input" type="email" name="email" placeholder="email@example.com" required>
                  </div>
                </div>

                <div class="field">
                  <div class="control">
                    <button class="button is-primary is-fullwidth" type="submit">Confirmar suscripción</button>
                  </div>
                </div>
              </form>
            </div>
          </section>

          <section class="section">
            <div class="container">
            <h2 class="title is-4 has-text-centered has-text-primary mb-4">Suscriptores</h2>
              <div class="notification is-info is-dark has-text-centered has-text-light custom-shadow">
                <p class="is-size-3 has-text-weight-semibold">%s</p>
              </div>
            </div>
          </section>

          <footer class="footer has-background-dark has-text-light py-2">
            <div class="content has-text-centered">
              <p>© 2025 Servidor Newsletter | Andrés Samuel Podadera González | Módulo PSP</p>
            </div>
          </footer>

        </body>
        </html>
        """, fraseFormateada);
    }

}
