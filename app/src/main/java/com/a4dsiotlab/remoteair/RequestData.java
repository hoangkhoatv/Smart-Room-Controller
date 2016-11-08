package com.a4dsiotlab.remoteair;

/**
 * Created by hoangkhoatv on 11/5/16.
 */

import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by tommy on 5/26/16.
 */
public class RequestData {
    private Thread thread;
    private ConcurrentLinkedQueue<String> msgQueue;
    private ConcurrentLinkedQueue<String> rcvMsgQueue;
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;

    private class Job implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket("10.0.2.2",6969);
                writer = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
                while (Thread.currentThread().isAlive()) {
                    try {
                        if (!msgQueue.isEmpty()) {
                            writer.println(msgQueue.remove());
                            writer.flush();
                        }
                        // receive msg from server if exist
                        if (scanner.hasNextLine()) {
                            rcvMsgQueue.add(scanner.nextLine());
                        }
                    } catch (Exception e) {
                        Log.d("Ex", e.getMessage());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public RequestData() {
        msgQueue = new ConcurrentLinkedQueue<>();
        rcvMsgQueue = new ConcurrentLinkedQueue<>();
        thread = new Thread(new Job());
        thread.start();
    }
    public void close() {
        thread.interrupt();
    }

    public void log(String message) {
        msgQueue.add(message);
    }

    public String getMsg() {
        if (!rcvMsgQueue.isEmpty()) {
            return rcvMsgQueue.poll();
        } else {
            return null;
        }
    }
}