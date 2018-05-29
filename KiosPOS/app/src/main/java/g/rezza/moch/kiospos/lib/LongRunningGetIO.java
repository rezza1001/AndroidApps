package g.rezza.moch.kiospos.lib;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
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
import cz.msebera.android.httpclient.util.EntityUtils;
import g.rezza.moch.kiospos.holder.ConfigHolder;
import g.rezza.moch.kiospos.holder.KeyValueHolder;

/**
 * Created by Rezza on 8/22/17.
 */

public class LongRunningGetIO extends AsyncTask<String, Void, String> {

    private static final String TAG = "LongRunningGetIO";
    private String url = "";
    private String ipaddress = "192.168.88.83";
    private String port = "8080";
    private ArrayList<KeyValueHolder> kvh = new ArrayList<>();
    private Context context;

    public LongRunningGetIO(Context mContext){
        ConfigHolder config = new ConfigHolder();
        config.getData(mContext);
        if (!config.ip_address.isEmpty()){
            ipaddress   = config.ip_address;
            port        = config.port;
        }
        context = mContext;
    }
    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public void setPort(String port) {
        this.port = port;
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
        HttpPost httpPost = new HttpPost("http://"+ ipaddress+":"+ port+"/"+ url);
        String text = null;
        List<NameValuePair> nameValuePair = new ArrayList<>(getKeyValue().size());
        Log.d(TAG,"Post To "+httpPost.getURI().toString());
        try {
            for (int i=0; i< getKeyValue().size(); i++){
                Log.d(TAG,"PARAM TO SEND "+ "Key "+ getKeyValue().get(i).getKey()+" | Value "+getKeyValue().get(i).getValue() );
                nameValuePair.add(new BasicNameValuePair(getKeyValue().get(i).getKey(), getKeyValue().get(i).getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity ent = response.getEntity();
            text = EntityUtils.toString(ent);
            return text;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "Request time out";
        } catch (IOException e) {
            e.printStackTrace();
            return "Request time out";
        }


    }


    protected void onPostExecute(String results) {
        if (results!=null) {
            Log.d(TAG,"TEXT RESPONSE "+ results.toString());
        }
        if (mReceiveListener != null){
            if (results != null){
                if (results.equals("Request time out")){
                    Toast.makeText(context,"Request time out", Toast.LENGTH_SHORT).show();
                }
                mReceiveListener.onReceive(results);
            }
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
