package shomazzapp.com.homecontorl.mvp.model;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;


import shomazzapp.com.homecontorl.common.Response;

public class Client {

    private static final int WAIT_TIME = 5000;
    private static final String NETWORK_TAG = "Network";

    public static final String HOST = "192.168.43.243";
    public static final int PORT = 8888;

    private final String host;
    private final int port;
    private Socket socket;
    private ClientListener listenner;

    public Client(ClientListener listenner, String host, int port) {
        this.host = host;
        this.port = port;
        this.listenner = listenner;
    }
    
    public void postMsg(String msg){
        Thread thread = new Thread(() -> {
            try {
                Log.d(NETWORK_TAG, "Connecting to server...");
                socket = new Socket(host, port);

                Log.d(NETWORK_TAG, "Connected"
                        + "\nHost : " + host
                        + "\nPort : " + port);
                request(msg, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void postMsgForResponse(String msg) {
        Thread thread = new Thread(() -> {
            try {
                Log.d(NETWORK_TAG, "Connecting to server...");
                socket = new Socket(host, port);

                Log.d(NETWORK_TAG, "Connected"
                        + "\nHost : " + host
                        + "\nPort : " + port);

                request(msg, socket);
                listenner.reciveResponse(getResponse(socket));
            } catch (ConnectException e) {
                e.printStackTrace();
                listenner.reciveResponse(new Response(
                        Response.TIMEOUT_WAITING, null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void request(String msg, Socket socket) throws IOException {
        Log.d(NETWORK_TAG, "Request msg : " + msg);
        String request = stringDecode(msg, 1024);
        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        if (!socket.isOutputShutdown())
            oos.writeChars(request);
        oos.flush();
        oos.close();
    }

    @NonNull
    private Response getResponse(Socket socket) throws IOException {
        Log.d(NETWORK_TAG, "Waiting for response...");
        long start = System.currentTimeMillis();
        long elapsed = 0;
        byte[] data = new byte[1024];
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        //TODO: нужно ли добавлять условие data.length < 1024 ?
        while (!socket.isInputShutdown() && elapsed < WAIT_TIME) {
            inputStream.read(data);
            elapsed = System.currentTimeMillis() - start;
        }
        inputStream.close();
        Response response = new Response(elapsed >= WAIT_TIME ?
                Response.TIMEOUT_WAITING : Response.OK, new String(data));
        Log.d(NETWORK_TAG, "Response : " + response.getResponse());
        return response;
    }

    private String stringDecode(String word, int length) {
        int realSize = word.length();
        char result[] = new char[length];
        for (int i = 0; i < realSize; i++)
            result[i] = word.charAt(i);
        for (int i = realSize; i < length; i++)
            result[i] = '^';
        return new String(result);
    }
}
