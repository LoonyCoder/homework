package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Context {

    ClassLoader classLoader;

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private Map<String,Wrapper> servletMap = new ConcurrentHashMap<String,Wrapper>();

    public Wrapper getWrapper(String uri) {
        return servletMap.get(uri);
    }

    public void addWrapper(String uri,Wrapper wrapper){
        servletMap.put(uri,wrapper);
    }
}
