package g.rezza.moch.mobilesales.Activity.OrderCheck;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.support.annotation.NonNull;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import g.rezza.moch.mobilesales.R;

public class BarcodeActivity extends AppCompatActivity {

    private Resources r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        r = getResources();
        setContentView(R.layout.activity_barcode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPemission();
    }

    private void inTegration(){
        IntentIntegrator scanIntegrator = new IntentIntegrator(BarcodeActivity.this);
        scanIntegrator.setPrompt(r.getString(R.string.scanning_barcode));
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == IntentIntegrator.REQUEST_CODE ){
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                sendToHome(scanContent);
            }
            else{
                Toast toast = Toast.makeText(BarcodeActivity.this, r.getString(R.string.data_not_found),
                        Toast.LENGTH_SHORT);
                toast.show();
                sendToHome("");
            }
        }
        else {
            sendToHome("-1");
        }

    }

    private void sendToHome(String data){
        Intent intent = new Intent(this, OrderCheckActivity.class);
        intent.putExtra("CODE", data);
        setResult(200,intent);
        finish();
    }

    private void requestPemission(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CAMERA};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else {
            inTegration();
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            inTegration();
        } else {
            sendToHome("-1");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendToHome("-1");
    }
}
