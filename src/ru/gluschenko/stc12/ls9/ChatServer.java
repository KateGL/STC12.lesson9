package ru.gluschenko.stc12.ls9;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ChatServer {
    static String URL = "127.0.0.1";
    static Integer SERVER_PORT = 4999;
    static ArrayList<ChatServerThread> clients;

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(SERVER_PORT);
        clients = new ArrayList<>();

        while(true) {
            //ждем подключения клиента
            Socket socket = server.accept();
            System.out.println("Client connected");

            //получаем от него служебную информацию
            Integer port = ChatServer.getClientInfo(socket);
            //запускаем поток для его прослушки
            ChatServer.appendClientThread(socket, port);
        }

    }

    static void appendClientThread(Socket socket, Integer port){
        System.out.println("appendClientThread for:"+port);
        ChatServerThread client = new ChatServerThread(socket, port);
        client.start();
        clients.add(client);
    }

    static Integer getClientInfo(Socket socket) throws IOException {
        Integer port = null;
        InputStream fromClient = socket.getInputStream();
        BufferedReader clientReader = new BufferedReader(new InputStreamReader(fromClient));
        //ловим номер порта от подключившегося клиента
        while (port == null) {
            String messageClient = clientReader.readLine();
            port = ChatServer.getPortFromMessage(messageClient);
        }
        return port;
    }

    static Integer getPortFromMessage(String message) {
        String[] tokens = message.split("[:]");
        if (tokens.length == 2) {
            String command = tokens[0];

            if (command.equals("local_port")) {
                return Integer.parseInt(tokens[1]);
            }
        }
        return null;
    }

    static void sendMessageForClients(String message, ChatServerThread initiator){
        //рассылка сообщения по клиентам
        System.out.println("sending messages");
        for(ChatServerThread client: clients){
            if(client.equals(initiator)){
                continue;
            }
            System.out.println(client.getName());
            client.sendMessage(message);
        }
    }
}
