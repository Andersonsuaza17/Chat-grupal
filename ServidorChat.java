import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor de chat TCP concurrente.
 * Cada cliente conectado es atendido por un hilo independiente.
 */
public class ServidorChat {
    private static final int PUERTO = 5000;
    private static final Set<PrintWriter> clientes = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("=== SERVIDOR DE CHAT TCP ===");
        System.out.println("Puerto: " + PUERTO);
        System.out.println("Esperando conexiones...");

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = servidor.accept();
                System.out.println("Nueva conexión desde: " + socketCliente.getInetAddress());

                // Un hilo independiente atiende a cada cliente.
                Thread hiloCliente = new Thread(new ManejadorCliente(socketCliente));
                hiloCliente.start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void difundir(String mensaje, PrintWriter emisor) {
        for (PrintWriter cliente : clientes) {
            if (cliente != emisor) {
                cliente.println(mensaje);
            }
        }
        System.out.println(mensaje);
    }

    private static class ManejadorCliente implements Runnable {
        private final Socket socket;
        private PrintWriter salida;

        ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String nombre = "Cliente";
            try (
                BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                PrintWriter salidaCliente = new PrintWriter(socket.getOutputStream(), true)
            ) {
                salida = salidaCliente;
                salida.println("Escribe tu nombre:");
                String nombreIngresado = entrada.readLine();

                if (nombreIngresado != null && !nombreIngresado.trim().isEmpty()) {
                    nombre = nombreIngresado.trim();
                }

                clientes.add(salida);
                difundir("*** " + nombre + " se ha conectado. ***", salida);

                salida.println("Conectado al chat. Escribe /salir para desconectarte.");

                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    if (mensaje.equalsIgnoreCase("/salir")) {
                        break;
                    }
                    if (!mensaje.trim().isEmpty()) {
                        difundir(nombre + ": " + mensaje, salida);
                    }
                }
            } catch (IOException e) {
                System.out.println("Conexión interrumpida: " + e.getMessage());
            } finally {
                if (salida != null) {
                    clientes.remove(salida);
                }
                difundir("*** " + nombre + " se ha desconectado. ***", null);
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
