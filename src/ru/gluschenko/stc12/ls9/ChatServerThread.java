package ru.gluschenko.stc12.ls9;

import java.io.*;
import java.net.Socket;

public class ChatServerThread extends Thread{
    /***
     * Класс для общения с клиентом
     */
    private Socket clientSocketForRead;
    private Integer clientPortForRead;
    private Socket clientSocketForWrite;

    public ChatServerThread(Socket socket, Integer port) {
        //ставим значение по-умолчанию, но надо задать командой
        this.clientSocketForRead = socket;
        this.clientPortForRead = port;
    }

    @Override
    public void run() {
        System.out.println("Start listening thread for client:"+clientSocketForRead.getInetAddress() +":"+clientPortForRead);
        InputStream fromClient = null;
        BufferedReader clientReader = null;

        try {
            clientSocketForWrite = new Socket(clientSocketForRead.getInetAddress(), clientPortForRead);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            fromClient = this.clientSocketForRead.getInputStream();
            clientReader = new BufferedReader(new InputStreamReader(fromClient));
        } catch (IOException e) {
            e.printStackTrace();
        }


        String message;
        try {
            while ((message = clientReader.readLine()) != null){
                //тут можно вставить какую-нибудь обработку служебных команд

                System.out.println("Echo: " + message);
                ChatServer.sendMessageForClients(message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        /**
         * Отправка соощения клиенту с сервера
         */
        BufferedWriter bufferedWriter = null;
        try {

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
