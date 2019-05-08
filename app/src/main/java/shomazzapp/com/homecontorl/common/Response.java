package shomazzapp.com.homecontorl.common;

public class Response {

    public static int OK = 0;
    public static int TIMEOUT_WAITING = 1;

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
