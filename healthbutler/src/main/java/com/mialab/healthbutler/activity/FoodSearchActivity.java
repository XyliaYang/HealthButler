package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.utils.MainTest;
import com.mialab.healthbutler.utils.NewData;

import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class FoodSearchActivity extends Activity  implements  View.OnClickListener {

    private ImageView ivSearch;
    private ImageView ivScanning;
    private EditText etEditor;
    private  ListView lvSearchList;
    private static org.apache.commons.logging.Log log = LogFactory.getLog(MainTest.class);
    private static int connectionTimeout = 26000;
    private static int socketTimeout = 25000;
    private static String startMask = "<dt>名称：</dt>";
    private static String endMask = "<dt>规格型号：</dt>";
    private  String SearchResult=null; //待搜索食物
    private List<String> arrSearchfood=new ArrayList<String>();
    private ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_food_search);
//        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.tasklistactivity));

        initView();

        initData();

    }

    private void initView() {

        ivSearch= (ImageView) findViewById(R.id.iv_search);
        ivScanning= (ImageView) findViewById(R.id.iv_scanning);
        etEditor= (EditText) findViewById(R.id.et_editor);
        lvSearchList= (ListView) findViewById(R.id.lv_searchlist);
    }

    private void initData() {
        ivSearch.setOnClickListener(this);
        ivScanning.setOnClickListener(this);


        lvSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                String itemString=adapter.getItem(position);

                Intent intent=new Intent();
                intent.putExtra("food",itemString);
                intent.setClass(FoodSearchActivity.this,FoodDisplayActivity.class);

                startActivity(intent);


            }

        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.iv_search:
                SearchResult=etEditor.getText().toString();
                if(SearchResult!=null)
                {
                    showList(SearchResult);

                }
                else
                {
    /*                new AlertDialog.Builder(FoodSearchActivity.this)
                            .setTitle("无效搜索！")
                            .create().show();*/
                }
                break;

            case R.id.iv_scanning:

                scanning();
                break;
        }


    }

    private void showList(String searchResult) {
        getDataFromServer(searchResult);
        adapter=new ArrayAdapter<String>(this,R.layout.list_adapter_searchfood,arrSearchfood);
        lvSearchList.setAdapter(adapter);

    }

    private void scanning() {
        System.out.println("scann---------------->>");
        Intent intent = new Intent(this,CaptureActivity.class);		//CaptureActivity是扫描的Activity类
        startActivityForResult(intent, 0);							//当前扫描完条码或二维码后,会回调当前类的onActivityResult方法,
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){	//判断回调
            Bundle bundle = data.getExtras();
            final String scanResult = bundle.getString("result");			//这就获取了扫描的内容了

            System.out.println(scanResult+"-------------->>>");
            System.out.println("onactiviytresult---------------->>");
//            etEditor.setText(scanResult);

            final  String url="http://search.anccnet.com/searchResult2.aspx?keyword="+scanResult;

            final HashMap<String,String> map = new HashMap<String, String>();
            map.put("Host","search.anccnet.com");
            map.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
            map.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,**/*//*;q=0.8");
            map.put("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//		        map.put("Accept-Encoding","gzip, deflate");
            //gzip 压缩
            map.put("Access-Control-Allow-Origin","*");
            map.put("Referer","http://www.ancc.org.cn/");
            //http://www.anccnet.com/ 也行
            map.put("Connection","keep-alive");
            map.put("Access-Control-Allow-Headers","X-Requested-With");


            System.out.println("thread_pre---------------->>");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("thread---------------->>");
                    getHttp("http://search.anccnet.com/searchResult2.aspx?keyword=6954041400012",map);
                }
            });

        }
    }


    private void getHttp(String url,Map<String, String> headers){
        HttpGet httpGet;
        httpGet = new HttpGet(url);

        System.out.println("gethttp---------------->>");

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
                System.out.println("success");

            }
            if((line = strber.toString().trim() .replaceAll("\\s*", "")).contains(startMask)&&line.contains(endMask))
                System.out.println("名称："+line.substring(line.indexOf(startMask)+startMask.length()+4,line.indexOf(endMask)-5));

            else
                System.out.println("wrong there---------------->>");

            etEditor.setText(line.substring(line.indexOf(startMask)+startMask.length()+4,line.indexOf(endMask)-5));

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void getDataFromServer(String searchResult) {
        // TODO Auto-generated method stub
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, "http://apix.sinaapp.com/calorie/?appkey=trailuser&name="+searchResult,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        // TODO Auto-generated method stub
                        String  result=(String)responseInfo.result;
                        Log.d("result",result+"-------------->>");
//	                      String result="";
                        parseData(result);

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // TODO Auto-generated method stub
                    }


                });

    }


    protected void parseData(String result) {
        // TODO Auto-generated method stub
        Gson gson=new Gson();
        NewData[] data=gson.fromJson(result,NewData[].class);

        arrSearchfood.clear();
        for(int i=1;i<data.length;i++){
            arrSearchfood.add("   "+data[i].Title);
        }

    }


}
