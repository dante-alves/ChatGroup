# Chat Group em Java (Eclipse Project)

Este projeto é um **chat em tempo real** usando **Java Sockets** e **Threads**, permitindo que vários clientes conversem simultaneamente através de um servidor central.

O projeto foi criado no **Eclipse**, e sua estrutura é:

ChatGroup/
└── src/
└── (default package)
├── Client.java
├── ClientHandler.java
└── Server.java


---

##  Requisitos

- **Java JDK 8+**
- **Eclipse IDE**
- Permissão para usar a porta **1234**

---

##  Importando o Projeto no Eclipse

1. Abra o Eclipse  
2. Vá em **File → Open Projects from File System...**
3. Clique em **Directory...**
4. Selecione a pasta raiz **ChatGroup/**
5. Clique em **Finish**

---

##  Executando o Servidor (Server.java)

1. No *Package Explorer*, abra:
    ChatGroup → src → Server.java
2. Clique com o botão direito no arquivo  
3. Vá em **Run As → Java Application**

O servidor começará a aguardar conexões na porta **1234**.

---

##  Executando os Clientes (Client.java)

Para cada cliente que quiser abrir:

1. Abra:
    ChatGroup → src → Client.java
2. Clique com o botão direito  
3. Selecione **Run As → Java Application**
4. Digite um **username** quando solicitado

Abra vários clientes para simular um chat real.

---

##  Testando a Comunicação

Com o servidor em execução:

- Abra vários Clients
- Envie mensagens por cada um

As mensagens aparecerão em todos os clientes conectados.

Exemplo:

Cliente 1: João: Olá amigos!
Cliente 2: Maria: Oi João!
Cliente 3: Ana: Cheguei!

---

##  Encerrando o Chat

### Encerrar o servidor
- Feche a aba Console no Eclipse  
**ou** clique no botão vermelho **Terminate (⛔)**

### Encerrar um cliente
- Feche a execução do Client  
- O servidor exibirá:
    SERVER: <nome> saiu do Chat!

---

##  Como funciona internamente

- O **Server** usa `ServerSocket` para aceitar novas conexões.
- Para cada conexão é criado um **ClientHandler**, executado em uma nova **thread**.
- O **ClientHandler**:
- recebe as mensagens do cliente
- retransmite para todos os outros
- O **Client** usa:
- uma thread para **receber**
- a thread principal para **enviar**

A comunicação é feita com:
- `BufferedReader` (recebimento)
- `BufferedWriter` (envio)

---