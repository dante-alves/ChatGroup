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
    
    public void enviaMensagem() {
        try {
            // Recebendo nome do usuário que enviou a mensagem, para imprimir no client de quem receber a mensagem.
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            // Criação de um scanner para enviar a mensagem aos outros clients.
            Scanner scanner = new Scanner(System.in);
            // O escaneamento continua enquanto houver conexão com o servidor
            while (socket.isConnected()) {
                String msgFila = scanner.nextLine();
                bufferedWriter.write(username + ": " + msgFila);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            Terminate(socket, bufferedReader, bufferedWriter);
        }
    }

    public void recebeMensagem() {
        // É necessária a criação de uma nova thread para o recebimento de mensagens
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mensagemChat;
                // O recebimento de mensagens continua enquanto houver conexão com o server
                while (socket.isConnected()) {
                    try {
                        // Recebendo as mensagens e as imprimindo no console
                        mensagemChat = bufferedReader.readLine();
                        System.out.println(mensagemChat);
                    } catch (IOException e) {
                        Terminate(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    
    // Método que fecha (terminate - extermina) tudo de maneira simples
    public void Terminate(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        // É requisitado um username para o usuário
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite seu Username para entrar no Chat: ");
        String username = scanner.nextLine();
        // O socket de conexão com o server é criado
        Socket socket = new Socket("localhost", 1234);

        // O socket e o username são repassados
        Client client = new Client(socket, username);
        // E aqui inicia-se o loop de enviar e receber mensagens do chat
        client.recebeMensagem();
        client.enviaMensagem();
    }
}


