package g.rezza.moch.kiospos.lib;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

/**
 * Created by Rezza on 8/22/17.
 */

public class LongRunningGetIO2 extends AsyncTask<String, Void, String> {

    private static final String TAG = "LongRunningGetIO2";
    private String url              = "";
    private String ipaddress        = "192.168.88.83";
    private String port             = "8080";
    private JSONObject mData        = new JSONObject();
    private Context context;

    public LongRunningGetIO2(Context mContext){
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

    public void setData(JSONObject jo){
        mData = jo;
    }

    public String getUrl() {
        return url;
    }


    protected String doInBackground(String... arg0) {

        try {
            URL url = new URL("http://"+ ipaddress+":"+ port+"/"+ this.url); //Enter URL here
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            Log.d(TAG,"SEND : "+ mData.toString());
            wr.writeBytes(mData.toString());
            wr.flush();
            wr.close();

            int responseCode = httpURLConnection.getResponseCode();
            Log.d(TAG,"RESPONSE CODE : "+ responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();

        } catch (MalformedURLException e) {
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
