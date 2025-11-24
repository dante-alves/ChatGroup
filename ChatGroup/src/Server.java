import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
}
