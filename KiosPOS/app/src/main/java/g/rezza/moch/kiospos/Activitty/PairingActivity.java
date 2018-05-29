package g.rezza.moch.kiospos.Activitty;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.adapter.DeviceListAdapter;
import g.rezza.moch.kiospos.db.Database;
import g.rezza.moch.kiospos.holder.BluethoothHolder;

public class PairingActivity extends Activity implements Runnable{
    private static final String TAG = "PairingActivity";

    private ListView                    lsvw_paired;
    private DeviceListAdapter mAdapter;
    private ProgressDialog              mProgressDlg;
    private ArrayList<BluetoothDevice>  mDeviceList = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter            mBluetoothAdapter;
    private ProgressDialog              mBluetoothConnectProgressDialog;
    private BluetoothDevice             bluetoothDevice;
    private BluetoothSocket             mBluetoothSocket;
    private Button                      bbtn_close_11;
    private UUID                        applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private Database                    mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);

        lsvw_paired     = (ListView) findViewById(R.id.lsvw_paired_10);
        mAdapter	    = new DeviceListAdapter(this);
        bbtn_close_11   = (Button)  findViewById(R.id.bbtn_close_11);

        lsvw_paired.setAdapter(mAdapter);
        mAdapter.setData(mDeviceList);

        new AlertDialog.Builder(PairingActivity.this)
                .setTitle("Pairing Confirmation")
                .setMessage("Do you really want to pairing device ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listener();
                         /*
                         * BLUETHOOTH CONNECTION
                         */
                        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                        if (pairedDevices == null || pairedDevices.size() == 0) {
                            showToast("No Paired Devices Found");
                        } else {
                            mDeviceList.addAll(pairedDevices);
                        }
                        mAdapter.notifyDataSetChanged();
                    }})
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       onBackPressed();
                    }
                }).show();

        bbtn_close_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDeviceList.clear();
        if (mBluetoothAdapter != null){
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices == null || pairedDevices.size() == 0) {
                showToast("No Paired Devices Found");
            } else {
                mDeviceList.addAll(pairedDevices);
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            unregisterReceiver(mReceiver);
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e(TAG, "Exe ", e);
        }
    }

    public void listener(){
        mAdapter.setOnConnectListenr(new DeviceListAdapter.OnConnectListener() {
            @Override
            public void onDeviceSelected(BluetoothDevice bt) {
                if (bt.getBondState() == BluetoothDevice.BOND_BONDED) {
                    try {
                        bluetoothDevice                 = bt;
                        mBluetoothConnectProgressDialog = ProgressDialog.show(PairingActivity.this, "Connecting...", bt.getName() + " : " + bt.getAddress(), true, false);
                        Thread mBlutoothConnectThread   = new Thread(PairingActivity.this);
                        mBlutoothConnectThread.start();
                    }catch (Exception e){
                        showToast("Error Connecting to Bluetooth "+bt.getName());
                    }

                } else {
                    showToast("Pairing...");
                    pairDevice(bt);
                }
            }
        });


        mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();
        mProgressDlg 		= new ProgressDialog(this);
        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mBluetoothAdapter.cancelDiscovery();
            }
        });

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
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAGRZ", "requestCode " +requestCode +" | " + resultCode );
        if (requestCode == 1000){
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices == null || pairedDevices.size() == 0) {
                showToast("No Paired Devices Found");
            } else {
                mDeviceList.addAll(pairedDevices);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void run() {
        try {
            mBluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(3);

        } catch (IOException eConnectException) {
            Log.e(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);

            mBluetoothConnectProgressDialog.dismiss();
            PairingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PairingActivity.this,"Could Not Connect to "+bluetoothDevice.getName(), Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(-99);
        PairingActivity.this.finish();
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
    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private void printStatus(){
        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket.getOutputStream();
                    String BILL = "";
                    BILL = BILL+ "\nYour device is connected to "+bluetoothDevice.getName() + "\n";
                    BILL = BILL+ "\n\n";
                    os.write(BILL.getBytes());
                    os.flush();

                    if (mBluetoothSocket != null)
                        mBluetoothSocket.close();
                } catch (Exception e) {
                    Log.e(TAG, "Exe ", e);
                }
            }
        };
        t.start();
    }

    private void insertDeviceToDB(final BluetoothDevice mBT){
        mDB = new Database(PairingActivity.this);
        new AlertDialog.Builder(PairingActivity.this)
                .setTitle("Bluetooth Confirmation")
                .setMessage("Do you really want to save bluetooth device "+ mBT.getName())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(mDB != null){
                            BluethoothHolder bt = new BluethoothHolder().getData(mDB);
                            Log.d(TAG,"INSERT DATABASE");
                            bt.macaddress = mBT.getAddress();
                            bt.name = mBT.getName();
                            bt.bondState = mBT.getBondState();
                            boolean x = mDB.insert(bt.TABLE_NAME, bt.createContentValues());
                            if (x){
                                Toast.makeText(PairingActivity.this,"Success", Toast.LENGTH_SHORT).show();
                                mHandler.sendEmptyMessageDelayed(5,1000);
                            }
                            else {
                                Toast.makeText(PairingActivity.this,"Failed to save bluetooth device", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Log.e(TAG,"DATABASE IS NULL");
                        }
                    }})
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBluetoothConnectProgressDialog.dismiss();
                    }
                }).show();

        mDB.close();
    }

     /*
     * BLUETHOOTH CONNECTION & RECEIVER
     */

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG,"BroadcastReceiver | action:  "+ action);
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();
                mProgressDlg.show();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mAdapter.setData(mDeviceList);
                mAdapter.notifyDataSetChanged();
                mProgressDlg.dismiss();

            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device);
            }
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	    = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                Log.d(TAG,"state : "+ state+ " | prevState :"+ prevState);
                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    showToast("Paired");
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    showToast("Unpaired");
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 3){
                mBluetoothConnectProgressDialog.dismiss();
                mBluetoothConnectProgressDialog = ProgressDialog.show(PairingActivity.this, "Printing Status...", "Please wait", true, false);
                mHandler.sendEmptyMessageDelayed(4,1000);
            }
            else if (msg.what == 4){
                printStatus();
                insertDeviceToDB(bluetoothDevice);
            }
            else if (msg.what == 5){
                mBluetoothConnectProgressDialog.dismiss();
                setResult(200);
                finish();
            }

        }
    };
}
