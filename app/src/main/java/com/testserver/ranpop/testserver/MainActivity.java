package com.testserver.ranpop.testserver;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends ActionBarActivity {
    final String TAG_STRING = "TAG";
    int LOGIN = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //为了解决网络异常
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    public void clickLogin(View view) {
        Log.i(TAG_STRING, "Login Button Click");
        EditText namete = (EditText)findViewById(R.id.username);
        EditText passte = (EditText)findViewById(R.id.password);
        String username = namete.getText().toString();
        String passwd = passte.getText().toString();
        String urlPath = "http://www.smartcreate.net/login";

        HttpPost request = new HttpPost(urlPath);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", username));
        params.add(new BasicNameValuePair("password", passwd));
        try {
            HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            request.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() == 200){
                String str = EntityUtils.toString(response.getEntity());
                Log.i(TAG_STRING, str);
                LOGIN = 1;
                Toast.makeText(getApplicationContext(), "登录成功",
                        Toast.LENGTH_SHORT).show();
            }else{
                Log.i(TAG_STRING, "erro");
                Toast.makeText(getApplicationContext(), "登录失败",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void clickLogout(View view) {
        Log.i(TAG_STRING, "Logout Button Click");

        String urlPath = "http://www.smartcreate.net/logout";

        HttpPost request = new HttpPost(urlPath);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            request.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() == 200){
                String str = EntityUtils.toString(response.getEntity());
                Log.i(TAG_STRING, str);
                LOGIN = -1;
                Toast.makeText(getApplicationContext(), "退出成功",
                        Toast.LENGTH_SHORT).show();
            }else{
                Log.i(TAG_STRING, "erro");
                Toast.makeText(getApplicationContext(), "退出失败",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void clickGetArticle(View view) {
        Log.i(TAG_STRING, "button click get article");
        if (LOGIN != 1){
            Toast.makeText(getApplicationContext(), "未登录",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        WebView webview = (WebView)findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        String httpUrl = "http://www.smartcreate.net/u/ranpop/2015-7-3/%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%93%8D%E5%BA%94%E6%B5%8B%E8%AF%95";
        HttpGet request = new HttpGet(httpUrl);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse response = httpClient.execute(request);
            if(response.getStatusLine().getStatusCode() == 200){
                String str = EntityUtils.toString(response.getEntity());
                Toast.makeText(getApplicationContext(), "获取成功",
                        Toast.LENGTH_SHORT).show();
                webview.loadUrl(httpUrl);
                webview.setWebViewClient(new HelloWebViewClient());
                Log.i(TAG_STRING, str);
            }else{
                Toast.makeText(getApplicationContext(), "获取失败",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
