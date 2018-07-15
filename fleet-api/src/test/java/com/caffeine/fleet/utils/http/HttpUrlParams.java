package com.caffeine.fleet.utils.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpUrlParams {
    private Map<String, String> URL_PARAMS = new HashMap<>();

    public void addParam(String key, String value) {
        this.URL_PARAMS.put(key, value);
    }

    public Map<String, String> getUrlParams() {
        return this.URL_PARAMS;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        Iterator<String> urlParams = this.URL_PARAMS.keySet().iterator();
        while (urlParams.hasNext()) {
            String param = urlParams.next();
            sb.append( param + "=" + this.URL_PARAMS.get(param) );

            if (urlParams.hasNext())
                sb.append(", ");
        }
        sb.append("}");

        return sb.toString();
    }
}
