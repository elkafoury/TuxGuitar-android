package org.herac.tuxguitar.android.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothDevice;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.herac.tuxguitar.android.R;
import org.herac.tuxguitar.android.TuxGuitar;
import org.herac.tuxguitar.android.action.impl.gui.TGBackAction;
import org.herac.tuxguitar.android.action.impl.intent.TGProcessIntentAction;

import org.herac.tuxguitar.android.drawer.TGDrawerManager;
import org.herac.tuxguitar.android.fragment.impl.TGMainFragmentController;
import org.herac.tuxguitar.android.menu.context.TGContextMenuController;
import org.herac.tuxguitar.android.navigation.TGNavigationManager;
import org.herac.tuxguitar.editor.action.TGActionProcessor;
import org.herac.tuxguitar.editor.action.file.TGLoadTemplateAction;
import org.herac.tuxguitar.util.TGContext;




public class TGActivity extends AppCompatActivity {
	private static final String TAG = "TGActivity";
	private boolean destroyed;
	private TGContext context;
	private TGContextMenuController contextMenu;
	private TGNavigationManager navigationManager;
	private TGDrawerManager drawerManager;


	//bluetooth

	private BluetoothAdapter btAdapter = null;
	private BluetoothDevice device;
	private BluetoothSocket btSocket = null;
	private ConnectedThread mConnectedThread;

	public ConnectedThread getmConnectedThread() {
		return mConnectedThread;
	}

	private StringBuilder recDataString = new StringBuilder();



