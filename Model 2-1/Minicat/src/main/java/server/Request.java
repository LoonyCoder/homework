package server;


import java.io.IOException;
import java.io.InputStream;

/**
 * 把请求信息封装为Request（根据InputStream输入流封装）
 */
public class Request {

    private String method; // 请求方式，比如GET/POST
    private String url;  // 例如 /,/index.html

    private String host; // 域名

    private Context context;

    private Host hostObj;

    public Host getHostObj() {
        return hostObj;
    }

    public void setHostObj(Host hostObj) {
        this.hostObj = hostObj;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private InputStream inputStream;  // 输入流，其他属性从输入流中解析出来

    private Wrapper wrapper;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Request() {
    }

    public String getHost() {
        return host;
    }

    public Wrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(Wrapper wrapper) {
        this.wrapper = wrapper;
    }

    // 构造器，输入流传入
    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        // 从输入流中获取请求信息
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }

        byte[] bytes = new byte[count];
        inputStream.read(bytes);

        String inputStr = new String(bytes);
        String[] split = inputStr.split("\\n");
        // 获取第一行请求头信息
        String firstLineStr = split[0];  // GET / HTTP/1.1

        String[] strings = firstLineStr.split(" ");

        this.method = strings[0];
        this.url = strings[1];
        // 获取第二行host信息
        String s = split[1];
        host = s.split(" ")[1].split(":")[0];
        System.out.println("=====>>method:" + method);
        System.out.println("=====>>url:" + url);
        System.out.println("=====>>host:" + host);


    }
}
