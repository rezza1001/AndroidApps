package g.rezza.moch.kiospos.lib;

import android.util.Log;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Rezza on 10/13/17.
 */

public class Converter {
    public static String TAG = "Converter";

    public static String toCurrnecy(String pData){
        try {
            Double money = Double.parseDouble(pData);
            NumberFormat formatKurensi = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
            String data = formatKurensi.format(money);
            data = data.substring(1,(data.length()-3));
            return data;
        }catch (Exception e){
            Log.e(TAG, "money "+ pData);
            Log.e(TAG, e.getMessage());
            return "0";
        }


    }
}
