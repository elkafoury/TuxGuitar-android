package org.herac.tuxguitar.player.plugin;


import android.util.Log;

import org.herac.tuxguitar.android.activity.TGActivity;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;




import java.util.UUID;

/**
 * Created by mohamedelkafouri on 9/27/16.
 */

public class bluetoothCommunicator {


    private Boolean bConnected = true;


    private   OutputStream output;


    private static final String TAG = "BluetoothCommunicator";
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private TGActivity activity;

    public TGActivity getActivity() {
        return activity;
    }

    public void setActivity(TGActivity activity) {
        this.activity = activity;
    }

    public bluetoothCommunicator(TGActivity activity) {


        setActivity(activity);

//        mConnectedThread =  activity.getmConnectedThread();
//        //rxtxSettingsUtil.instance().setAvailablePorts(this.ports);
//        if(mConnectedThread!= null){
//            connect();
//        }

connect();
    }


    //connect to the selected port in the combo box
    //pre: ports are already found by using the searchForPorts method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public void connect() {
//        try{
//
//          if(mConnectedThread!=null)  {
//              Boolean b = initIOStream();
//              if(b){
//                  Log.d(TAG,  "successfully initialized the io stream");
//              }else{
//                  Log.d(TAG,  "Failed to initialized the io stream");
//              }
//              setConnected(b);
//              mConnectedThread.write(64); // reset all leds
//
//              Thread.sleep(2000);
//          }else{
//              Log.d(TAG,  "come on! thread is NULLLLLLLlllllllllLLLLLLl in the first place !");
//          }
//
//        } catch( Exception e){
//
//            setConnected(false);
//        }
        activity.connectAndSend(64);
    }

    //open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
//    public Boolean initIOStream() {
//        //return value for whather opening the streams is successful or not
//        Boolean successful = false;
//
//        //
//        try {
//
//            output =mConnectedThread.getMmOutStream();
//            if(output!=null){
//                successful = true;
//                Log.d(TAG,  "hey output steam created successfully");
//
//            }
//
//
//        } catch (Exception e) {
//            successful = false;
//            e.printStackTrace();
//        }
//
//
//        return successful;
//    }



    //disconnect the serial port
    //pre: an open serial port
    //post: clsoed serial port
//    public void disconnect()
//    {
//        try {
//
//            if(output!=null){
//                output.close();
//                setConnected(false);
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//    }

//    public void reconnect() throws IOException {
//        disconnect();
//        connect();
//
//    }

    final public Boolean getConnected()
    {
        return bConnected;
    }

    public void setConnected(Boolean bConnected)
    {
        this.bConnected = bConnected;
    }

    //what happens when data is received
    //pre: serial event is triggered
    //post: processing on the data it reads


//    public void flush() throws IOException, Exception {
//        try {
//            output.flush();
//        }
//        catch (IOException e) {
//            reconnect();
//        }
//    }
    //method that can be called to send data
    //pre: open serial port
    //post: data sent to the other device
    public void writeData(int data) throws IOException {
//        String logText;
//        if (!getConnected()) {
//            Log.d(TAG,  " writeData(): not connected");
//          //  reconnect();
//            return;
//        }
//        Log.d(TAG, "writeData(): value to write "+  String.valueOf(data));
//        try
//        {
//            if(mConnectedThread!=null){
//                mConnectedThread.write(data);
//               // Thread.sleep(100);
//
//            }
//
//
//            Log.d(TAG, "writeData--> "+  String.valueOf(data));
//
//        } catch (Exception e)
//        {
//            logText = "Failed to write data. (" + e.toString() + ")";
//            Log.d(TAG,  logText);
//        }
        activity.connectAndSend(data);
    }


}
