package org.herac.tuxguitar.android.view.dialog.loop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.herac.tuxguitar.android.R;
import org.herac.tuxguitar.android.view.dialog.TGDialog;
import org.herac.tuxguitar.document.TGDocumentContextAttributes;
import org.herac.tuxguitar.editor.action.TGActionProcessor;
import org.herac.tuxguitar.editor.action.composition.TGChangeTempoRangeAction;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTempo;

import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.player.base.MidiPlayerMode;
import org.herac.tuxguitar.android.action.impl.transport.TGTransportModeAction;

public class TGLoopDialog extends TGDialog {
	protected Spinner customFrom;
	protected Spinner customTo;
	int fromVal = 0;
	int toVal = 0;

	public TGLoopDialog() {
		super();
	}
	
	@SuppressLint("InflateParams")
	public Dialog onCreateDialog() {
		View view = getActivity().getLayoutInflater().inflate(R.layout.view_loop_dialog, null);
		//final MidiPlayerMode mode = MidiPlayer.getInstance(this.context.getContext()).getMode();
		final MidiPlayerMode mode = MidiPlayer.getInstance(findContext() ).getMode();
		final TGSong song = getAttribute(TGDocumentContextAttributes.ATTRIBUTE_SONG);
		final TGMeasureHeader header = getAttribute(TGDocumentContextAttributes.ATTRIBUTE_HEADER);
	//	final TGMeasure measure = song.
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, createMeasuresValues());
		
		this.customFrom = (Spinner) view.findViewById(R.id.loop_dlg_sloop_value);
		customFrom.setAdapter(adapter);
		//customFrom.setSelection(adapter.getPosition(Integer.valueOf(tempo.getValue())));
		this.customFrom.setSelection(fromVal);

		this.customTo= (Spinner) view.findViewById(R.id.loop_dlg_eloop_value);
		customTo.setAdapter(adapter);
		//customTo.setSelection(adapter.getPosition(Integer.valueOf(tempo.getValue())));
		this.customTo.setSelection(toVal);

		final int applyToDefault = TGChangeTempoRangeAction.APPLY_TO_ALL;

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.loop_dlg_title);
		builder.setView(view);
		builder.setPositiveButton(R.string.global_button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.d("customfrom: ",  Integer.toString(     parseValue(customFrom)   ) );
				Log.d("customTo: ",  Integer.toString(     parseValue(customTo)   ) );
				changeLoop(song, header, parseValue(customFrom),  parseValue(customTo));
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.global_button_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		
		return builder.create();
	}
	
	public Integer[] createMeasuresValues() {
		final TGSong song = getAttribute(TGDocumentContextAttributes.ATTRIBUTE_SONG);

		int length = song.countMeasureHeaders();
		Integer[] items = new Integer[length];
		for (int i = 0; i < length; i++) {
			items[i] = Integer.valueOf(i);
		}
		return items;
	}
	
	public int parseValue(Spinner s) {
		return ((Integer)s.getSelectedItem()).intValue();
	}



	
	public void changeLoop(TGSong song, TGMeasureHeader header, Integer start, Integer end) {
	//	final MidiPlayerMode mode = MidiPlayer.getInstance(this.context.getContext()).getMode();
		final MidiPlayerMode mode = MidiPlayer.getInstance(findContext()).getMode();
		//	Integer type = (this.custom.getSelection() ? MidiPlayerMode.TYPE_CUSTOM : MidiPlayerMode.TYPE_SIMPLE );
		Integer type =  MidiPlayerMode.TYPE_SIMPLE ;
		//	Boolean loop = (type == MidiPlayerMode.TYPE_CUSTOM || (type == MidiPlayerMode.TYPE_SIMPLE && this.simpleLoop.getSelection()));
		Boolean loop = true;
		fromVal=start;
		toVal=end;
	//	TGActionProcessor tgActionProcessor = new TGActionProcessor(this.context.getContext(), TGTransportModeAction.NAME);

		TGActionProcessor tgActionProcessor = new TGActionProcessor(findContext(), TGTransportModeAction.NAME);

		tgActionProcessor.setAttribute(TGTransportModeAction.ATTRIBUTE_TYPE, type);
		tgActionProcessor.setAttribute(TGTransportModeAction.ATTRIBUTE_LOOP, loop);
		tgActionProcessor.setAttribute(TGTransportModeAction.ATTRIBUTE_SIMPLE_PERCENT, MidiPlayerMode.DEFAULT_TEMPO_PERCENT);
	//	tgActionProcessor.setAttribute(TGTransportModeAction.ATTRIBUTE_CUSTOM_PERCENT_FROM,start);
	//	tgActionProcessor.setAttribute(TGTransportModeAction.ATTRIBUTE_CUSTOM_PERCENT_TO, end);
	//	tgActionProcessor.setAttribute(TGTransportModeAction.ATTRIBUTE_CUSTOM_PERCENT_INCREMENT, end);
		tgActionProcessor.setAttribute(TGTransportModeAction.ATTRIBUTE_LOOP_S_HEADER,  start );
		tgActionProcessor.setAttribute(TGTransportModeAction.ATTRIBUTE_LOOP_E_HEADER, end );
		Log.d("TGTransportModeAction: " , Boolean.toString(loop));
		tgActionProcessor.process();

	}

}
