package shomazzapp.com.homecontorl.common;

public class Response {

    public static final int OK = 0;
    public static final int TIMEOUT_WAITING = 1;
    public static final int CONNECTION_ERROR = 2;

    private int responseCode;
    private String response;

    public Response(int responseCode, String response) {
        this.response = response;
        this.responseCode = responseCode;
    }

    public int getResponceCode() {
        return responseCode;
    }

    public String getResponse() {
        return response;
    }

}
