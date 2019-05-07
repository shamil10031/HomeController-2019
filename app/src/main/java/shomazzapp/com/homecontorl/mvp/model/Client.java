package shomazzapp.com.homecontorl.mvp.model;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private final String host;
    private final int port;
    private Socket socket;

    public Client (String host, int port){
        this.host = host;
        this.port = port;
        this.socket = new Socket();
    }

    public void postString(String str1) throws IOException {
        socket = new Socket(host,port);
        String str = stringReform(str1, 1024);
        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        if (!socket.isOutputShutdown()) {
            oos.writeChars(str);
            oos.flush();
            oos.close();
        }
    }

    public String stringReform(String word, int length){
        int realSize = word.length();
        char result [] = new char [length];
        for (int i = 0; i < realSize; i++)
            result[i] = word.charAt(i);
        for (int i = realSize; i < length; i++)
            result[i] = '^';
        return new String(result);
    }

    @NonNull
    public String reciveString() throws IOException {
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        byte[] data = new byte[1024];
        if(!socket.isInputShutdown())
            inputStream.read(data);
        return new String(data);
    }
}
