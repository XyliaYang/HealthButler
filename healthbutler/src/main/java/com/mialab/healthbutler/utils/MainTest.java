package com.mialab.healthbutler.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.*;

public class MainTest {
    private static Log log = LogFactory.getLog(MainTest.class);
    private static int connectionTimeout = 26000;
    private static int socketTimeout = 25000;
    private static String startMask = "<dt>名称：</dt>";
    private static String endMask = "<dt>规格型号：</dt>";
    /**
     * @param args
     */
    public static void main(String[] args) {
        MainTest test = new MainTest();

        HashMap<String,String> map = new HashMap<String, String>();
        map.put("Host","search.anccnet.com");
        map.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
        map.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        map.put("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//        map.put("Accept-Encoding","gzip, deflate");
        //gzip 压缩
        map.put("Access-Control-Allow-Origin","*");
        map.put("Referer","http://www.ancc.org.cn/");
        //http://www.anccnet.com/ 也行
        map.put("Connection","keep-alive");
        map.put("Access-Control-Allow-Headers","X-Requested-With");
        test.getHttp("http://search.anccnet.com/searchResult2.aspx?keyword="+args[0],map);
    }

    private void getHttp(String url,Map<String, String> headers){
        HttpGet httpGet;
        httpGet = new HttpGet(url);

        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();
                httpGet.addHeader(key, headers.get(key));
            }
        }

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);

        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        httpclient.getParams().setParameter("http.protocol.content-charset", "gb2312");
        try {
            HttpResponse httpResponse = httpclient.execute(httpGet);
            InputStream inStream =     httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"gb2312"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();
            log.info(strber.toString());
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("success");
            }
            if((line = strber.toString().trim() .replaceAll("\\s*", "")).contains(startMask)&&line.contains(endMask))
                System.out.println("名称："+line.substring(line.indexOf(startMask)+startMask.length()+4,line.indexOf(endMask)-5));
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

