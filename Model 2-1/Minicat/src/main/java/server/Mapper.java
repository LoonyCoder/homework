
package server;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public final class Mapper {

    private Map<String,Host> hostMap = new ConcurrentHashMap<>();

    public Host getHost(String hostName) {
        return hostMap.get(hostName);
    }

    public void addHost(String hostName,Host host) {
        hostMap.put(hostName,host);
    }

    /**
     * 匹配，为request找到servlet
     * @param request
     * @return
     */
    public boolean map(Request request){ // /demo1/server1
        try {
            String url = request.getUrl();
            String[] split = url.split("/");
            String contextName = split[1];
            Host host = getHost(request.getHost());
            if (host == null) return false;
            Context context = host.getContext(contextName);
            request.setHostObj(host);
            if (context == null) return false;
            request.setContext(context);
            int i = url.indexOf("/", 1);
            String urlPattern = url.substring(i,url.length());
            Wrapper wrapper = context.getWrapper(urlPattern);
            if (wrapper == null) return false;
            request.setWrapper(wrapper);
            return true;
        } catch (Exception e) {
            return false;
        }

    }


}
