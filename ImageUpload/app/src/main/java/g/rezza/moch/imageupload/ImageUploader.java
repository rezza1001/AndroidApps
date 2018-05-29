package g.rezza.moch.imageupload;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUploader extends AsyncTask<String, Void, String> {
    private static final String TAG = "ImageUploader";
    private Uri uri;
    private Context context;
    private LoadingScreen loadingScreen;
    private boolean showloading = false;
    private String sparator     = "re-001";

    public ImageUploader(Uri uri, Context pContext) {
         this.uri       = uri;
         context        = pContext;
         loadingScreen  = new LoadingScreen(pContext);
         loadingScreen.setMessage("Uploading Image...");
         showloading    = true;
    }

    public void hideLoading(){
        showloading = false;
    }


    @Override
    protected void onPreExecute() {
        if (showloading){
            loadingScreen.show();
        }
    }
    @Override
    protected String doInBackground(String... params) {
        StringBuilder result = new StringBuilder();
        try {
            String param        = params[0];
            String lineEnd      = "\r\n";
            String twoHyphens   = "--";
            String boundary     = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;
            //todo change URL as per client ( MOST IMPORTANT )
            URL url = new URL("http://imageupload.mybarber.my.id/api/image-upload");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs &amp; Outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Set HTTP method to POST.
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            FileInputStream fileInputStream;
            DataOutputStream outputStream;
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            outputStream.writeBytes("Content-Disposition: form-data; name=\"reference\""+ lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes("my_refrence_text");
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            File file = new File(getPath(uri));
            Log.d(TAG, " File Name : "+ file.getName());

            outputStream.writeBytes("Content-Disposition: form-data; name=\""+param+"\";filename=\"" + file.getName() +"\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            fileInputStream = new FileInputStream(getPath(uri));
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            Log.d(TAG, "serverResponseCode "+ serverResponseCode);

            result.append(serverResponseCode).append(sparator);
            if (serverResponseCode == 200) {
                StringBuilder s_buffer = new StringBuilder();
                InputStream is = new BufferedInputStream(connection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    s_buffer.append(inputLine);
                }
                result.append(s_buffer.toString());
            }
            else {
                result.append("Error");
            }
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();

}

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "Result : "+ result);
        loadingScreen.dimiss();
        if (mListener != null){
            int code = Integer.parseInt(result.split(sparator)[0]);
            JSONObject data = null;
            try {
                data = new JSONObject(result.split(sparator)[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mListener.onFinishUpload(code,data);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private UploadListener mListener;
    public void setOnUploadListener(UploadListener uploadListener){
        mListener = uploadListener;
    }
    public interface UploadListener{
        public void onFinishUpload(int code, JSONObject response);
    }

}
