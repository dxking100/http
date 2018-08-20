package com.duideduo.http;
import  com.duideduo.security.SecurityUtil;

import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;


public class HttpUtil {
    public static String httpGet(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            if(response.getStatusLine().getStatusCode() == 200){
                return EntityUtils.toString(response.getEntity());
            }else{
                throw new Exception("网络异常,code:"+response.getStatusLine().getStatusCode());
            }
        } finally {
            response.close();
        }
    }

    public static String httpsGet(String url) throws Exception{

        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null,  new TrustStrategy(){

                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        return true;
                    }
                }).build();
        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();


        HttpGet httpget = new HttpGet(url);

        CloseableHttpResponse response = httpClient.execute(httpget);
        try {
            if(response.getStatusLine().getStatusCode() == 200){
                return EntityUtils.toString(response.getEntity());
            }else{
                throw new Exception("网络异常,code:"+response.getStatusLine().getStatusCode());
            }
        } finally {
            response.close();
        }
    }

    public static String httpsGetSecurity(HashMap<String,String> param,String url) throws Exception{
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null,  new TrustStrategy(){

                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        return true;
                    }
                }).build();
        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();


        String aesKey = SecurityUtil.createPassWord(0,32);
        String secParam = SecurityUtil.clientEncode(JSON.toJSONString(param),aesKey);
        HttpGet httpget = new HttpGet(url+"?sCondiction="+ URLEncoder.encode(secParam));
        System.out.println(secParam);

        CloseableHttpResponse response = httpClient.execute(httpget);
        try {
            if(response.getStatusLine().getStatusCode() == 200){
                String secResult = EntityUtils.toString(response.getEntity());
                return SecurityUtil.clientDecode(secResult,aesKey);
            }else{
                throw new Exception("网络异常,code:"+response.getStatusLine().getStatusCode());
            }
        } finally {
            response.close();
        }
    }

    public static String httpPost(HashMap<String,String> param, String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> set = param.keySet();
        Iterator<String> it = set.iterator();
        while(it.hasNext())
        {
            String key = it.next();
            nvps.add(new BasicNameValuePair(key,param.get(key)));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            if(response.getStatusLine().getStatusCode() == 200){
                return EntityUtils.toString(response.getEntity());
            }else{
                throw new Exception("网络异常,code:"+response.getStatusLine().getStatusCode());
            }
        } finally {
            response.close();
        }
    }

    public static String httpsPost(HashMap<String,String> param,String url) throws Exception {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null,  new TrustStrategy(){

                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        return true;
                    }
                }).build();
        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();


        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> set = param.keySet();
        Iterator<String> it = set.iterator();
        while(it.hasNext())
        {
            String key = it.next();
            nvps.add(new BasicNameValuePair(key,param.get(key)));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            if(response.getStatusLine().getStatusCode() == 200){
                return EntityUtils.toString(response.getEntity());
            }else{
                throw new Exception("网络异常,code:"+response.getStatusLine().getStatusCode());
            }
        } finally {
            response.close();
        }
    }
//
//    public static String httpPostSecurity(HashMap<String,String> param, String url) throws Exception {
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(url);
//        String aesKey = TTDSecurityUtil.createPassWord(0,32);
//        String secParam = TTDSecurityUtil.clientEncode(JSON.toJSONString(param),aesKey);
//        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//        //传加密参数
//        nvps.add(new BasicNameValuePair("sCondiction",secParam));
//        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
//        CloseableHttpResponse response = httpclient.execute(httpPost);
//
//        try {
//            if(response.getStatusLine().getStatusCode() == 200){
//                String secResult = EntityUtils.toString(response.getEntity());
//                return TTDSecurityUtil.clientDecode(secResult,aesKey);
//            }else{
//                throw new Exception("网络异常,code:"+response.getStatusLine().getStatusCode());
//            }
//        } finally {
//            response.close();
//        }
//    }

    public static void main(String[] args) throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("name","victor");
        String result = HttpUtil.httpsGetSecurity(param,"https://localhost:8443/sercurityHello");
        System.out.println(result);


        String resul1t = HttpUtil.httpsGet("https://localhost:8443/hello");
        System.out.println(resul1t);
    }

}


