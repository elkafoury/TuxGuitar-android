package org.herac.tuxguitar.android.action.impl.transport;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.document.TGDocumentContextAttributes;
import org.herac.tuxguitar.editor.action.TGActionBase;
import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.player.base.MidiPlayerMode;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.util.TGContext;

public class TGTransportSetLoopSHeaderAction extends TGActionBase {
	
	public static final String NAME = "action.transport.set-loop-start";
	
	public TGTransportSetLoopSHeaderAction(TGContext context) {
		super(context, NAME);
	}
	
	protected void processAction(TGActionContext context){
		TGMeasure measure = context.getAttribute(TGDocumentContextAttributes.ATTRIBUTE_MEASURE);
		if( measure != null ){
			MidiPlayer midiPlayer = MidiPlayer.getInstance(getContext());
			MidiPlayerMode pm = midiPlayer.getMode();
			if( pm.isLoop() ){
				pm.setLoopSHeader( pm.getLoopSHeader() != measure.getNumber() ? measure.getNumber() : -1 );
			}
		}
	}
}
