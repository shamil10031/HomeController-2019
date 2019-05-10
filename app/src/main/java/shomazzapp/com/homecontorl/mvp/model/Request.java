package shomazzapp.com.homecontorl.mvp.model;

import android.support.annotation.Nullable;

public class Request {

    public static final int LOGIN = 0;
    public static final int LOGOUT = 1;
    public static final int CREATE_USER = 2;
    public static final int VALIDATE_USER = 3;
    public static final int ADD_PHOTOS = 4;
    public static final int HOME_INFO = 5;
    public static final int AVALIABLE_DEVICES = 6;

    private static final String divider = " ";

    @Nullable
    private String requestMsg;
    private int requestCode;

    public Request(int requestCode, @Nullable String requestMsg) {
        this.requestMsg = requestMsg;
        this.requestCode = requestCode;
    }

    public static Request createAuthRequest(String login, String password) {
        return new Request(LOGIN, login + divider + password);
    }

    public static Request createRegistrationRequest(String name, String login, String password) {
        return new Request(CREATE_USER,
                name + divider + login + divider + password);
    }

    public static Request createAddFhotosRequest(String login){
        return new Request(ADD_PHOTOS, login);
    }

    public static Request createAviableDevicesRequest(String login) {
        return new Request(AVALIABLE_DEVICES, login);
    }

    public static Request createHomeInfoRequest() {
        return new Request(HOME_INFO, null);
    }

    public String getEncodeRequestCode() {
        return encodeMsg(requestCode + "");
    }

    public String getEncodeRequestMsg() {
        return encodeMsg(requestMsg);
    }

    @Nullable
    public String getRequestMsg() {
        return requestMsg;
    }

    public int getRequestCode() {
        return requestCode;
    }

    @Nullable
    private String encodeMsg(String msg) {
        if (msg != null) {
            int realSize = msg.length();
            char result[] = new char[Client.BYTES_COUNT];
            for (int i = 0; i < realSize; i++)
                result[i] = msg.charAt(i);
            for (int i = realSize; i < Client.BYTES_COUNT; i++)
                result[i] = '^';
            return new String(result);
        }
        return null;
    }

}
