package com.lagou.edu.mvcframework.pojo;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 封装handler方法相关的信息
 */
public class Handler {

    private Object controller; //method.invoke(obj,)
    private Method method;
    private Pattern pattern;//spring中url是支持正则的
    private String[] classNames;
    private String[] methodNames;

    public String[] getClassNames() {
        return classNames;
    }

    public void setClassNames(String[] classNames) {
        this.classNames = classNames;
    }

    public String[] getMethodNames() {
        return methodNames;
    }

    public void setMethodNames(String[] methodNames) {
        this.methodNames = methodNames;
    }

    /**
     * String:参数名
     * Integer:参数索引
     */
    private Map<String,Integer> paramIndexMapping;  //参数顺序，为了进行参数绑定


    public Handler(Object controller, Method method, Pattern pattern,String[] classNames,String[] methodNames) {
        this.classNames = classNames;
        this.methodNames = methodNames;
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        this.paramIndexMapping = new HashMap<>();
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Map<String, Integer> getParamIndexMapping() {
        return paramIndexMapping;
    }

    public void setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
        this.paramIndexMapping = paramIndexMapping;
    }
}
