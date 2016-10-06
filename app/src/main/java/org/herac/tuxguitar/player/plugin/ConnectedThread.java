package org.herac.tuxguitar.player.plugin;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import org.herac.tuxguitar.android.activity.TGActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mohamedelkafouri on 9/30/16.
 */

//create new class for connect thread
public class ConnectedThread extends Thread {
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private static final String TAG = "ConnectedThread";
    private TGActivity activity;


    private static ConnectedThread instance = null;
    protected   ConnectedThread(BluetoothSocket socket, TGActivity activity) {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            //Create I/O streams for connection
            Log.d(TAG, "...getI nputStream..");
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {

            Log.d(TAG, "...ConnectedThread const exception.."+ e.getMessage() );
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }
    public static ConnectedThread getInstance(BluetoothSocket socket, TGActivity activity) {
        if(instance == null) {
            instance = new ConnectedThread(socket, activity);
        }
        return instance;
    }


    //creation of the connect thread

    public InputStream getMmInStream() {
        return mmInStream;
    }

    public OutputStream getMmOutStream() {
        return mmOutStream;
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

//        result[0] = (byte) (i >> 24);
//        result[1] = (byte) (i >> 16);
//        result[2] = (byte) (i >> 8);
 //       result[0] = (byte) (i /*>> 0*/);



        Byte b = Byte.valueOf(i+"");
        result[0] =b;
        Log.d(TAG, "converted to "+ b);
        return result;
    }
}
