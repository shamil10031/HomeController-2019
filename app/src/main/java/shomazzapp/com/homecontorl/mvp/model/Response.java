package shomazzapp.com.homecontorl.mvp.model;

public class Response {

    public static final int OK = 0;
    public static final int TIMEOUT_WAITING = 1;
    public static final int CONNECTION_ERROR = 2;
    public static final int NO_ROUTE_TO_HOST = 3;

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
