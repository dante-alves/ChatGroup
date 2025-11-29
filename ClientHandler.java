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

    // Nesse método, buscamos executá-lo em threads diferentes, para que o programa não fique
    // travado, esperando por mensagens de outros usuários
    @Override
    public void run() {
        String mensagemClient;
        // Haverá recebimento de mensagens enquanto houver conexão com o client
        while (socket.isConnected()) {
            try {
                // Fazendo a leitura do que um client enviou para que o mesmo seja enviado para os outros
                mensagemClient = bufferedReader.readLine();
                transmiteMensagem(mensagemClient);

                Server.addMessage(mensagemClient); // salvando no histórico de mensagens
            } catch (IOException e) {
                Terminate(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    // Método para enviar uma mensagem para todos os clientHandler, fazendo com que todo usuário receba a mensagem
    public void transmiteMensagem(String msgFila) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                // Bloqueando a transmissão da mensagem (de login, por exemplo) para o usuário que a gerou
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(msgFila);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                Terminate(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    // Método com a função de remover o Client caso o mesmo apresente erros ou desconexão
    public void removeClientHandler() {
        clientHandlers.remove(this);
        transmiteMensagem("SERVER: " + clientUsername + " saiu do Chat!");
    }

    public void Terminate(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        // Para que nenhuma outra mensagem seja transmitida por causa de erros ou corte de conexão, o Client é desconectado.
        removeClientHandler();

        Server.saveConversationAsHTML();

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
}