package g.rezza.moch.kiospos.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import org.json.JSONException;
import org.json.JSONObject;
import g.rezza.moch.kiospos.lib.Converter;
import g.rezza.moch.kiospos.lib.MasterView;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.component.TextKeyValue;

/**
 * Created by Rezza on 10/16/17.
 */

public class Print extends MasterView {

    public static final String TAG = "Print";

    private TextKeyValue    txkv_orderdtl_40;
    private TextKeyValue    txkv_price_41;
    private TextKeyValue    txkv_qty_42;
    private TextKeyValue    txkv_total_43;
    private TextKeyValue    txkv_status_44;
    private ImageView       imvw_qrcode_31;
    private RelativeLayout  rvly_body_21;

    public Print(Context context) {
        super(context);
    }

    public Print(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_print, this, true);

        txkv_orderdtl_40    = (TextKeyValue)    findViewById(R.id.txkv_orderdtl_40);
        txkv_price_41       = (TextKeyValue)    findViewById(R.id.txkv_price_41);
        txkv_qty_42         = (TextKeyValue)    findViewById(R.id.txkv_qty_42);
        txkv_total_43       = (TextKeyValue)    findViewById(R.id.txkv_total_43);
        txkv_status_44      = (TextKeyValue)    findViewById(R.id.txkv_status_44);
        imvw_qrcode_31      = (ImageView)       findViewById(R.id.imvw_qrcode_31);
        rvly_body_21        = (RelativeLayout)  findViewById(R.id.rvly_body_21);
    }

    public void create(JSONObject js){
        mData = js;
        try {
            JSONObject data = mData.getJSONObject("Category");
            txkv_orderdtl_40.create("ORDER DETAILS",data.getString("value"));
            txkv_price_41.create("PRICE PER TICKETS","IDR "+ Converter.toCurrnecy(data.getString("price")));

            JSONObject data2 = mData.getJSONObject("ChooseQty");
            Log.d(TAG, data2.toString());
            int mQty = data2.getInt("quantity");
            txkv_qty_42.create("QUANTITY", mQty+" Tickets");

            float price = Float.parseFloat(data.getString("price")) ;
            float total = mQty * price;
            txkv_total_43.create("SUBTOTAL","IDR "+ Converter.toCurrnecy(total + ""));
            txkv_status_44.create("STATUS","Unpaid");
            generateQrCode();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void generateQrCode(){
        JSONObject joBarcode = new JSONObject();
        try {
            joBarcode.put("CUSTOMER",mData.getJSONObject("GuestInformation"));
            joBarcode.put("CATEGORY",mData.getJSONObject("Category"));
            joBarcode.put("QUANTITY",mData.getJSONObject("ChooseQty").getString("quantity"));
            joBarcode.put("ATTDZ",mData.getJSONObject("Summary").getJSONArray("guest"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            Log.d(TAG, joBarcode.toString());
            BitMatrix bitMatrix = multiFormatWriter.encode(joBarcode.toString(), BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imvw_qrcode_31.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public RelativeLayout getPrintPage(){
        return rvly_body_21;
    }
}
