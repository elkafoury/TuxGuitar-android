package org.herac.tuxguitar.player.plugin;

import android.util.Log;

import org.herac.tuxguitar.android.TuxGuitar;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.graphics.control.TGNoteImpl;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.plugin.TGPlugin;
import org.herac.tuxguitar.util.plugin.TGPluginException;
import org.herac.tuxguitar.song.models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public  class smartLight implements TGPlugin {

    private static final String MODULE_ID = "Smartlight-plugin";
    private int beatIdx = 0;
    private TGContext context;
    private TGBeat beat;
    private TGBeat externalBeat;
    private  List lastNotes=new ArrayList();
    private bluetoothCommunicator serial;
    private byte[][]octaves= new byte[][]{
            { 0, 0, 0, 0, 0, 1 },
            { 0, 0, 0, 0, 0, 1 },
            { 0, 1, 2, 1, 0, 1 },
            { 1, 1, 2, 1, 1, 2 },
            { 1, 1, 2, 2, 1, 2 },
            { 2, 2, 3, 2, 3, 3 },
            { 2, 2, 3, 2, 3, 3 },
            { 3, 4, 4, 3, 3, 4 },
            { 3, 4, 4, 3, 4, 4 },
            { 3, 4, 5, 5, 4, 4 },
            { 4, 5, 5, 5, 5, 5 },
            { 4, 5, 5, 5, 5, 5 },
            { 6, 6, 6, 6, 6, 7 },
            { 6, 6, 6, 6, 6, 7 },
            { 6, 7, 8, 7, 6, 7 },
            { 7, 7, 8, 7, 7, 8 },
            { 7, 7, 8, 8, 7, 8 },
            { 8, 8, 9, 8, 9, 9 },
            { 8, 8, 9, 8, 9, 9 },
            { 9, 10, 10, 9, 9, 10 },
            { 9, 10, 10, 9, 10, 10 },
            { 9, 10, 11, 11, 10, 10 },
            //this is not going to be used since there is only 21 frets,
            // just to prevent error in notes above that fret
            { 9, 10, 10, 9, 10, 10 },
            { 9, 10, 11, 11, 10, 10 }
    };

    private byte[][]Notes= new byte[][]{
            { 7, 0, 5, 10,2, 7 },
            { 8, 1,6,11, 3, 8 },
            { 9,2, 7, 0, 4, 9 },
            { 10, 3, 8, 1, 5, 10 },
            { 11,4,9, 2, 6, 11 },
            { 0, 5, 10, 3, 7, 0 },
            { 1,6, 11, 4, 8, 1 },
            { 2, 7, 0, 5, 9, 2 },
            { 3, 8, 1, 6, 10, 3 },
            { 4,9, 2, 7, 11, 4 },
            { 5, 10, 3, 8, 0, 5 },
            { 6, 11, 4, 9, 1, 6 },
            { 7, 0, 5, 10, 2, 7 },
          //  { 7, 0, 5, 10,2, 7 },
            { 8, 1,6,11, 3, 8 },
            { 9,2, 7, 0, 4, 9 },
            { 10, 3, 8, 1, 5, 10 },
            { 11,4,9, 2, 6, 11 },
            { 0, 5, 10, 3, 7, 0 },
            { 1,6, 11, 4, 8, 1 },
            { 2, 7, 0, 5, 9, 2 },
            { 3, 8, 1, 6, 10, 3 },
            { 4,9, 2, 7, 11, 4 },
            { 5, 10, 3, 8, 0, 5 },
            { 6, 11, 4, 9, 1, 6 }
        //    { 7, 0, 5, 10, 2, 7 }
    };
    // Serial.write(B01000000);// reset SMARTLIGHT
    // Serial.write(64);// reset SMARTLIGHT
    // Serial.write(B01000101);// command to trun led on
    // Serial.write(B01000100);// command to trun led off

    private TGActivity activity;

    public smartLight(TGContext context) {
        Log.d("Smartlight construct",MODULE_ID);
        this.context = context;

        this.appendListeners();

        //connect bt
        if(serial==null){
            serial=new bluetoothCommunicator(getActivity());
        }

    }
    public TGActivity getActivity() {
        return activity;
    }

    public void setActivity(TGActivity activity) {
        this.activity = activity;
    }

    public void appendListeners() {
        Log.d("append Smrt listener",MODULE_ID);
        TGSmartlightEventListener listener = new TGSmartlightEventListener (this);
        TuxGuitar.getInstance(this.context).getEditorManager().addPluginRedrawListener(listener);
        setActivity(TuxGuitar.getInstance(this.context).getActivity());
    }

    public byte getOctave(int fret,int string) {
        return octaves[fret][string];
    }

    public byte getNote(int fret,int string) {
        return Notes[fret][string];
    }

    public void connect(TGContext context) throws TGPluginException {
        try {
                //init(context);
        } catch (Throwable throwable) {
            throw new TGPluginException(throwable.getMessage(),throwable);
        }
    }

    public void disconnect(TGContext context) throws TGPluginException {
        try {


        } catch (Throwable throwable) {
            throw new TGPluginException(throwable.getMessage(),throwable);
        }
    }

    public void turnOffBeats ( List lastNotes  ) throws Exception {

        int fretIndex=-1,stringIndex=-1,smStringIndex=-1;


        for (int i = 0; i < lastNotes.size(); i++) {
            TGNoteImpl note = (TGNoteImpl)lastNotes.get(i);
            fretIndex = note.getValue();
            stringIndex = note.getString() ;
            smStringIndex=6-stringIndex;
            //el traste 0 es al aire - ahora lo ignoramos
            Log.d("turning off ", "string: " + stringIndex + " fret: " + fretIndex);
            //smartlight communications
            if (serial.getConnected() != null && fretIndex!=-1) {
                Log.d("turning off ", "string: " + stringIndex + " fret: " + fretIndex);
                this.serial.writeData(68);// command to trun led off
                this.serial.writeData(getNote(fretIndex,smStringIndex ));// command to note
                this.serial.writeData(getOctave(fretIndex,smStringIndex ));// command to octave
                this.serial.writeData(64);
                Log.d("flushed OFF " ," off" );

            }

        }

    }


    private void setBeat( List lastNotes){
        try{
            if (lastNotes!=null){
                turnOffBeats(this.lastNotes);
            }


            if(TuxGuitar.getInstance(this.context).getPlayer().isRunning()){
                this.beat = TuxGuitar.getInstance(this.context).getTransport().getCache().getPlayBeat();
            }else if(this.externalBeat != null){
                //vienen de la entrada midi.
                //this.beat = this.externalBeat;
            }else{
                //?????????????  do we need to reset  if player isnt running?
                if (serial != null) {
                    this.serial.writeData(64); // reset all leds upon connecting
                }

                this.beat = TuxGuitar.getInstance(this.context).getTransport().getCache().getPlayBeat();
            }

        } catch (Exception e) {

        }
    }


    private List lightBeats(TGBeat beat) throws IOException {

        if (beat == null){
            return null;
        }

        Log.d("dumpbeats called  " , String.valueOf(this.beatIdx));
        Log.d("BEAT idx = " , String.valueOf(this.beatIdx));

        Log.d("voices:",  String.valueOf(beat.countVoices()));

        //elkafoury test
        int fretIndex=-1,stringIndex=-1,smStringIndex=-1;

        List  lastNotes =new ArrayList();
        for(int v = 0; v < beat.countVoices(); v ++){
            TGVoice voice = beat.getVoice( v );
            List n = voice.getNotes();
            int noOfNotes=n.size() ;
            Log.d("there are ", noOfNotes +"note(s) in voice "+v);
            TGNoteImpl note ;
            boolean firstNote;
            //turn off last note
            if( noOfNotes>0   ){
                lastNotes= (List) ((ArrayList) n).clone(); // register this as last note so we can turn it off later
                for (int i = 0; i < noOfNotes; i++) {
                    note = (TGNoteImpl)n.get(i);

                    fretIndex = note.getValue();

                    stringIndex = note.getString() ;
                    smStringIndex=6-stringIndex;

                    //smartlight communications
                    Log.d("lighting String " , stringIndex + " fret: " + fretIndex );
                    if (serial != null) {

                        //this.serial.writeData(GDPSerialCommunicator.NEW_LINE_ASCII);
                        Log.d("lighting String " , stringIndex + " fret: " + fretIndex );
                        this.serial.writeData(69);// command to trun led on
                        this.serial.writeData(getNote(fretIndex,smStringIndex ));// command to note
                        this.serial.writeData(getOctave(fretIndex,smStringIndex ));// command to octave

                    }
                }
            }

        }

        this.beatIdx++;
        return lastNotes;
    }

    public void redraw(int type) throws IOException {

        setBeat(this.lastNotes );
        this.lastNotes=this.lightBeats(this.beat);
    }

    public String getModuleId()  {
        return MODULE_ID;
    }
}
