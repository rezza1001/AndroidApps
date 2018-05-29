package g.rezza.moch.kiospos.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by Rezza on 8/17/17.
 */

public class Parser {
    private static final String TAG = "RZ Parser";
    public static String toCurrnecy(double pData){
        NumberFormat formatKurensi = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        String data = formatKurensi.format(pData);
        data = data.substring(1,(data.length()-3));
        return data;
    }

    public static void saveToPdf(RelativeLayout content, String name, Context context){
        Long currentTime = Calendar.getInstance().getTimeInMillis();
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();
        File file,f = null;

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            file =new File(android.os.Environment.getExternalStorageDirectory(),"KiosTix");
            if(!file.exists())
            {
                file.mkdirs();

            }
            f = new File(file.getAbsolutePath()+"/"+ name +"_"+currentTime+".png");
        }

        try {
            FileOutputStream ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            ostream.close();
            Toast.makeText(context,"File Saved to Folder KiosTix",Toast.LENGTH_SHORT).show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromLayout(RelativeLayout content){
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();
        return bitmap;
    }

    public static String getDate(String pDate){
        String new_date = "";
        try {
            pDate = pDate.split(" ")[0];
            new_date = pDate.split("-")[2]+"/"+ pDate.split("-")[1]+"/"+ pDate.split("-")[0];
        }
        catch (Exception e){
            new_date = pDate;
            Log.e(TAG,"Error getDate "+ pDate+ " "+ e.getMessage());
        }

        return new_date;
    }

    public static String getDateServer(String pDate){
        String new_date = "";
        try {
            new_date = pDate.split("/")[2]+"-"+ pDate.split("/")[1]+"-"+ pDate.split("/")[0];
        }catch (Exception e){
            Log.e(TAG,"Error getDateServer "+ pDate+ " "+ e.getMessage());
        }

        return new_date;
    }

    

}
