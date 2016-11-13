package com.altarit.test.sockets.client;

import com.altarit.test.sockets.base.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static int counter = 0;
    private int id = counter++;
    private static int threadcount = 0;

    public static int threadCount() {
        return threadcount;
    }

    public ClientThread(InetAddress addr) {
        System.out.println("Making client " + id);
        threadcount++;
        try {
            socket = new Socket(addr, Constants.SERVER_PORT);
        }
        catch (IOException e) {
            System.err.println("Socket failed");
            // Если создание сокета провалилось,
            // ничего ненужно чистить.
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            // Включаем автоматическое выталкивание:
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
            start();
        }
        catch (IOException e) {
            // Сокет должен быть закрыт при любой
            // ошибке, кроме ошибки конструктора сокета:
            try {
                socket.close();
            }
            catch (IOException e2) {
                System.err.println("Socket not closed");
            }
        }
        // В противном случае сокет будет закрыт
        // в методе run() нити.
    }

    public void run() {
        try {
            for (int i = 0; i < 25; i++) {
                out.println("Client " + id + ": " + i);
                String str = in.readLine();
                System.out.println(str);
            }
            out.println("END");
        }
        catch (IOException e) {
            System.err.println("IO Exception");
        }
        finally {
            // Всегда закрывает:
            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println("Socket not closed");
            }
            threadcount--; // Завершаем эту нить
        }
    }
}

public class MultiClient {
    static final int MAX_THREADS = 40;

    public static void main(String[] args) throws IOException,
            InterruptedException {
        InetAddress addr = InetAddress.getByName(null);
        while (true) {
            if (ClientThread.threadCount() < MAX_THREADS)
                new ClientThread(addr);
            Thread.currentThread().sleep(100);
        }
    }
}
