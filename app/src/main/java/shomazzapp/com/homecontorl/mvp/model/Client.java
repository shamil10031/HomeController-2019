package shomazzapp.com.homecontorl.mvp.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import shomazzapp.com.homecontorl.common.interfaces.ClientListener;

public class Client {

    private static final String NETWORK_TAG = "Network";

    public static final String HOST = "192.168.43.243";
    public static final int PORT = 8887;
    private int UDP_PORT;
    public static final int BYTES_COUNT = 1024;
    public static final int CHUNK_SIZE = 40960;

    private final String host;
    private final int port;
    private Socket socket;
    private ClientListener listenner;
    private Object lock;

    private AtomicInteger photosLoaded = new AtomicInteger();

    public Client(ClientListener listenner, String host, int port) {
        this.host = host;
        this.port = port;
        this.listenner = listenner;
        photosLoaded.set(0);
    }

    public Thread sendPicBytes(byte[] pic, boolean closeSocket) {

        Log.d(NETWORK_TAG, "Send bitmap...");
        Thread thread = new Thread(() -> {
            try {
                sendPicSync(pic);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (closeSocket) {
                    closeSocket(socket);
                }
            }
        });
        thread.start();
        return thread;
    }

    public synchronized void sendPicSync(byte[] pic) throws InterruptedException, IOException{
        String encoded = Base64.encodeToString(pic, Base64.DEFAULT);

        int imageBytesCount = encoded.length();
        Log.d(NETWORK_TAG, "Sending bytes count = " + imageBytesCount);
        sendRequest(new Request(imageBytesCount, null), false).join();

        Log.d(NETWORK_TAG, "Sending bytes [" + imageBytesCount + "]");
        sendBytes(encoded);
        Log.d(NETWORK_TAG, "Sended!");
        listenner.reciveResponse(new Response(Response.BITMAP_SENDED,
                photosLoaded.incrementAndGet() + ""));
    }

