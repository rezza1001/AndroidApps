package g.rezza.moch.kiospos.Activitty;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import g.rezza.moch.kiospos.lib.PrintPic;
import g.rezza.moch.kiospos.db.Database;
import g.rezza.moch.kiospos.holder.BluethoothHolder;
import g.rezza.moch.kiospos.holder.ImageHolder;
import g.rezza.moch.kiospos.R;

public class PrintActivity extends Activity implements Runnable {
    public static final String TAG = "PrintActivity";

    public static final int REQUEST_REGISTER_PRINTER = 1;

    private ArrayList<BluetoothDevice>  mDeviceList = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter            mBluetoothAdapter;
    private ProgressDialog              mBluetoothConnectProgressDialog;
    private BluetoothDevice             bluetoothDevice;
    private BluetoothSocket             mBluetoothSocket;
    private UUID                        applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ImageView                   imvw_print_20;
    private Database mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        imvw_print_20      = (ImageView) findViewById(R.id.imvw_print_20) ;
        listener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBluetoothConnectProgressDialog = ProgressDialog.show(PrintActivity.this, "Preparing connection", "Please wait...", true, false);
        Database db = new Database(PrintActivity.this);
        ImageHolder imageHolder = ImageHolder.getData(db);
        Bitmap _bitmap = BitmapFactory.decodeByteArray(imageHolder.image,0,imageHolder.image.length);
        imvw_print_20.setImageBitmap(_bitmap);


        /*
         * BLUETHOOTH CONNECTION
         */
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices == null || pairedDevices.size() == 0) {
            showToast("No Paired Devices Found");
        } else {
            mDeviceList.addAll(pairedDevices);

        }

        if (mDB == null){
            mDB = new Database(PrintActivity.this);
        }

        boolean isFound = false;
        BluethoothHolder bt = new BluethoothHolder().getData(mDB);
        if (bt.macaddress != null){
            for (int i=0; i<mDeviceList.size(); i++){
                if (mDeviceList.get(i).getAddress().equals(bt.macaddress)){
                    bluetoothDevice                 = mDeviceList.get(i);
                    isFound = true;
                }
            }
        }

        if (isFound){
            mHandler.sendEmptyMessageDelayed(2,2000);
        }
        else {
            mBluetoothConnectProgressDialog.dismiss();
            Intent intent = new Intent(PrintActivity.this, PairingActivity.class);
            startActivityForResult(intent, REQUEST_REGISTER_PRINTER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTER_PRINTER){
            if (resultCode == -99){
                finish();
            }
            else {
                onStart();
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(mReceiver);
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e(TAG, "Exe ", e);
        }
    }

    @Override
    public void run() {
        try {
            mBluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);

        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);


            mBluetoothConnectProgressDialog.dismiss();
            PrintActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PrintActivity.this,"Could Not Connect to "+bluetoothDevice.getName(), Toast.LENGTH_SHORT).show();
                    PrintActivity.this.finish();
                }
            });
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    public void listener(){

        mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            IntentFilter filter = new IntentFilter();

            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

            registerReceiver(mReceiver, filter);

        }

    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void showUnsupported() {
        showToast("Bluetooth is unsupported by this device");
    }
    private void showEnabled() {
        showToast("Bluetooth is Ready");
    }
    private void closeSocket(BluetoothSocket nOpenSocket) {
        Log.d(TAG, "closeSocket");
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.e(TAG, "CouldNotCloseSocket");
        }
    }
    private void print(){
        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket.getOutputStream();
                    try {

                        imvw_print_20.setDrawingCacheEnabled(true);
                        Bitmap bitmap = imvw_print_20.getDrawingCache();
                        ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, _bs);

                        byte[] sendData = null;
                        PrintPic d;
                        PrintPic pg = new PrintPic();
                        pg.initCanvas(394);
                        pg.initPaint();
//                        pg.drawImage(0, 0, "/storage/emulated/0/KiosTix/Test.png");
                        pg.drawImage(0, 0, bitmap);
                        sendData = pg.printDraw();
                        os.write(sendData);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "the file isn't exists");
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Exe ", e);
                }
            }
        };
        t.start();
    }

     /*
     * BLUETHOOTH CONNECTION & LISTENER
     */

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG,"BroadcastReceiver "+ action);
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");
                    showEnabled();
                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device);
            }
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	    = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                Log.d(TAG,"state : "+ state+ " | prevState "+ prevState);
                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    showToast("Paired");
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    showToast("Unpaired");
                }
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                mBluetoothConnectProgressDialog.dismiss();
                mHandler.sendEmptyMessageDelayed(1,1000);
            }
            else if (msg.what == 1){
                mBluetoothConnectProgressDialog = ProgressDialog.show(PrintActivity.this, "Printing", "Please wait ...", true, false);
                print();
                mHandler.sendEmptyMessageDelayed(3,5000);
            }
            else if (msg.what == 2){
                try {
                    mBluetoothConnectProgressDialog.dismiss();
                    mBluetoothConnectProgressDialog = ProgressDialog.show(PrintActivity.this, "Connecting...", bluetoothDevice.getName() + " : " + bluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread   = new Thread(PrintActivity.this);
                    mBlutoothConnectThread.start();
                }catch (Exception e){
                    showToast("Error Connecting to Bluethooth "+bluetoothDevice.getName());
                    finish();
                }
            }
            else if (msg.what == 3){
                mBluetoothConnectProgressDialog.dismiss();
                finish();
            }
        }
    };
}
