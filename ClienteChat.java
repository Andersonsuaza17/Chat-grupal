import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Cliente de chat TCP. Recibe mensajes en un hilo y permite escribir
 * mensajes desde el hilo principal.
 */
public class ClienteChat {
    public static void main(String[] args) {
        String host = args.length >= 1 ? args[0] : "localhost";
        int puerto = args.length >= 2 ? Integer.parseInt(args[1]) : 5000;

        System.out.println("=== CLIENTE DE CHAT TCP ===");
        System.out.println("Conectando a " + host + ":" + puerto + "...");

        try (
            Socket socket = new Socket(host, puerto);
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(
                new InputStreamReader(System.in))
        ) {
            // El servidor solicita el nombre antes de iniciar el chat.
            System.out.println(entrada.readLine());
            String nombre = teclado.readLine();
            salida.println(nombre);

            // Hilo receptor: escucha mientras el usuario puede seguir escribiendo.
            Thread receptor = new Thread(() -> {
                try {
                    String mensajeServidor;
                    while ((mensajeServidor = entrada.readLine()) != null) {
                        System.out.println(mensajeServidor);
                    }
                } catch (IOException e) {
                    System.out.println("Se cerró la conexión con el servidor.");
                }
            });
            receptor.setDaemon(true);
            receptor.start();

            System.out.println("Puedes escribir mensajes. Usa /salir para terminar.");
            String mensaje;
            while ((mensaje = teclado.readLine()) != null) {
                salida.println(mensaje);
                if (mensaje.equalsIgnoreCase("/salir")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo conectar. Verifica que el servidor esté activo.");
            System.err.println("Detalle: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("El puerto debe ser un número.");
        }
    }
}
