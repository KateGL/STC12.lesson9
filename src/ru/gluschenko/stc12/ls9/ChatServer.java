package ru.gluschenko.stc12.ls9;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {
    static String URL = "127.0.0.1";
    static Integer SERVER_PORT = 4999;
    static ArrayList<ChatServerThread> clients;

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String message;
        ServerSocket server = new ServerSocket(SERVER_PORT);
        clients = new ArrayList<>();

        while(true) {
            Socket socket = server.accept(); //это в отдельный класс и в поток
            System.out.println("Client connected");
            //ловим команды от подключившегося клиента
            Integer port = null;
            while (port == null) {
                System.out.println(port);
                InputStream fromClient = socket.getInputStream();
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(fromClient));
                String messageClient = clientReader.readLine();
                System.out.println("mess"+messageClient);
                System.out.println(messageClient);
                port = ChatServer.getPortFromMessage(messageClient);
            }
            ChatServer.appendClientThread(socket, port);
        }

    }

    static void appendClientThread(Socket socket, Integer port){
        System.out.println("appendClientThread");
        ChatServerThread client = new ChatServerThread(socket, port);
        client.start();
        clients.add(client);
    }

    /**
     * Мы можем вводить служебные сообщения. Примеры
     * local_port:5002
     */
    static Integer getPortFromMessage(String message) {
        String[] tokens = message.split("[:]");
        if (tokens.length == 2) {
            String command = tokens[0];

            if (command.equals("local_port")) {
                System.out.println("local_port:" + tokens[1]);
                return Integer.parseInt(tokens[1]);
            }
        }
        return null;
    }

    static void sendMessageForClients(String message, ChatServerThread initiator){
        //рассылка сообщения по
        System.out.println("sending messages");
        for(ChatServerThread client: clients){
            System.out.println(client.getName());
            if(client.equals(initiator)){
                //System.out.println("equals initiator");
                continue;
            }
            client.sendMessage(message);
        }
    }
}
