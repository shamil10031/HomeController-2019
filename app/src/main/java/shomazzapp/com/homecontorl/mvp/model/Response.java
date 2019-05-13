package shomazzapp.com.homecontorl.mvp.model;

public class Response {

    public static final int OK = 0;
    public static final int TIMEOUT_WAITING = 1;
    public static final int CONNECTION_ERROR = 2;
    public static final int NO_ROUTE_TO_HOST = 3;
    public static final int BITMAP_SENDED = 9;
    public static final int SUCCESS = 200;
    public static final int ERROR = 400;


    private int responseCode;
    private String message;

    public Response(int responseCode, String message) {
        this.message = message;
        this.responseCode = responseCode;
    }

    public Response(String response) {
        if (response.length() < 1) {
            responseCode = ERROR;
        } else {
            responseCode = Integer.parseInt(response.substring(0, 3));
            message = response.substring(3, response.length());
        }
    }

    public int getResponceCode() {
        return responseCode;
    }

    @Override
    public String toString() {
        return responseCode + " : " + message;
    }

    public String getMessage() {
        return message;
    }

}
