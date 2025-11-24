// É aberto um Socket > É aberto um InputStream e OutputStream para o Socket > Lê-se e escreve-se nas Streams de acordo com os comandos do Server > Streams são fechadas > Socket é fechado

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

// Quando um novo usuário é conectado, o server gera uma nova thread para ele, possibilitando assim a existência de múltiplos usuários simultâneamente

public class ClientHandler implements Runnable {

    // Lista com todas as threads conectadas à usuários para que cada mensagem seja enviada para os usuários e suas respectivas threads
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    // Socket para realizar conexão e buffers para gerenciamento (recebimento e envio) de dados
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    // Criação do handler de client/usuário do socket repassado pelo server
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // Quando um usuário conecta-se, seu nome é anunciado no chat.
            this.clientUsername = bufferedReader.readLine();
            // O novo clientHandler é adicionado ao array para que ele possa receber mensagens
            clientHandlers.add(this);
            transmiteMensagem("SERVER: " + clientUsername + " entrou no Chat!");
        } catch (IOException e) {
            Terminate(socket, bufferedReader, bufferedWriter);
        }
    }
}