package g.rezza.moch.hrsystem.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import g.rezza.moch.hrsystem.LoginActivity;
import g.rezza.moch.hrsystem.database.AbsentDB;
import g.rezza.moch.hrsystem.database.EmployeesDB;
import g.rezza.moch.hrsystem.database.OrganizationDB;
import g.rezza.moch.hrsystem.database.UserDB;
import g.rezza.moch.hrsystem.holder.KeyValueHolder;
import g.rezza.moch.hrsystem.lib.ErrorCode;
import g.rezza.moch.hrsystem.lib.LoadingScreen;

/**
 * Created by Rezza on 8/22/17.
 */

public class PostManager extends AsyncTask<String, Void, String> {

    private static final String TAG = "PostManager";
    private static final String SPARATOR = "~R3224~";

    private String url              = "https://mybarber.my.id/api";
    private String apiUrl           = "";
    private JSONObject mData        = new JSONObject();
    private Context context;
    private LoadingScreen loading ;

    private boolean showLoading = true;

    public PostManager(Context mContext){
        loading = new LoadingScreen(mContext);
        context = mContext;
    }

    public void showloading(boolean show){
        if (!show){
            showLoading = false;
        }
    }

    public void setApiUrl(String url) {
        this.apiUrl = url;
    }

    public void setData(JSONObject jo){
        mData = jo;
    }

    public void setData(ArrayList<KeyValueHolder> pHolders){
        UserDB user = new UserDB();
        user.getMine(context);
        if (user.id != 0){
            try {
                mData.put("user_id",""+ user.id);
                mData.put("token",""+ user.token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for(KeyValueHolder holder : pHolders){
            try {
                mData.put(holder.key, holder.value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onPreExecute() {
        if (showLoading){
            loading.show();
        }
        super.onPreExecute();
    }

    protected String doInBackground(String... arg0) {
        StringBuilder sbResponse = new StringBuilder();
        try {
            String type = arg0[0];
            URL url = new URL(this.url+"/"+ apiUrl); //Enter URL here
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(type); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.setRequestProperty("Accept", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            Log.d(TAG,type+ " "+apiUrl+ " : "+ mData.toString());
            wr.writeBytes(mData.toString());
            wr.flush();
            wr.close();

            int responseCode = httpURLConnection.getResponseCode();
            Log.d(TAG,"RESPONSE CODE : "+ responseCode);
            sbResponse.append(responseCode).append(SPARATOR);
            BufferedReader in;
            if (responseCode == ErrorCode.CODE_UNPROCESSABLE_ENTITY){
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }
            else {
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            sbResponse.append(response.toString());
            return sbResponse.toString();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, " Error "+e.getMessage());
            sbResponse.append("Request time out");
            return sbResponse.toString();
        }

    }


    protected void onPostExecute(String presults) {
        loading.dimiss();
        try {
            String results = presults.split(SPARATOR)[1];
            int code    =  Integer.parseInt(presults.split(SPARATOR)[0]);
            if (results!=null) {
                Log.d(TAG,"TEXT RESPONSE "+ results.toString() +" | CODE : "+ code);
            }
            if (mReceiveListener != null){
                if (results != null){
                    if (results.equals("Request time out")){
                        Toast.makeText(context,"Request time out", Toast.LENGTH_SHORT).show();
                        mReceiveListener.onReceive(null, ErrorCode.TIME_OUT);
                    }
                    try {
                        JSONObject  jo = new JSONObject(results);
                        int body_error_code = jo.getInt("error_code");
                        mReceiveListener.onReceive(jo, body_error_code);
                        if (body_error_code == ErrorCode.ACCESS_TOKEN){
                            goToLogin();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mReceiveListener.onReceive(null, ErrorCode.UNDIFINED_ERROR);
                    }

                }
            }
        }catch (Exception e){
            mReceiveListener.onReceive(null, ErrorCode.UNDIFINED_ERROR);
        }


    }

    private onReceiveListener mReceiveListener;
    public void setOnReceiveListener(onReceiveListener mReceiveListener){
        this.mReceiveListener = mReceiveListener;
    }
    public interface onReceiveListener{
        public void onReceive(JSONObject obj, int code);
    }

    private void goToLogin(){
        AbsentDB absentDB = new AbsentDB();
        absentDB.clearData(context);

        EmployeesDB employeesDB = new EmployeesDB();
        employeesDB.clearData(context);

        OrganizationDB organizationDB = new OrganizationDB();
//        organizationDB.clearData(context);

        UserDB userDB = new UserDB();
        userDB.clearData(context);



        Intent intent = new Intent(context, LoginActivity.class);
        Activity activity = (Activity)context;
        activity.startActivity(intent);
        activity.finish();
    }
}
