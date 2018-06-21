package g.rezza.moch.mobilesales.lib;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Rezza on 11/10/17.
 */

public class Parse {

    public static String formatTextMultiLine(String text, int maxline){
        int countline = 1;
        String value = "";
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<text.length(); i++){
            char chr = text.charAt(i);
            value = value + chr;

            if (i % maxline == 0 && i != 0) {
                sb.append(value.trim()).append(" ").append("-");
                sb.append("\n");
                value = "";
                countline ++;
            }
            else {
                sb.append(value);
                value = "";
            }
        }
        if (countline > 2){
            StringBuilder mSb = new StringBuilder();
            mSb.append(sb.toString().split("\n")[0]).append("\n");
            mSb.append(sb.toString().split("\n")[1].substring(0, maxline-1)).append("-");
            return mSb.toString();
        }
        else {
            return sb.toString();
        }

    }

    public static String formatTextTrim(String text, int maxline){
        if (text.length() > maxline){
            return text.substring(0,maxline-3)+" ...";
        }
        else {
            return text;
        }
    }

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static SpannableString underlineText(String pText){
        SpannableString content = new SpannableString(pText);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }

    public static SpannableString BoldText(String pText){
        SpannableString content = new SpannableString(pText);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        content.setSpan(boldSpan, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return content;
    }

    public static SpannableString BoldItalicText(String pText){
        SpannableString content = new SpannableString(pText);
        StyleSpan boldSpan = new StyleSpan(Typeface.ITALIC);
        content.setSpan(boldSpan, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return content;
    }


    public static int getPixelValue(Context context, int dimenId) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dimenId,
                resources.getDisplayMetrics()
        );
    }

    public static String getDateCustom(String date){
        try {
            StringBuilder sbDate = new StringBuilder();
            sbDate.append(date.split("-")[2]).append("-");
            sbDate.append(date.split("-")[1]).append("-");
            sbDate.append(date.split("-")[0]);
            return sbDate.toString();
        }catch (Exception e){
            return "";
        }

    }

    public static String getDateToServer(String date){
        try {
            StringBuilder sbDate = new StringBuilder();
            sbDate.append(date.split("-")[2]).append("-");
            sbDate.append(date.split("-")[1]).append("-");
            sbDate.append(date.split("-")[0]);
            return sbDate.toString();
        }catch (Exception e){
            return "";
        }

    }

    public static String toCurrnecy(double pData){
        try {
            NumberFormat formatKurensi = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
            String data = formatKurensi.format(pData);
            data = data.substring(1,(data.length()-3));
            return data;
        }catch (Exception e){
            return "0";
        }

    }

    public static String toCurrnecy(String pData){
        try {
            double dData = Double.parseDouble(pData);
            NumberFormat formatKurensi = NumberFormat.getCurrencyInstance(new Locale("id"));
            String data = formatKurensi.format(dData);
            data = data.substring(1,(data.length()-3));
            return data;
        }catch (Exception e){
            return "0";
        }

    }

    public static String currencyToString(String pData){
        try {
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(new Locale("id"));
            String data = pData.replaceAll("\\.","");
            return data;
        }catch (Exception e){
            Log.e("TAG",e.getMessage());
            return "0";
        }

    }

    public static String getStringNotNull(String data){
        try {
            if (data.equals("NULL")){
                return "";
            }
            else {
                return data;
            }
        }catch (Exception e){
            return "";
        }

    }

}
