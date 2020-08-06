package com.lagou.edu.mvcframework.servlet;

import com.lagou.edu.mvcframework.annotations.*;
import com.lagou.edu.mvcframework.pojo.Handler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LgDispatcherServlet extends HttpServlet {

    //缓存扫描到的累的全限定类名
    private List<String> classNames = new ArrayList<>();
    private Properties properties = new Properties();

    //ioc容器
    private Map<String,Object> ioc = new HashMap<String,Object>();


    //handlerMapping
    //private Map<String,Method> handlerMapping = new HashMap<>();//存储url和method之间的映射关系

    private List<Handler> handlerMapping = new ArrayList<>();
    @Override
    public void init(ServletConfig config) throws ServletException {
        //读取web.xml中配置的初始化参数
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        //1.加载配置文件 springmvc.properties
        doLoadConfig(contextConfigLocation);

        //2.扫描相关的类，扫描注解
        doScan(properties.getProperty("scanPackage"));

        //3.初始化bean对象（实现IoC容器，基于注解）
        doInstance();


        //4.实现依赖注入
        doAutowired();

        //5.构造一个HandlerMapping(处理器映射器)，将配置好的url和method建立关系
        initHandlerMappping();

        System.out.println("lagou mvc 初始化完成");
        //6.等待请求进入，处理请求

    }

    //构造一个映射器

    /**
     * 手写mvc框架中最关键的环节
     * 目的：将url和method建立关联
     */
    private void initHandlerMappping() {
        if(ioc.isEmpty()) {
            return;
        }
        for(Map.Entry<String,Object> entry : ioc.entrySet()){
            //获取ioc中当前遍历对象的class类型
            Class<?> aClass = entry.getValue().getClass();
            if(!aClass.isAnnotationPresent(LgController.class)){continue;}
            String baseUrl = "";
            String[] classNames = new String[0];
            if(aClass.isAnnotationPresent(LgRequestMapping.class)){
                LgRequestMapping annotation = aClass.getAnnotation(LgRequestMapping.class);
                baseUrl = annotation.value(); //等同于/demo
            }

            if(aClass.isAnnotationPresent(LgSecurity.class)){
                LgSecurity annotation = aClass.getAnnotation(LgSecurity.class);
                if(annotation != null && !"".equals(annotation)){
                    classNames = annotation.value();
                }

            }

            
            //获取方法
            String[] methodNames = new String[0];
            Method[] methods = aClass.getMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                //方法没有标识@LgRequestMapping，就不处理
                if(!method.isAnnotationPresent(LgRequestMapping.class)){continue;}
                //如果标识，就处理
                LgRequestMapping annotation = method.getAnnotation(LgRequestMapping.class);

                //从方法上获取@LgSecurity注解
                LgSecurity security = method.getAnnotation(LgSecurity.class);
                if(security != null && !"".equals(security)){
                    methodNames = security.value();
                }




                String methodUrl = annotation.value(); //等同于/query
                String url = baseUrl + methodUrl; //计算出来的url /demo/query
                //把method所有信息及url封装为一个Handler
                Handler handler = new Handler(entry.getValue(),method, Pattern.compile(url),classNames,methodNames);

                //计算方法的参数位置信息
                //query(HttpServletRequest request, HttpServletResponse response,String name)
                Parameter[] parameters = method.getParameters();
                for (int j = 0; j < parameters.length; j++) {
                    Parameter parameter = parameters[j];
                    if(parameter.getType() == HttpServletRequest.class || parameter.getType() == HttpServletResponse.class){
                        //如果是request和response对象，那么参数名称写HttpServletRequest和HttpServletResponse
                        handler.getParamIndexMapping().put(parameter.getType().getSimpleName(),j);
                    }else{
                        //普通类型存的是类型名字
                        handler.getParamIndexMapping().put(parameter.getName(),j);
                    }
                }


                //建立url和method之间的映射关系（使用map缓存起来）
                handlerMapping.add(handler);
            }
        }

    }

    //实现依赖注入
    private void doAutowired() {

        if(ioc.isEmpty()){
            return;
        }
        //有对象再进行依赖注入处理
        //遍历ioc中所有对象，查看对象中的字段是否含有@lgAutowired注解，如果有，就维护依赖注入关系
        for(Map.Entry<String,Object> entry : ioc.entrySet()){
            //获取bean对象中的字段信息
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            //遍历判断处理
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                //如果没有@LgAutowired注解
                if(!declaredField.isAnnotationPresent(LgAutowired.class)){
                    continue;
                }
                //有@LgAutowired注解
                LgAutowired annotation = declaredField.getAnnotation(LgAutowired.class);
                String beanName = annotation.value();//需要注入的bean的id
                if("".equals(beanName.trim())){//没有配置具体的bean的id，那就需要根据当前字段类型注入（接口注入） IDemoService
                    beanName = declaredField.getType().getName();
                }
                //开启赋值
                declaredField.setAccessible(true);
                try {
                    declaredField.set(entry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    //ioc容器
    //基于classNames缓存的类的全限定类名以及反射技术完成对象创建和管理
    private void doInstance() {
        if(classNames.size() == 0){
            return;
        }

        try{
            for (int i = 0; i < classNames.size(); i++) {
                String className = classNames.get(i);//com.lagou.demo.controller.DemoController
                //反射创建对象
                Class<?> aClass = Class.forName(className);
                //区分controller和service
                if(aClass.isAnnotationPresent(LgController.class)){
                    //controller的id此处不做过多处理，不区value了，就使用首字母小写的类名作为id，保存到ioc容器
                    String simpleName = aClass.getSimpleName();  //DemoController
                    String lowerName = lowerFirst(simpleName);//demoController
                    Object o = aClass.newInstance();
                    ioc.put(lowerName,o);

                }else if(aClass.isAnnotationPresent(LgService.class)){
                    LgService annotation = aClass.getAnnotation(LgService.class);
                    //获取注解value值
                    String beanName = annotation.value();

                    //如果指定了id，就以指定的为准
                    if(!"".equals(beanName.trim())){
                        ioc.put(beanName,aClass.newInstance());
                    }else{//如果没有指定，就以首字母小写的类名为准
                        beanName = lowerFirst(aClass.getSimpleName());
                        ioc.put(beanName,aClass.newInstance());
                    }

                    //service层往往是有接口的，面向接口开发，此时在以接口名为id放入一份对象到ioc容器中
                    //便于后期根据接口类型注入
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (int j = 0; j < interfaces.length; j++) {
                        Class<?> anInterface = interfaces[j];
                        //以接口的全限定类名作为id放入ioc容器
                        ioc.put(anInterface.getName(),aClass.newInstance());
                    }
                }else{
                    continue;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    //把类名首字母小写方法
    public String lowerFirst(String str){
        char[] chars = str.toCharArray();
        if('A' <= chars[0] && chars[0] <= 'Z'){
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    //扫描类，扫描注解
    //scanPackage:com.lagou.demo
    private void doScan(String scanPackage) {

        String scanPackagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + scanPackage.replaceAll("\\.", "/");
        File pack = new File(scanPackagePath);
        File[] files = pack.listFiles();
        for (File file : files) {
            if(file.isDirectory()){ //子package
                //递归
                doScan(scanPackage+"."+file.getName());//com.lagou.demo.controller
            }else if(file.getName().endsWith(".class")){
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }
    }

    //加载配置文件
    private void doLoadConfig(String contextConfigLocation) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理请求：根据url找到对应的method方法进行调用
        //获取uri
//        String requestURI = req.getRequestURI();
        //从容器中获取到对应的反射方法
//        Method method = handlerMapping.get(requestURI);
        //反射调用，需要传入对象和参数，此处无法完成调用，没有把对象缓存起来，也没有参数
        //所以我们需要改造initHandlerMapping
//        method.invoke();


        //根据url获取到能够处理当前请求的handler（从handlerMapping中获取）
        Handler handler = getHandler(req);

        if(handler == null){
            resp.getWriter().write("404 not found");
            return;
        }
        String username = req.getParameter("name");
        String[] classNames = handler.getClassNames();
        String[] methodNames = handler.getMethodNames();
        if(classNames.length > 0 || methodNames.length > 0){
            if(!findFromArrays(classNames,username)){
                resp.getWriter().write("No controller Access!");
                return;
            }else if(!findFromArrays(methodNames,username)){
                    resp.getWriter().write("No method Access!");
                    return;
            }
        }

        //参数绑定
        //获取所有参数类型数组，这个数组的长度就是我们最后要传入的args数组的长度
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
        //根据上述数组长度穿件一个新的数组（参数数组，是要传入反射调用的）
        Object[] paramValues = new Object[parameterTypes.length];
        //以下就是为了向参数数组中存值，而且还需要保证参数的顺序和方法中形参顺序一致
        Map<String, String[]> parameterMap = req.getParameterMap();
        //遍历request中所有参数（填充除了req和resp之外的参数）
        for (Map.Entry<String,String[]> param : parameterMap.entrySet()) {
            String value = StringUtils.join(param.getValue(), ",");  //value的格式为：1,2,3以逗号隔开的形式
            //如果参数和方法中的参数匹配上了，填充数据
            if(!handler.getParamIndexMapping().containsKey(param.getKey())){
                continue;
            }
            //方法形参中有该参数，找到它的索引位置，把对应的参数放入paramValues
            Integer index = handler.getParamIndexMapping().get(param.getKey());

            paramValues[index] = value;//把前台传过来的参数值填充到对应的位置
        }

        int reqIndex = handler.getParamIndexMapping().get(HttpServletRequest.class.getSimpleName());
        int respIndex = handler.getParamIndexMapping().get(HttpServletResponse.class.getSimpleName());
        paramValues[reqIndex] = req;
        paramValues[respIndex] = resp;


        //最终调用handler的method属性
        try {
            handler.getMethod().invoke(handler.getController(),paramValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private boolean findFromArrays(String[] array, String str) {
        for (String s : array) {
            if(s.equals(str))
                return true;
        }
        return false;
    }

    private Handler getHandler(HttpServletRequest req) {

        if(handlerMapping.isEmpty()){
            return null;
        }
        String url = req.getRequestURI();
        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.getPattern().matcher(url);//判断url是否匹配请求过来的正则url
            if(!matcher.matches()){
                continue;
            }
            return handler;

        }
        return null;
    }
}
