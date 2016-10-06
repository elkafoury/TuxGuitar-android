package org.herac.tuxguitar.player.plugin;

import android.util.Log;

import org.herac.tuxguitar.editor.event.TGRedrawEvent;
import org.herac.tuxguitar.editor.event.TGUpdateEvent;
import org.herac.tuxguitar.event.TGEvent;
import org.herac.tuxguitar.event.TGEventException;
import org.herac.tuxguitar.event.TGEventListener;

import java.io.IOException;


/**
 * Created by mohamedelkafouri on 9/25/16.
 */

public class TGSmartlightEventListener implements TGEventListener {
    smartLight smartlight;
    public TGSmartlightEventListener(smartLight sm) {

        this.smartlight = sm;
    }

    public void processUpdateEvent(TGEvent event) {
        int type = ((Integer)event.getAttribute(TGUpdateEvent.PROPERTY_UPDATE_MODE)).intValue();
        if( type == TGUpdateEvent.SELECTION ){
            //this.songView.updateSelection();
        }
        else if( type == TGUpdateEvent.MEASURE_UPDATED ){
            //this.songView.updateMeasure(((Integer) event.getAttribute(TGUpdateMeasureEvent.PROPERTY_MEASURE_NUMBER)).intValue());
        }
        else if( type == TGUpdateEvent.SONG_UPDATED ){
           // this.songView.updateTablature();
        }
        else if( type == TGUpdateEvent.SONG_LOADED ){
//            this.songView.updateTablature();
//            this.songView.resetScroll();
//            this.songView.resetCaret();
        }
    }

    public void processRedrawEvent(TGEvent event) throws IOException {
        int type = ((Integer)event.getAttribute(TGRedrawEvent.PLUGIN_PROPERTY_REDRAW_MODE)).intValue();
        if( type == TGRedrawEvent.PLUGIN_NORMAL ){
          // this.smartlight.redraw(type);
        }
        else if( type == TGRedrawEvent.PLUGIN_PLAYING_NEW_BEAT ){
            this.smartlight.redraw(type);
        }
    }

    @Override
    public void processEvent(TGEvent event) throws TGEventException {
      //  Log.d("SmartlightEventListener",TGRedrawEvent.PLUGIN_EVENT_TYPE );
        if( TGRedrawEvent.PLUGIN_EVENT_TYPE.equals(event.getEventType()) ) {

            try {
                this.processRedrawEvent(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        else if( TGUpdateEvent.EVENT_TYPE.equals(event.getEventType()) ) {
//            Log.d("SmartlightEventListener",TGUpdateEvent.EVENT_TYPE);
//            this.processUpdateEvent(event);
//        }


    }
}
