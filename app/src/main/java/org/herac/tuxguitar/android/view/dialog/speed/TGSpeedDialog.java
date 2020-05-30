package org.herac.tuxguitar.android.view.dialog.speed;

import org.herac.tuxguitar.android.R;
import org.herac.tuxguitar.android.view.dialog.TGDialog;
import org.herac.tuxguitar.document.TGDocumentContextAttributes;
import org.herac.tuxguitar.editor.action.TGActionProcessor;
import org.herac.tuxguitar.editor.action.composition.TGChangeTempoRangeAction;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTempo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class TGSpeedDialog extends TGDialog {

    public TGSpeedDialog() {
        super();
    }

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.view_speed_dialog, null);

        final RadioGroup applyToPercentGroup = (RadioGroup) view.findViewById(R.id.percent_dlg_options_group);


        final TGSong song = getAttribute(TGDocumentContextAttributes.ATTRIBUTE_SONG);
        final int applyToPercent = song.getTempoPercent();

        this.updateRadio((RadioButton)view.findViewById(R.id.percent_dlg_options_25),25, applyToPercent);
        this.updateRadio((RadioButton)view.findViewById(R.id.percent_dlg_options_50),50, applyToPercent);
        this.updateRadio((RadioButton)view.findViewById(R.id.percent_dlg_options_75),75, applyToPercent);
        this.updateRadio((RadioButton)view.findViewById(R.id.percent_dlg_options_100),100, applyToPercent);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.tempo_dlg_speed_title);
        builder.setView(view);
        builder.setPositiveButton(R.string.global_button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                changeTempoPercent(parseApplyTo(applyToPercentGroup, applyToPercent));
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



    public int parseTempoValue(Spinner tempo) {
        return ((Integer)tempo.getSelectedItem()).intValue();
    }

    public void updateRadio(RadioButton button, Integer value, Integer selection) {
        button.setTag(Integer.valueOf(value));
        button.setChecked(selection != null && selection.equals(value));
    }

    public Integer parseApplyTo(RadioGroup radioGroup, Integer defaultValue) {
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        if( radioButtonId != -1 ) {
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonId);
            if( radioButton != null ) {
                return ((Integer)radioButton.getTag()).intValue();
            }
        }
        return defaultValue;
    }

    public void changeTempoPercent( Integer applyTo) {
        final TGSong song = getAttribute(TGDocumentContextAttributes.ATTRIBUTE_SONG);
        song.setTempoPercent(applyTo);

    }
}

