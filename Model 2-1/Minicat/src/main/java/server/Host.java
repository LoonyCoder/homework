package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Host {
    private String appBase;

    public String getAppBase() {
        return appBase;
    }

    public void setAppBase(String appBase) {
        this.appBase = appBase;
    }

    private Map<String,Context> contextMap = new ConcurrentHashMap<>();

    public Context getContext(String contextName) {
        return contextMap.get(contextName);
    }

    public void addContext(String contextName,Context context){
        contextMap.put(contextName,context);
    }


}
