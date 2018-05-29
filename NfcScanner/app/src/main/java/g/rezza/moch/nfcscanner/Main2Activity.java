package g.rezza.moch.nfcscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import g.rezza.moch.nfcscanner.nfc.SummaryActivity;

public class Main2Activity extends AppCompatActivity {

    public static final int  REQUEST_NFC = 1;
    public static final int  REQUEST_QR = 2;

    private ImageView imvw_nfc;
    private ImageView imvw_qr;
    private TextView txvw_value_2;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cekmailview);

        imvw_nfc    = (ImageView) findViewById(R.id.imvw_nfc);
        imvw_qr     = (ImageView) findViewById(R.id.imvw_qr);
        txvw_value_2 = (TextView) findViewById(R.id.txvw_value_2);
        button      = (Button) findViewById(R.id.button);

        imvw_nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivityIfNeeded(intent,REQUEST_NFC);
            }
        });

        imvw_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, BarcodeActivity.class);
                startActivityIfNeeded(intent,REQUEST_QR);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, SummaryActivity.class);
                intent.putExtra("CODE",txvw_value_2.getText().toString());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NFC){
            String code = "Empty Code";
            code = data.getStringExtra("CODE");
            txvw_value_2.setText(code);
        }
        if (requestCode == REQUEST_QR){
            String code = "Empty Code";
            code = data.getStringExtra("CODE");
            txvw_value_2.setText(code);
        }
    }
}
