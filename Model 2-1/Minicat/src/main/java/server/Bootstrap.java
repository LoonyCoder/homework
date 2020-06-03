package server;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Minicat的主类
 */
public class Bootstrap {


    //定义socket监听的端口号
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private Mapper mapper = new Mapper();


    /**
     * minicat启动需要初始化展开的一些操作
     */
    public void start() throws Exception {

//        //加载解析相关的配置，web.xml
//        loadServlet();

        // 加载解析封装Mapper对象
        loadMapper();

//        //加载解析server.xml
//        loadServerXml();


        //定义一个线程池
        int corePoolSize = 10;
        int maximumPoolSize = 50;
        long keepAliveTime = 100l;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue,threadFactory,handler);



        //完成minicat v1.0版本
        //需求：（浏览器请求http://localhost:8080，返回一个固定字符串"Hello,Minicat!"到页面）
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Minicat started on port : " + port);

//        while (true){
//            Socket socket = serverSocket.accept();
//            //有了socket，接收到请求
//            OutputStream outputStream = socket.getOutputStream();
//            String data = "Hello,Minicat!";
//            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
//            outputStream.write(responseText.getBytes());
//            socket.close();
//        }

//        //完成minicat v2.0版本
//        //需求：封装Request和Response对象，返回html静态资源文件
//
//        while (true){
//            Socket socket = serverSocket.accept();
//            InputStream inputStream = socket.getInputStream();
//
//            //封装request和response对象
//            Request request = new Request(inputStream);
//            Response response = new Response(socket.getOutputStream());
//
//            response.outputHtml(request.getUrl());
//
////            请求信息：GET / HTTP/1.1
////            Host: localhost:8080
////            Upgrade-Insecure-Requests: 1
////            Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
////            User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1 Safari/605.1.15
////            Accept-Language: zh-cn
////            Accept-Encoding: gzip, deflate
////            Connection: keep-alive
//
//            socket.close();
//
//        }


//        /**
//         * 完成minicat 3.0版本
//         * 需求：可以请求动态资源（Servlet）
//         */
//        while (true) {
//            Socket socket = serverSocket.accept();
//            InputStream inputStream = socket.getInputStream();
//
//            //封装request和response对象
//            Request request = new Request(inputStream);
//            Response response = new Response(socket.getOutputStream());
//
//
//            //静态资源处理
//            if(servletMap.get(request.getUrl()) == null){
//                response.outputHtml(request.getUrl());
//            }else{ //动态资源处理
//                HttpServlet httpServlet = servletMap.get(request.getUrl());
//                httpServlet.service(request,response);
//            }
//
//            socket.close();
//
//        }

        /**
         * 完成minicat 3.0版本 多线程改造(不使用线程池)
         */
//        while (true) {
//            Socket socket = serverSocket.accept();
//            RequestProcessor requestProcessor = new RequestProcessor(socket,servletMap);
//            requestProcessor.start();
//        }

        /**
         * 多线程改造（使用线程池）
         */
        while (true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,mapper);
//            requestProcessor.start();
            threadPoolExecutor.execute(requestProcessor);
        }

    }


    private Map<String,HttpServlet> servletMap = new HashMap<String,HttpServlet>();


    /**
     * 初始化Mapper对象
     */
    private void loadMapper() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            // 解析端口号
            this.port = Integer.parseInt(rootElement.element("service").element("connector").attributeValue("port"));
            // 解析Engine
            List<Element> hosts = rootElement.element("service").element("Engine").elements("Host");
            // 遍历所有的host
            hosts.forEach(hostEle->{
                String hostName = hostEle.attributeValue("name");
                String appBase = hostEle.attributeValue("appBase");
                Host host = new Host();
                host.setAppBase(appBase);
                mapper.addHost(hostName,host);
                // 解析host下的context
                File file = new File(appBase);
                File[] files = file.listFiles();
                for (File contextFile : files) {
                    String contextName = contextFile.getName();
                    Context context = new Context();
                    host.addContext(contextName,context);
                    // 解析应用目录，初始化类加载器
                    URL url = null;
                    try {
                        url = contextFile.toURI().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
                    context.setClassLoader(urlClassLoader);
                    // 解析web.xml
                    if(contextFile.isDirectory()){
                        File[] webFiles = contextFile.listFiles((dir, name) -> {
                            return name.equals("web.xml");
                        });
                        File webFile = webFiles[0];
                        try {
                            // 解析web.xml 封装wrapper，context对象
                            loadServerXml(webFile,urlClassLoader,context);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载解析web.xml，初始化Servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
               // <servlet-name>lagou</servlet-name>
                String servletName = servletNameElement.getStringValue();
                Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
               // <servlet-class>server.LagouServlet</servlet-class>
                String servletClass = servletClassElement.getStringValue();


                //根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                //   /lagou
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();


                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());

            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }



    /**
     * 加载解析web.xml，初始化Servlet，wrapper，封装context
     */
    private void loadServerXml(File file, ClassLoader classLoader,Context context) throws FileNotFoundException {
        InputStream resourceAsStream = new FileInputStream(file);
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element =  selectNodes.get(i);
                // <servlet-name>lagou</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                // <servlet-class>server.LagouServlet</servlet-class>
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();


                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /lagou
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                // 加载类并实例化
                HttpServlet servlet = (HttpServlet) classLoader.loadClass(servletClass).newInstance();
                // 实例化wrapper并装入context中
                Wrapper wrapper = new Wrapper(urlPattern, servlet);
                context.addWrapper(urlPattern,wrapper);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Minicat的程序启动入口
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
