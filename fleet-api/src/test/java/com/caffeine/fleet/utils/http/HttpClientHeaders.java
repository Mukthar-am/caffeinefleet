package com.caffeine.fleet.utils.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpClientHeaders {
    private Map<String, String> HEADERS = new HashMap<>();

    public void addHeader(String key, String value) {
        this.HEADERS.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return this.HEADERS;
    }

    public void removeHeaders(String key) {
        this.HEADERS.remove(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        Iterator<String> headers = this.HEADERS.keySet().iterator();
        while (headers.hasNext()) {
            String header = headers.next();
            sb.append( header + "=" + this.HEADERS.get(header) );

            if (headers.hasNext())
                sb.append(", ");
        }
        sb.append("}");

        return sb.toString();
    }
}
