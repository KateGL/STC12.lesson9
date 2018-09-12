package ru.gluschenko.stc12.ls9;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Chater {
    public static String getURL() {
        return URL;
    }

    static String URL = "127.0.0.1";
    static Integer LOCAL_PORT = 5000;
    static ChaterInstance chat = new ChaterInstance();
    static Socket socket = null;

    private Chater() {

    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String message;

        System.out.println("Пожалуйста, введите имя клиента:");
        message = scanner.nextLine();
        Chater.setChaterName(message);

        System.out.println("Пожалуйста, введите порт клиента:");
        message = scanner.nextLine();
        Chater.connectLocalPort(message);

        //подключаемся к серверу
        try {
            socket = new Socket(ChatServer.URL, ChatServer.SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //отправляем ему информацию о нашем порте, чтобы он мог нам слать ответ
        OutputStreamWriter serverOutput = null;
        try {
            serverOutput = new OutputStreamWriter(socket.getOutputStream());
            //если это не служебная команда, то отправляем на сервер
            BufferedWriter bufferedWriter = new BufferedWriter(serverOutput);
            bufferedWriter.write("local_port:"+message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!(message = scanner.nextLine()).equals("exit")) {
            //тут мы отправляем сообщения на сервер
            Chater.processMessage(message);
        }
    }


    static void connectLocalPort(String port){
        Chater.LOCAL_PORT = Integer.parseInt(port);
        //перенаправляем listener на этот порт
        Chater.chat.setPort(Chater.LOCAL_PORT);
        Chater.chat.start();
    }

    static void setChaterName(String name){
        Chater.chat.setChaterName(name);
    }

    static void processMessage(String message) {

        OutputStreamWriter serverOutput = null;
        try {
            serverOutput = new OutputStreamWriter(socket.getOutputStream());
            //если это не служебная команда, то отправляем на сервер
            BufferedWriter bufferedWriter = new BufferedWriter(serverOutput);
            bufferedWriter.write(chat.getChaterName()+"->"+message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
