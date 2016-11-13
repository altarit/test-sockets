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

public class SimpleClient {

    public static void main(String[] args) throws IOException {
        // Передаем null в getByName(), получая
        // специальный IP адрес "локальной заглушки"
        // для тестирования на машине без сети:
        InetAddress addr = InetAddress.getByName(null);
        // Альтернативно, вы можете использовать
        // адрес или имя:
        // InetAddress addr =
        // InetAddress.getByName("127.0.0.1");
        // InetAddress addr =
        // InetAddress.getByName("localhost");
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, Constants.SERVER_PORT);
        // Помещаем все в блок try-finally, чтобы
        // быть уверенным, что сокет закроется:
        try {
            System.out.println("socket = " + socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Вывод автоматически Output быталкивается PrintWriter'ом.
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            for (int i = 0; i < 10; i++) {
                out.println("howdy " + i);
                String str = in.readLine();
                System.out.println(str);
            }
            out.println("END");
        } finally {
            System.out.println("closing...");
            socket.close();
        }
    }
}
