package com.a4dsiotlab.remoteair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by hoangkhoatv on 11/8/16.
 */

public class ExchangeData {
    private Thread thread;
    private Socket socket;
    BufferedReader in = null;
    PrintWriter out = null;
   // private Handler handler;
    private String msgQueue;
    private String rcvMsgQueue;
    String iPAddress;
    int port;


    private class Job implements Runnable {

        @Override
        public void run() {

                    while (Thread.currentThread().isAlive()){
                       if (!msgQueue.equals("")){
                            if(out!=null){
                                out.println(msgQueue);
                                out.flush();
                                msgQueue = "";

                           }
                        }

                    }


        }
    }

    class Sender extends Thread {
        private PrintWriter out;
        private BufferedReader in;
       // private Handler handler;
        public Sender(BufferedReader input,PrintWriter output) {
            this.out=output;
            this.in=input;
           // this.handler=handler;
        }

        public void run() {
            try {
                // Read messages from the server
                String message;
                while ((message = in.readLine()) != null) {
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
            this.socket = new Socket(iPAddress,port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
        }



        /*handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Bundle bundle = msg.getData();
                        String string = bundle.getString("myKey");
                        rcvMsgQueue = string;

                    }
                };
            }
        };*/
        Sender sender = new Sender(in,out);
        sender.setDaemon(true);
        sender.start();

        thread = new Thread(new Job());
        thread.start();
    }
    public void close() {
        thread.interrupt();
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
