package org.herac.tuxguitar.android.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import org.herac.tuxguitar.android.R;

public class DeviceListActivity extends AppCompatActivity {


    private static final String TAG = "DeviceListActivity";



    // declare button for launching website and textview for connection status
    Button tlbutton;
    TextView textView1;


    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "oncreate");
        setContentView(R.layout.device_list);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //***************
        checkBTState();
        Log.d(TAG, "onResume");
        textView1 = (TextView) findViewById(R.id.connecting);
        textView1.setTextSize(40);
        textView1.setText(" ");

        // Initialize array adapter for paired devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter!=null){
                // Get a set of currently paired devices and append to 'pairedDevices'
                Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

                // Add previosuly paired devices to the array
                if (pairedDevices.size() > 0) {
                    findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
                    for (BluetoothDevice device : pairedDevices) {
                        mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                } else {
                    String noDevices = getResources().getText(R.string.none_paired).toString();
                    mPairedDevicesArrayAdapter.add(noDevices);
                }
        }
        tlbutton  = (Button) findViewById(R.id.back_tg_activity);
        tlbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click


                String address = "none";
                Log.d(TAG, "...address..."+ address);
                // Make an intent to start next activity while taking an extra which is the MAC address.
                Intent i = new Intent(DeviceListActivity.this, TGActivity.class);

                i.putExtra(EXTRA_DEVICE_ADDRESS, address);
                startActivity(i);
                finish();
            }
        });


    }

    // Set up on-click listener for the list (nicked this - unsure)
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            textView1.setText("Connecting...");
            Log.d(TAG, "...connecting ...");
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Log.d(TAG, "...address..."+ address);
            // Make an intent to start next activity while taking an extra which is the MAC address.
            Intent i = new Intent(DeviceListActivity.this, TGActivity.class);
            i.putExtra(EXTRA_DEVICE_ADDRESS, address);
            startActivity(i);
            finish();
        }
    };

    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter=BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if(mBtAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }
}