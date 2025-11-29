import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
	private final ServerSocket serverSocket;
    public static List<String> conversation = new ArrayList<>();

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

    // adicionar mensagens ao histórico de mensagens
    public static void addMessage(String message) {
        conversation.add(message);
    }

    public static void saveConversationAsHTML() {
        try (PrintWriter writer = new PrintWriter("chat.html")) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html><body>");
            writer.println("<h2>Histórico da Conversa</h2>");

            for (String msg : conversation) {
                writer.println("<p>" + msg + "</p>");
            }

            writer.println("</body></html>");

            System.out.println("Arquivo chat.html salvo.");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startHttpServer() {
        try (ServerSocket httpServer = new ServerSocket(8080)) {

            System.out.println("Servidor HTTP rodando em: http://localhost:8080/chat.html");

            while (true) {

                Socket client = httpServer.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream());

                String request = in.readLine();

                if (request != null && request.contains("GET /chat.html")) {

                    File file = new File("chat.html");

                    if (!file.exists()) {
                        out.println("HTTP/1.1 404 Not Found");
                        out.println();
                        out.println("<h1>Histórico ainda não gerado.</h1>");
                        out.flush();
                        client.close();
                        continue;
                    }

                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/html; charset=UTF-8");
                    out.println();

                    BufferedReader htmlReader = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = htmlReader.readLine()) != null) {
                        out.println(line);
                    }

                    htmlReader.close();

                } else {
                    out.println("HTTP/1.1 404 Not Found");
                    out.println();
                }

                out.flush();
                client.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeServer() {
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor de chat encerrado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // É executado o programa
    public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(1234);
    Server server = new Server(serverSocket);

    System.out.println("Servidor de Chat iniciado na porta 1234.");
    System.out.println("Digite sair para encerrar o chat e iniciar o servidor HTTP.");

    // Thread do chat
    Thread chatThread = new Thread(server::startServer);
    chatThread.start();

    // Thread para capturar comandos do console
    Thread consoleThread = new Thread(() -> {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String cmd = scanner.nextLine();
            if (cmd.equalsIgnoreCase("sair")) {
                System.out.println("Comando recebido. Encerrando o chat...");
                server.closeServer();
                break;
            }
        }
    });
    consoleThread.start();

    // Espera o chat encerrar
    try {
        chatThread.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    System.out.println("Chat encerrado. Iniciando servidor HTTP...");
    startHttpServer();
}

}
