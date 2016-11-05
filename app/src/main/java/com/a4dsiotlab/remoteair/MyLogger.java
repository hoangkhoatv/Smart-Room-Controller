package com.a4dsiotlab.remoteair;

/**
 * Created by hoangkhoatv on 11/5/16.
 */

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by tommy on 5/26/16.
 */
public class MyLogger {
    private Thread thread;
    private ConcurrentLinkedQueue<String> msgQueue;
    private Socket socket;
    private PrintWriter writer;
    Handler updateDataHandler;

    private class Job implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket("10.0.2.2",6969);
                writer = new PrintWriter(socket.getOutputStream());
                while (Thread.currentThread().isAlive()) {
                    if (!msgQueue.isEmpty()) {
                        writer.println(msgQueue.remove());
                        writer.flush();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()){
                try {
                    ReceiveDataThread receiveDataThread = new ReceiveDataThread(socket);
                    new Thread(receiveDataThread).start();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        }
    }
    class ReceiveDataThread implements Runnable{
        private Socket clientSocket;
        private BufferedReader input;
        public ReceiveDataThread(Socket clientSocket){
            this.clientSocket = clientSocket;
            try {
                this.input = new BufferedReader(
                        new InputStreamReader(
                                this.clientSocket.getInputStream()
                        ));
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        @Override
        public void run(){
            while (!Thread.currentThread().isInterrupted()){
                try {
                    String read = input.readLine();
                    updateDataHandler.post(new updateDisplaySettings(read));

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    class updateDisplaySettings implements Runnable{
        private String msg;
        public updateDisplaySettings(String str){
            this.msg = str;
        }
        @Override
        public void run(){
            // xu ly sao???
        }
    }
    public MyLogger() {
        msgQueue = new ConcurrentLinkedQueue<>();
        updateDataHandler = new Handler() ;
        thread = new Thread(new Job());
        thread.start();
    }
    public void close() {
        thread.interrupt();
    }

    public void log(String message) {
        msgQueue.add(message);
    }
}