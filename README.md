# ChatGroup – Servidor de Chat + Servidor HTTP

Este projeto é uma aplicação simples de **chat em Java usando sockets**, composta por:

- **Server.java** – gerencia conexões e mensagens dos clientes  
- **ClientHandler.java** – trata cada cliente conectado  
- **Client.java** – conecta ao servidor e envia mensagens  
- **Servidor HTTP (porta 8080)** – serve o arquivo `chat.html` contendo o histórico da conversa  


---

## Estrutura do Projeto

```
ChatGroup/
│
├── Server.java
├── Client.java
├── ClientHandler.java
└── README.md
```

> O arquivo `chat.html` será gerado automaticamente na pasta raiz após o encerramento do chat.

---

##  Como Executar o Projeto

### 1. Compile os arquivos

Na pasta do projeto:

```bash
javac *.java
```

Isso gera os `.class` dentro da raíz.

### 2. Rode o servidor

```bash
java Server
```

### 3. Rode quantos clientes quiser

Em outros terminais dentro da pasta raíz:

```bash
java Client
```

---

## Como Encerrar o Chat e Gerar o HTML

No console do **Server**, digite:

```
sair
```

O servidor fará:

1. Encerrar chat  
2. Salvar `chat.html`  
3. Iniciar automaticamente o servidor HTTP (porta 8080)

---

## 🌐 Como Acessar o Histórico do Chat

Depois que o servidor HTTP iniciar, abra:

```
http://localhost:8080/chat.html
```

Você verá cada mensagem em um `<p>` separado.

---

## Teste Rápido

1. Abra **Server**  
2. Abra 2 instâncias de **Client**  
3. Envie mensagens  
4. No servidor, digite `sair`  
5. Abra: http://localhost:8080/chat.html  
6. Veja o histórico formatado

---

## Requisitos

- Java 8+  
- Porta **1234** livre para o chat  
- Porta **8080** livre para o servidor HTTP  

---
