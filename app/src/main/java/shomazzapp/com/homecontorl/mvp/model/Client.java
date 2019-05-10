package shomazzapp.com.homecontorl.mvp.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.NoRouteToHostException;
import java.net.Socket;

import shomazzapp.com.homecontorl.common.interfaces.ClientListener;

public class Client {

    private static final String NETWORK_TAG = "Network";

    public static final String HOST = "192.168.43.243";
    public static final int PORT = 8884;
    public static final int BYTES_COUNT = 1024;

    private final String host;
    private final int port;
    private Socket socket;
    private ClientListener listenner;

    public Client(ClientListener listenner, String host, int port) {
        this.host = host;
        this.port = port;
        this.listenner = listenner;
    }

    public void sendRequestForResponse(Request request) {
        Thread thread = new Thread(() -> {
            try {
                Log.d(NETWORK_TAG, "Connecting to server...");
                socket = new Socket(host, port);

                Log.d(NETWORK_TAG, "Connected"
                        + "\nHost : " + host
                        + "\nPort : " + port);
                request(request, socket);

                listenner.reciveResponse(getResponse(socket));
            } catch (NoRouteToHostException e) {
                listenner.reciveResponse(new Response(
                        Response.NO_ROUTE_TO_HOST, null));
                e.printStackTrace();
            } catch (IOException e) {
                listenner.reciveResponse(new Response(
                        Response.CONNECTION_ERROR, null));
                e.printStackTrace();
            } finally {
                closeSocket(socket);
            }
        });
        thread.start();
    }

    public void sendRequest(Request request) {
        Thread thread = new Thread(() -> {
            try {
                Log.d(NETWORK_TAG, "Connecting to server...");
                socket = new Socket(host, port);

                Log.d(NETWORK_TAG, "Connected"
                        + "\nHost : " + host
                        + "\nPort : " + port);
                request(request, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void request(Request request, Socket socket) throws IOException {
        Log.d(NETWORK_TAG, "Request msg : " + request.getRequestMsg());
        OutputStream oos = socket.getOutputStream();
        Log.d(NETWORK_TAG, "Sending request code : " + request.getRequestCode());
        if (!socket.isOutputShutdown())
            oos.write(request.getEncodeRequestCode().getBytes());
        Log.d(NETWORK_TAG, "Sending request : " + request.getRequestMsg());
        if (!socket.isOutputShutdown() && request.getEncodeRequestMsg() != null)
            oos.write(request.getEncodeRequestMsg().getBytes());
    }

    private void closeSocket(@Nullable Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.getOutputStream().flush();
                socket.getOutputStream().close();
                socket.getInputStream().close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.w(NETWORK_TAG, "Cannot close socket! Socket is "
                    + (socket == null ? "null" : "closed"));
        }
    }

    @NonNull
    private Response getResponse(Socket socket) throws IOException {
        Log.d(NETWORK_TAG, "Waiting for response...");
        InputStream inputStream = socket.getInputStream();
        byte[] data = new byte[BYTES_COUNT];
        if (!socket.isInputShutdown())
            inputStream.read(data);
        inputStream.close();
        Response response = new Response(new String(data));
        Log.d(NETWORK_TAG, "Response : " + response.toString());
        return response;
    }
}
