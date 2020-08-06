package server;

import java.util.HashMap;
import java.util.Map;

public class Wrapper {
    private HttpServlet servlet;

    private String uri;

    public Wrapper(String uri, HttpServlet servlet){
        this.uri = uri;
        this.servlet = servlet;
    }

    public HttpServlet getServlet() {
        return servlet;
    }

    public String getUri() {
        return uri;
    }
}
