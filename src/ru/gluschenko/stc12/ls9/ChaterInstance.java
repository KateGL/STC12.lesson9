package ru.gluschenko.stc12.ls9;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChaterInstance extends Thread{
    private Integer myPort;
    private String name;
    private ServerSocket serverSocket;


    public ChaterInstance() {
        //ставим значение по-умолчанию, но надо задать командой
        this.myPort = Chater.LOCAL_PORT;
    }

    public void setPort(Integer port){
        this.myPort=port;
        try {
            serverSocket = new ServerSocket(this.myPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setChaterName(String name){
        this.name = name;
    }

    public String getChaterName(){
        return this.name;
    }

    public Integer getChaterPort(){
        return this.myPort;
    }


    @Override
    public void run() {
        System.out.println("start chat thread");
        try {
            //ловим то что нам шлет ChatServer, он у нас один, так что
            Socket socket = serverSocket.accept();
            System.out.println("Server connected");

            InputStream fromClient = socket.getInputStream();
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(fromClient));

            String message;
            while ((message = clientReader.readLine()) != null){
                this.processMessage(message);
            }

        } catch (IOException e) {
                e.printStackTrace();
        }
        System.out.println("stop thread");
    }

    void processMessage(String message){
        //тут могут быть разные команды, а могут быть просто сообщения
        System.out.println(message);
    }


}
