package com.caffeine.fleet.utils.http;

import com.caffeine.fleet.utils.general.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Author: mukthar.m@myntra.com
 * Date: 29-Nov-2017
 *
 * HttpClient extension, to perform all http client operations
 *
 */

public class TouchHttpClient {
    private static Logger LOG = LoggerFactory.getLogger(TouchHttpClient.class);

    private final String USER_AGENT = "Mozilla/5.0";
    private HttpClient CLIENT = null;
    private String URL = null;
    private int RESPONSE_CODE = 0;
    private HttpClientHeaders HEADERS = new HttpClientHeaders();
    private HttpUrlParams URL_PARAMS = new HttpUrlParams();
    private String POST_BODY = null;
    private JsonNode RESPONSE_JSON = null;
    private String RESPONSE = null;

    /** just init client builder in the constructor */
    public TouchHttpClient() {
        this.CLIENT = HttpClientBuilder.create().build();
    }

    /** setters */
    public TouchHttpClient setURL(String URL) {
        this.URL = URL;
        return this;
    }

    /** set headers */
    public TouchHttpClient setHeaders(HttpClientHeaders headers) {
        this.HEADERS = headers;
        return this;
    }

    /** set url-parameters */
    public TouchHttpClient setUrlParams(HttpUrlParams urlParams) {
        this.URL_PARAMS = urlParams;
        return this;
    }

    /** Setting post body string */
    public TouchHttpClient setPostBody(String body) {
        this.POST_BODY = body;
        return this;
    }

    public String getResponseString() {
        return this.RESPONSE;
    }

    public JsonNode getResponseJson() {
        return RESPONSE_JSON;
    }

    /** getters */
    public String getURL() { return this.URL; }

    public int getResponseStatus() { return this.RESPONSE_CODE; }

    /**
     * Send get request
     * @return Response-Code
     * @throws IOException
     */
    public int sendGetRequest() throws IOException {
        LOG.info("Sending Get() request.");

        HttpGet request = new HttpGet(this.URL);
        addHeadersToRequest(request);

        HttpResponse response = this.CLIENT.execute(request);
        this.RESPONSE_CODE = response.getStatusLine().getStatusCode();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }

        if (!result.toString().isEmpty()) {
            this.RESPONSE = result.toString();
            this.RESPONSE_JSON = JsonUtils.convertToJsonNode(result.toString());
        }

        bufferedReader.close();

        return this.RESPONSE_CODE;
    }


    /**
     * Send post request
     * @throws IOException
     */
    public int sendPostRequest() throws IOException {
        LOG.info("Sending post() request to - Url: " + this.URL);
        LOG.info("Post body: " + this.POST_BODY);

        LOG.info("============");

        HttpPost httpPost = new HttpPost(this.URL);
        addHeadersToRequest(httpPost);

        if (!this.URL_PARAMS.getUrlParams().isEmpty())
            addUrlParams();

        if (this.POST_BODY != null) {
            StringEntity entity = new StringEntity(this.POST_BODY);
            httpPost.setEntity(entity);
        }

        HttpResponse response = this.CLIENT.execute(httpPost);
        this.RESPONSE_CODE = response.getStatusLine().getStatusCode();

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            LOG.info("Line: " + line);
            result.append(line);
        }


        if (!result.toString().isEmpty()) {
            this.RESPONSE = result.toString();
            this.RESPONSE_JSON = JsonUtils.convertToJsonNode(result.toString());
        }

        return this.RESPONSE_CODE;
    }


    /**
     * Adding request headers to the request object iteratively
     * @param request - Http Post | Get
     */
    private void addHeadersToRequest(HttpGet request) {
        request.addHeader("User-Agent", USER_AGENT);

        Map<String, String> headersMap = this.HEADERS.getHeaders();
        Iterator<String> headers = headersMap.keySet().iterator();
        while (headers.hasNext()) {
            String header = headers.next();
            String headerValue = headersMap.get(header);
            request.addHeader(header, headerValue);
        }
    }


    /**
     * Adding request headers to the request object iteratively
     * @param request - Http Post | Get
     */
    private void addHeadersToRequest(HttpPost request) {
        request.addHeader("User-Agent", USER_AGENT);

        Map<String, String> headersMap = this.HEADERS.getHeaders();
        Iterator<String> headers = headersMap.keySet().iterator();
        while (headers.hasNext()) {
            String header = headers.next();
            String headerValue = headersMap.get(header);
            request.addHeader(header, headerValue);
        }
    }


    /**
     * Adding url params if required.
     * @return
     * @throws UnsupportedEncodingException
     */
    private UrlEncodedFormEntity addUrlParams() throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<>();

        Map<String, String> urlParamsMap = this.URL_PARAMS.getUrlParams();
        Iterator<String> headers = urlParamsMap.keySet().iterator();
        while (headers.hasNext()) {
            String header = headers.next();
            urlParameters.add(new BasicNameValuePair(header, urlParamsMap.get(header)));
        }

        return new UrlEncodedFormEntity(urlParameters);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------\n");
        sb.append("| Http Response Object  |\n");
        sb.append("------------------------\n");
        sb.append("URI: " + this.URL + "\n");
        sb.append("Headers:\n");

        Map<String, String> headersMap = this.HEADERS.getHeaders();
        Iterator<String> headers = headersMap.keySet().iterator();
        while (headers.hasNext()) {
            String header = headers.next();
            sb.append(header + "=" + headersMap.get(header) + "\n");
        }
        sb.append("\n");
        sb.append("ResponseCode: " + this.RESPONSE_CODE + "\n\n");

        if (this.RESPONSE_JSON != null )
            sb.append("Response: " + JsonUtils.beautifyJson(this.RESPONSE_JSON.toString()));



        return sb.toString();
    }
}
