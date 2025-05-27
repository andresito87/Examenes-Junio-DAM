package tarea3pspsolucion;

/**
 *
 * @author LuisRosillo <>
 */
import java.util.Random;

public class SesionJuego {
    int intentosAdivina;
    int numeroSecreto;
    int marcadorUsuarioDados;
    int marcadorServidorDados;
    int rondaDados;
    int marcadorUsuarioPPT;
    int marcadorServidorPPT;
    int rondaPPT;

    public SesionJuego() {
        this.intentosAdivina = 0;
        this.numeroSecreto = new Random().nextInt(100) + 1;
        this.marcadorUsuarioDados = 0;
        this.marcadorServidorDados = 0;
        this.rondaDados=0;
        this.marcadorUsuarioPPT = 0;
        this.marcadorServidorPPT = 0;
        this.rondaPPT=0;
    }
}