package servidorhttps_farmacia;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 *
 * @author andres
 */
public class ServidorHTTPS {

    private static final String HOST = "localhost";
    private static final int PUERTO = 8081;
    private static final Almacen almacen = new Almacen();

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Socket socCliente;
        //Crear acceso al fichero del almacen de certificados
        try (FileInputStream fis = new FileInputStream("AlmacenSSL")) {
            //Transformar la contraseña del almacén a array
            char[] claveKeystore = "123456".toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(fis, claveKeystore);

            //Crear la fábrica de claves SSL
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, claveKeystore);

            //Configurar SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

            //Crear el servidor SSL
            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) ssf.createServerSocket(PUERTO);

            //Habilitar protocolos seguros
            sslServerSocket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});

            //Mensaje informativo en el servidor dando instrucciones de uso por parte del cliente
            imprimeDisponible();

            //Permite al servidor procesar múltiples peticiones de los clientes
            while (true) {
                //Método bloqueante que queda a la espera de la conexión de clientes
                socCliente = sslServerSocket.accept();

                //Procesa la peticion del cliente, recibe el socket del cliente y el almacen de usuarios(recurso compartido)
                Thread hiloServidor
                        = new Thread(new HiloServidor(socCliente, almacen));

                //Lanzamos el hilo para atender al cliente
                hiloServidor.start();
            }

        } catch (IOException
                | NoSuchAlgorithmException
                | CertificateException
                | KeyStoreException
                | UnrecoverableKeyException
                | KeyManagementException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * **************************************************************************
     * muestra un mensaje en la Salida que confirma el arranque, y da algunas
     * indicaciones posteriores
     */
    private static void imprimeDisponible() {

        System.out.println("El Servidor WEB se está ejecutando y permanece a la "
                + "escucha por el puerto " + PUERTO + ".\n"
                + "   Escribe en la barra de direcciones de tu navegador preferido:\n"
                + "\thttps://" + HOST + ":" + PUERTO + "/ para solicitar la página de bienvenida\n"
                + "\thttps://" + HOST + ":" + PUERTO + "/loquesea para simular un error");
    }

}