	// SPP UUID service - this should work for most devices
	private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// String for MAC address
	private static String address=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "oncreate");
		super.onCreate(savedInstanceState);
		
		this.destroyed = false;
		this.attachInstance();
		this.initializeTuxGuitar();
		this.setContentView(R.layout.activity_tg);
		
		this.registerForContextMenu(findViewById(R.id.root_layout));
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		
		this.navigationManager = new TGNavigationManager(this);
		this.drawerManager = new TGDrawerManager(this);
		this.loadDefaultFragment();
		Toast.makeText(getBaseContext(), "Started the App", Toast.LENGTH_LONG).show();

		btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
		checkBTState();


	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "ondestroy");
		super.onDestroy();
		connectAndSend(64);
		this.destroyTuxGuitar();
		this.detachInstance();
		this.destroyed = true;
		address=null;
				try {
			//Don't leave Bluetooth sockets open when leaving activity
			if (btSocket != null) {
				btSocket.close();
			}
		} catch (IOException e2) {
			//insert code to deal with this
		}

	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Log.d(TAG, "onPostCreate");
		//bluetooth
		if(address==null || address.equalsIgnoreCase("none")){
			Intent dIntent = new Intent(TGActivity.this,DeviceListActivity.class);
			startActivity(dIntent);
		}

		this.connectPlugins();
		this.loadDefaultSong();
		this.drawerManager.syncState();

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		//bluetooth
//		try {
//			//Don't leave Bluetooth sockets open when leaving activity
//			if (btSocket != null) {
//				btSocket.close();
//			}
//
//		} catch (IOException e2) {
//			//insert code to deal with this
//		}

		//connectAndSend();

	}
	@Override
	public void onResume() {
 		super.onResume();
		Log.d(TAG, "onResume");
		Intent intent = getIntent();

		//Get the MAC address from the DeviceListActivty via EXTRA
		address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

		connectAndSend(64);
	}
	@Override
	public void onBackPressed() {
		this.callBackAction();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		this.setIntent(intent);
		this.callProcessIntent();
	}
	
	@Override
	public void onConfigurationChanged(Configuration configuration) {
		super.onConfigurationChanged(configuration);
		
		this.drawerManager.onConfigurationChanged(configuration);
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if( this.drawerManager.onOptionsItemSelected(item) ) {
            return true;
        }
    	
        return super.onOptionsItemSelected(item);
    }
    
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (this.contextMenu != null) {
			this.contextMenu.inflate(menu, getMenuInflater());
		}
	}

	public void openContextMenu(TGContextMenuController contextMenu) {
		this.contextMenu = contextMenu;
		this.openContextMenu(this.findViewById(R.id.root_layout));
	}
	
	public void attachInstance() {
		TGActivityController.getInstance(findContext()).setActivity(this);
	}
	
	public void detachInstance() {
		TGActivityController.getInstance(findContext()).setActivity(null);
	}
	
	public void initializeTuxGuitar() {
		TuxGuitar.getInstance(findContext()).initialize(this);
	}
	
	public void destroyTuxGuitar() {
		TuxGuitar.getInstance(findContext()).destroy();
	}
	
	public void connectPlugins() {
		TuxGuitar.getInstance(findContext()).connectPlugins();
	}
	
	public TGDrawerManager getDrawerManager() {
		return drawerManager;
	}

	public TGNavigationManager getNavigationManager() {
		return navigationManager;
	}

	public TGContext findContext() {
		if( this.context == null ) {
			this.context = new TGContext();
		}
		return context;
	}
	
	public void loadDefaultFragment() {
		this.getNavigationManager().callOpenFragment(TGMainFragmentController.getInstance(findContext()));
	}
	
	public void loadDefaultSong() {
		Intent intent = this.getIntent();
		if( intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
			this.callProcessIntent();
		} else {
			this.callLoadDefaultSong();
		}
	}
	
	public void callLoadDefaultSong() {
		TGActionProcessor tgActionProcessor = new TGActionProcessor(findContext(), TGLoadTemplateAction.NAME);
		tgActionProcessor.setAttribute(TGBackAction.ATTRIBUTE_ACTIVITY, this);
		tgActionProcessor.process();
	}
	
	public void callProcessIntent() {
		TGActionProcessor tgActionProcessor = new TGActionProcessor(findContext(), TGProcessIntentAction.NAME);
		tgActionProcessor.setAttribute(TGBackAction.ATTRIBUTE_ACTIVITY, this);
		tgActionProcessor.process();


	}
	
	public void callBackAction() {
		TGActionProcessor tgActionProcessor = new TGActionProcessor(findContext(), TGBackAction.NAME);
		tgActionProcessor.setAttribute(TGBackAction.ATTRIBUTE_ACTIVITY, this);
		tgActionProcessor.process();


	}
	
	public boolean isDestroyed() {
		return this.destroyed;
	}

	//bluetooth
	//Checks that the Android device Bluetooth is available and prompts to be turned on if off
	private void checkBTState() {

		if(btAdapter==null) {
			Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
		} else {
			if (btAdapter.isEnabled()) {

			} else {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, 1);
			}
		}
	}
	private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

		return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
		//creates secure outgoing connecetion with BT device using UUID
	}
	public void connectAndSend (int data){



		if(btAdapter!=null && address!=null && !address.equalsIgnoreCase("none")){
			device = btAdapter.getRemoteDevice(address);


			if(btSocket==null){

				try {
					btSocket = createBluetoothSocket(device);
					Log.d(TAG, "...createBluetoothSocketing...");
				} catch (IOException e) {
					Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
				}
				// Establish the Bluetooth socket connection.
				try
				{
					btSocket.connect();
					Log.d(TAG, "...connecting..");
				} catch (IOException e) {
					Toast.makeText(getBaseContext(), "Socket connect failed", Toast.LENGTH_LONG).show();
					try
					{
						Log.d(TAG, "...closing..");
						btSocket.close();

					} catch (IOException e2) {
						//insert code to deal with this
						Toast.makeText(getBaseContext(), "Socket close failed", Toast.LENGTH_LONG).show();
					}
				}

			}
			if(mConnectedThread==null){
				mConnectedThread = new ConnectedThread (btSocket);
				mConnectedThread.start();
			}


			//I send a character when resuming.beginning transmission to check device is connected
			//If it is not an exception will be thrown in the write method and finish() will be called
			mConnectedThread.write(data);

		}
	}



	//create new class for connect thread
	private class ConnectedThread extends Thread {
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		//creation of the connect thread
		public ConnectedThread(BluetoothSocket socket) {
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				//Create I/O streams for connection
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) { }

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}


		public void run() {
			byte[] buffer = new byte[256];
			int bytes;


		}
		//write method
		public void write(int input) {
			byte[] msgBuffer = toBytes(input);           //converts entered String into bytes
			try {
				Log.d(TAG, "...tring to write.."+ msgBuffer);
				mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
				// Log.d(TAG, "...mmOutStream.."+ String.valueOf(msgBuffer) );
			} catch (IOException e) {
				Log.d(TAG, "...mmOutStream  cannot write.."+ e.getMessage());
				//if you cannot write, close the application
				//  Toast.makeText(activity, "Connection Failure", Toast.LENGTH_LONG).show();
				//  activity.finish();

			}
		}

		private byte[] toBytes(int i)
		{
			byte[] result = new byte[1];



			Byte b = Byte.valueOf(i+"");
			result[0] =b;
			Log.d(TAG, "converted to "+ b);
			return result;
		}
	}

}
