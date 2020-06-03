package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestProcessor extends Thread {

    private Socket socket;
//    private Map<String,HttpServlet> servletMap;
    private Mapper mapper;

//    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap,Mapper mapper) {
//        this.socket = socket;
//        this.servletMap = servletMap;
//        this.mapper = mapper;
//    }

    public RequestProcessor(Socket socket, Mapper mapper) {
        this.socket = socket;
        this.mapper = mapper;
    }


    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // 静态资源处理
            if(!mapper.map(request)) {
                response.outputHtml(request.getUrl());
            }else{
                // 动态资源servlet请求
                HttpServlet httpServlet = request.getWrapper().getServlet();
                httpServlet.service(request,response);
            }

            socket.close();

        }catch (Exception e) {
            e.printStackTrace();
        }


    }
}
