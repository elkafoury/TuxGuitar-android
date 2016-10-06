package org.herac.tuxguitar.android.action.impl.view;

import android.util.Log;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;

import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.util.TGContext;

/**
 * Created by mohamedelkafouri on 10/1/16.
 */

public class TGBluetoothAction extends TGActionBase {

    public static final String NAME = "action.bluetooth";
    public static final String ATTRIBUTE_ACTIVITY = TGActivity.class.getName();
    public TGBluetoothAction(TGContext context) {

        super(context, NAME);
    }

    protected void processAction(TGActionContext context) {
        //TODO
        // call the bluetooth activity
        TGActivity tgActivity = (TGActivity) context.getAttribute(ATTRIBUTE_ACTIVITY);
         if (tgActivity==null){
             Log.d("actionBluetooth", "rab");
         }
    }




}
