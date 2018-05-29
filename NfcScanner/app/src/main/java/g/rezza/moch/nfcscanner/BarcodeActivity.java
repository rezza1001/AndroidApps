package g.rezza.moch.nfcscanner;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeActivity extends AppCompatActivity {

    private String      mData = "-1";
    private TextView txvw_sacn_format, txvw_sacn_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        txvw_sacn_format    = (TextView)     findViewById(R.id.txvw_sacn_format);
        txvw_sacn_content   = (TextView)     findViewById(R.id.txvw_sacn_content);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

    }

    @Override
    protected void onStart() {
        super.onStart();
        inTegration();
    }

    private void inTegration(){
        IntentIntegrator scanIntegrator = new IntentIntegrator(BarcodeActivity.this);
        scanIntegrator.setPrompt("Scanning ...");
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == IntentIntegrator.REQUEST_CODE ){
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                txvw_sacn_format.setText(scanFormat);
                txvw_sacn_content.setText(scanContent);
                mData = scanContent;
                sendToHome(scanContent);
            }
            else{
                Toast toast = Toast.makeText(BarcodeActivity.this, "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
                sendToHome("");
            }
        }
        else {
            sendToHome("");
            inTegration();
        }

    }

    public String getData() {
        return mData;
    }


    private void sendToHome(String data){
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("CODE", data);
        setResult(200,intent);
        finish();
    }

}
