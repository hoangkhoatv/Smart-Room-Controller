package com.a4dsiotlab.remoteair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ExchangeData {
    private Sender sender;
    private Receiver receiver;
    private Socket socket;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String msgQueue;
    private String rcvMsgQueue;
    private String iPAddress;
    private int port;
    private boolean stop = false;


    private class Sender extends Thread {

        private PrintWriter out;

        public Sender(PrintWriter out) {
            this.out = out;
        }

        @Override
        public void run() {

            while (!stop) {
                if (!msgQueue.equals("")) {
                    if (this.out != null) {
                        this.out.println(msgQueue);
                        this.out.flush();
                        msgQueue = "";
                    }
                }

            }
        }
    }

    private class Receiver extends Thread {

        private BufferedReader in;

        public Receiver(BufferedReader input) {
            this.in = input;
        }

        public void run() {
            try {
                // Read messages from the server
                String message;
                while ((message = in.readLine()) != null && !stop) {
                    rcvMsgQueue = message;

                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public ExchangeData(String iPAddress, int port) {
        msgQueue = "";
        rcvMsgQueue = "";
        this.iPAddress = iPAddress;
        this.port = port;
        try {
            this.socket = new Socket(iPAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Create Thread Send and Receive
        Sender sender = new Sender(out);
        sender.setDaemon(true);
        sender.start();

        Receiver receiver = new Receiver(in);
        receiver.setDaemon(true);
        receiver.start();

    }

    public void close() throws IOException {
        this.socket.close();
        stop = true;
    }

    public void log(String message) {
        msgQueue = message;
    }


    public String getMsg() {
        if (!rcvMsgQueue.equals("")) {
            String temp = rcvMsgQueue;
            rcvMsgQueue = "";
            return temp;

        } else {
            return "";
        }
    }

}
