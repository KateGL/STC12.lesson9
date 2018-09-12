package ru.gluschenko.stc12.ls9;

import java.io.*;
import java.net.Socket;

public class ChatServerThread extends Thread{
    private Socket clientSocket;
    private Integer clientPort;
    private Socket clientSocketForWrite;

    public ChatServerThread(Socket socket, Integer port) {
        //ставим значение по-умолчанию, но надо задать командой
        this.clientSocket = socket;
        this.clientPort = port;
    }

    @Override
    public void run() {
        System.out.println("Start thread for client:"+clientSocket.getInetAddress() +":"+clientPort);
        InputStream fromClient = null;
        BufferedReader clientReader = null;

        try {
            clientSocketForWrite = new Socket(clientSocket.getInetAddress(), clientPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            fromClient = this.clientSocket.getInputStream();
            clientReader = new BufferedReader(new InputStreamReader(fromClient));
        } catch (IOException e) {
            e.printStackTrace();
        }


        String message;
        try {
            while ((message = clientReader.readLine()) != null){
                //ChatServer.processMessage(message);

                System.out.println("Echo: " + message);
                ChatServer.sendMessageForClients(message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        BufferedWriter bufferedWriter = null;
        try {

            System.out.println("port:"+clientPort);
            OutputStreamWriter serverOutput = new OutputStreamWriter(clientSocketForWrite.getOutputStream());

            bufferedWriter = new BufferedWriter(serverOutput);
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
