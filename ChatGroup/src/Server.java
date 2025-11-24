import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    public void startServer() {
        try {
            // Espera conexões na porta 1234.
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Um novo usuario conectou-se!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                // Esse método inicia a execução de uma thread, e quando "start()" é chamado, o método run tambem é.
                // Quem controlará as threads será o sistema operacional
                thread.start();
            }
        } catch (IOException e) {
            fechaServerSocket();
        }
    }

    // Fechamento do socket do servidor
    public void fechaServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Server Fechado");
        }
    }

    // É executado o programa
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
