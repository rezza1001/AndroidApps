package g.rezza.moch.unileverapp.lib;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Parse {

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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

}
