import java.io.*;
import java.net.Socket;
import java.util.Scanner;

//Um usuário/client envia mensagens para o servidor, que, por sua vez, repassa essas mensagens para todo outro 
//usuário conectado por meio de threads e sockets


public class Client {
	// O cliente possui um socket para conectar ao servidor, um leitor e um escritor para receber e enviar mensagens pelo buffer.
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            Terminate(socket, bufferedReader, bufferedWriter);
        }
    }
}
