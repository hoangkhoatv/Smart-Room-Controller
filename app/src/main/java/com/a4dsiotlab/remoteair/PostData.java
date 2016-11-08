package com.a4dsiotlab.remoteair;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by hoangkhoatv on 11/5/16.
 */

public class PostData extends AsyncTask<String, Void, Void> {
   // String dstAddress;
    //int dstPost;
  //  String msgSend;
   /* PostData(String addr, int port, String msg){
        this.dstAddress = addr;
        this.dstPost = port;
        this.msgSend = msg;
    }*/
    @Override
    protected  Void doInBackground(String... params){
        try {
            try {

                Socket socket = new Socket("10.0.2.2",6969);
                PrintWriter outToServer = new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()
                        )
                );

                outToServer.println(params[0]);
                outToServer.flush();

            } catch (IOException e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.d("Error Post Data",e.getMessage());
            return null;
        }
        return null;
    }
}
