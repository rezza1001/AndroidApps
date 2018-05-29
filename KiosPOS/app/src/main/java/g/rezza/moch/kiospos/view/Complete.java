package g.rezza.moch.kiospos.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import g.rezza.moch.kiospos.lib.Converter;
import g.rezza.moch.kiospos.lib.MasterView;
import g.rezza.moch.kiospos.lib.Parser;
import g.rezza.moch.kiospos.Activitty.MainActivity;
import g.rezza.moch.kiospos.Activitty.PrintActivity;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.component.TextKeyValue;
import g.rezza.moch.kiospos.db.Database;
import g.rezza.moch.kiospos.holder.ImageHolder;

/**
 * Created by Rezza on 10/13/17.
 */

public class Complete extends MasterView implements View.OnClickListener{
    public static final String TAG = "Complete";

    private TextKeyValue    txkv_orderdtl_40;
    private TextKeyValue    txkv_price_41;
    private TextKeyValue    txkv_qty_42;
    private TextKeyValue    txkv_total_43;
    private TextKeyValue    txkv_status_44;
    private Button          bbtn_print_30;
    private Button          bbtn_save_31;
    private Button          bbtn_done_31;
    private ImageView       imvw_qrcode_31;
    private RelativeLayout  rvly_print_00;

    public Complete(Context context) {
        super(context);
    }

    public Complete(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_complete, this, true);
        txkv_orderdtl_40    = (TextKeyValue)    findViewById(R.id.txkv_orderdtl_40);
        txkv_price_41       = (TextKeyValue)    findViewById(R.id.txkv_price_41);
        txkv_qty_42         = (TextKeyValue)    findViewById(R.id.txkv_qty_42);
        txkv_total_43       = (TextKeyValue)    findViewById(R.id.txkv_total_43);
        txkv_status_44      = (TextKeyValue)    findViewById(R.id.txkv_status_44);
        bbtn_print_30       = (Button)          findViewById(R.id.bbtn_print_30);
        bbtn_save_31        = (Button)          findViewById(R.id.bbtn_save_31);
        bbtn_done_31        = (Button)          findViewById(R.id.bbtn_done_31);
        imvw_qrcode_31      = (ImageView)       findViewById(R.id.imvw_qrcode_31);
        rvly_print_00       = (RelativeLayout)  findViewById(R.id.rvly_print_00);

        bbtn_print_30.setOnClickListener(this);
        bbtn_save_31.setOnClickListener(this);
        bbtn_done_31.setOnClickListener(this);
    }

    public void create(JSONObject js){
        disableLogo();

        mData = js;
        disableLogo();
        try {
            Log.d(TAG, mData.toString());
            JSONObject data = mData.getJSONObject("Category");
            txkv_orderdtl_40.create("ORDER DETAILS",data.getString("value"));
            txkv_price_41.create("PRICE PER TICKETS","IDR "+ Converter.toCurrnecy(data.getString("price")));

            JSONObject data2 = mData.getJSONObject("ChooseQty");
            int mQty = data2.getInt("quantity");
            txkv_qty_42.create("QUANTITY", mQty+" Tickets");

            float price = Float.parseFloat(data.getString("price")) ;
            float total = mQty * price;
            txkv_total_43.create("SUBTOTAL","IDR "+ Converter.toCurrnecy(total + ""));
            txkv_status_44.create("STATUS","Unpaid");
            generateQrCode();

            RelativeLayout.LayoutParams layoutParams    = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            Print print = new Print(getContext(), null);
            print.create(mData);
            rvly_print_00.removeAllViews();
            rvly_print_00.addView(print, layoutParams);

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

    @Override
    public void onClick(View v) {
        if (v.equals(bbtn_print_30)){
            Activity activity   = (Activity) getContext();
            Intent intent       = new Intent(activity, PrintActivity.class);
            Bitmap _bitmap      = Parser.getBitmapFromLayout(rvly_print_00);
            ByteArrayOutputStream _bs = new ByteArrayOutputStream();
            _bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);
            Database database = new Database(activity);
            ImageHolder imageHolder = new ImageHolder();
            imageHolder.name  = "Print";
            imageHolder.image = _bs.toByteArray();
            Log.d(TAG,imageHolder.image.toString() );
            database.delete(imageHolder.TABLE_NAME,"");
            database.insert(imageHolder.TABLE_NAME, imageHolder.createContentValues());
            activity.startActivityForResult(intent, MainActivity.REQUEST_PRINT);
        }
        else if (v.equals(bbtn_save_31)){
            Parser.saveToPdf(rvly_print_00,"Transaction", getContext());
            if (mFinishListener != null){
                mFinishListener.onSave(Complete.this, mData);
            }

        }
        else {
            if (mFinishListener != null){
                mFinishListener.onNext(Complete.this, mData);
            }
        }

    }

}
