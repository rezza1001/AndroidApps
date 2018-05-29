package g.rezza.moch.nfcscanner.nfc.lib;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.EntityUtils;
import g.rezza.moch.nfcscanner.nfc.holder.KeyValueHolder;

/**
 * Created by Rezza on 8/22/17.
 */

public class LongRunningGetIO extends AsyncTask<String, Void, String> {

    private String url = "";
    private ArrayList<KeyValueHolder> kvh = new ArrayList<>();

    public LongRunningGetIO (Context mContext){

    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<KeyValueHolder> getKeyValue() {
        return kvh;
    }

    public void addParam(KeyValueHolder kv){
        kvh.add(kv);
    }

    protected String doInBackground(String... arg0) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(getUrl());
        String text = null;
        List<NameValuePair> nameValuePair = new ArrayList<>(getKeyValue().size());
        try {
            for (int i=0; i< getKeyValue().size(); i++){
                Log.d("TAGRZ","PARAM TO SEND "+ "Key "+ getKeyValue().get(i).getKey()+" | Value "+getKeyValue().get(i).getValue() );
                nameValuePair.add(new BasicNameValuePair(getKeyValue().get(i).getKey(), getKeyValue().get(i).getValue()));
            }

//            nameValuePair.add(new BasicNameValuePair("token", "e2a1074523f95dea1242a49349bbcb1e"));
//            nameValuePair.add(new BasicNameValuePair("order_key", "17062000048"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity ent = response.getEntity();
            text = EntityUtils.toString(ent);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    protected void onPostExecute(String results) {
        if (results!=null) {
            Log.d("TAGRZ","TEXT RESPONSE "+ results.toString());
        }
        if (mReceiveListener != null){
            mReceiveListener.onReceive(results);
        }

    }

    private onReceiveListener mReceiveListener;
    public void setOnReceiveListener(onReceiveListener mReceiveListener){
        this.mReceiveListener = mReceiveListener;
    }
    public interface onReceiveListener{
        public void onReceive(Object obj);
    }
}