    public Thread sendBitmap(Bitmap bitmap, boolean closeSocket) {

        Log.d(NETWORK_TAG, "Send bitmap...");
        Thread thread = new Thread(() -> {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byteArray = stream.toByteArray();
                bitmap.recycle();

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                int imageBytesCount = encoded.length();
                Log.d(NETWORK_TAG, "Sending bytes count = " + imageBytesCount);
                sendRequest(new Request(imageBytesCount, null), false).join();
                //sendRequestByUDP(new Request(imageBytesCount, null)).join();

                Log.d(NETWORK_TAG, "Sending bytes [" + imageBytesCount + "]");
                sendBytes(encoded);
                //sendBytesUDPByChanks(encoded.getBytes());
                Log.d(NETWORK_TAG, "Sended!");
                listenner.reciveResponse(new Response(Response.BITMAP_SENDED,
                        photosLoaded.incrementAndGet() + ""));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (closeSocket) {
                    closeSocket(socket);
                }
            }
        });
        thread.start();
        return thread;
    }

    private void sendBytes(String bytes) throws IOException {
        connectSocketIfNeed();
        Log.d(NETWORK_TAG, "Start sending bytes");
        Log.d(NETWORK_TAG, "Open OutPutStream...");
        OutputStream oos = socket.getOutputStream();
        Log.d(NETWORK_TAG, "Opened!");
        if (!socket.isOutputShutdown()) {
            Log.d(NETWORK_TAG, "Sending bytes...");
            oos.write(bytes.getBytes());

        }
        Log.d(NETWORK_TAG, "Sended!");
    }

    private void sendBytesUDP(String bytes) throws IOException {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(bytes.getBytes(),
                    bytes.getBytes().length,
                    InetAddress.getByName(HOST), UDP_PORT);
            ds.setBroadcast(true);
            ds.send(dp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }

    private void sendBytesUDP(byte[] bytes) throws IOException {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(bytes,
                    bytes.length,
                    InetAddress.getByName(HOST), UDP_PORT);
            ds.setBroadcast(true);
            ds.send(dp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }

    private void sendBytesUDPByChanks(byte[] bytes) throws IOException {
        int bytesCount = bytes.length;
        Log.d("CHUNK", "bytesCount = " + bytesCount);
        int chunksCount = bytesCount / CHUNK_SIZE;
        Log.d("CHUNK", "chunksCount = " + chunksCount);
        for (int i = 0; i < chunksCount; i++) {
            sendBytesUDP(Arrays.copyOfRange(bytes, CHUNK_SIZE * i, CHUNK_SIZE * (i + 1)));
        }
        Log.d("CHUNK", "last chunk from : " + CHUNK_SIZE * chunksCount + " to " + bytes.length);
        Log.d("CHUNK", "last chunk size : " + (-CHUNK_SIZE * chunksCount + bytes.length));
        sendBytesUDP(Arrays.copyOfRange(bytes, CHUNK_SIZE * chunksCount, bytes.length));
    }

    private void sendBytes(byte[] bytes) throws IOException {
        connectSocketIfNeed();
        Log.d(NETWORK_TAG, "Start sending bytes");
        Log.d(NETWORK_TAG, "Open OutPutStream...");
        OutputStream oos = socket.getOutputStream();
        Log.d(NETWORK_TAG, "Opened!");
        if (!socket.isOutputShutdown()) {
            Log.d(NETWORK_TAG, "Sending bytes...");
            oos.write(bytes);

        }
        Log.d(NETWORK_TAG, "Sended!");
    }

    public void sendRequestForResponse(Request request, boolean closeSocket) {
        Thread thread = new Thread(() -> {
            try {
                connectSocketIfNeed();
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
                if (closeSocket) {
                    closeSocket(socket);
                }
            }
        });
        thread.start();
    }

    public Thread sendRequestByUDP(Request request) {
        Thread thread = new Thread(() -> {
            DatagramSocket ds = null;
            try {
                ds = new DatagramSocket();

                Log.d(NETWORK_TAG, "Request msg : " + request.toString());
                Log.d(NETWORK_TAG, "Sending request code : " + request.getRequestCode());
                DatagramPacket dp = new DatagramPacket(request.getEncodeRequestCode().getBytes(),
                        request.getEncodeRequestCode().getBytes().length,
                        InetAddress.getByName(HOST), UDP_PORT);
                ds.setBroadcast(true);
                ds.send(dp);

                Log.d(NETWORK_TAG, "Sending request msg: " + request.getRequestMsg());
                if (request.getEncodeRequestMsg() != null) {
                    dp = new DatagramPacket(request.getEncodeRequestMsg().getBytes(),
                            request.getEncodeRequestMsg().getBytes().length,
                            InetAddress.getByName(HOST), UDP_PORT);
                    ds.setBroadcast(true);
                    ds.send(dp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ds != null) {
                    ds.close();
                }
            }
        });
        thread.start();
        return thread;
    }

    public Thread sendRequest(Request request, boolean closeSocket) {
        Thread thread = new Thread(() -> {
            try {
                connectSocketIfNeed();
                request(request, socket);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (closeSocket) {
                    closeSocket(socket);
                }
            }
        });
        thread.start();
        return thread;
    }

    private void request(Request request, Socket socket) throws IOException {
        Log.d(NETWORK_TAG, "Request msg : " + request.toString());
        Log.d(NETWORK_TAG, "Sending request code : " + request.getRequestCode());
        OutputStream oos = socket.getOutputStream();
        if (!socket.isOutputShutdown()) {
            oos.write(request.getEncodeRequestCode().getBytes());
            oos.flush();
        }
        Log.d(NETWORK_TAG, "Sending request msg: " + request.getRequestMsg());
        if (!socket.isOutputShutdown() && request.getEncodeRequestMsg() != null) {
            oos.write(request.getEncodeRequestMsg().getBytes());
            oos.flush();
        }
    }

    public void connectSocketIfNeed() throws IOException {
        Log.d(NETWORK_TAG, "Connecting to server...");
        if (socket == null || socket.isClosed()) {
            socket = new Socket(host, port);
            Log.d(NETWORK_TAG, "Connected"
                    + "\nHost : " + host
                    + "\nPort : " + port);
        } else {
            Log.d(NETWORK_TAG, "Already connected");
        }
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

    public void setUdpPort(int port) {
        this.UDP_PORT = port;
    }

    public Socket getSocket() {
        return socket;
    }

    @NonNull
    public Response getResponse(Socket socket) throws IOException {
        Log.d(NETWORK_TAG, "Waiting for response...");
        InputStream inputStream = socket.getInputStream();
        byte[] data = new byte[BYTES_COUNT];
        if (!socket.isInputShutdown()) {
            inputStream.read(data);
        }
        //inputStream.close();
        Response response = new Response(new String(clearByteArray(data), StandardCharsets.UTF_8));
        Log.d(NETWORK_TAG, "Response : " + response.toString());
        return response;
    }

    public byte[] clearByteArray(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }

}
