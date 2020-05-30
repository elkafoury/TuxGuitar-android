package org.herac.tuxguitar.android.view.tool;

import org.herac.tuxguitar.android.R;
import org.herac.tuxguitar.android.action.TGActionProcessorListener;
import org.herac.tuxguitar.android.action.impl.caret.TGGoDownAction;
import org.herac.tuxguitar.android.action.impl.caret.TGGoLeftAction;
import org.herac.tuxguitar.android.action.impl.caret.TGGoRightAction;
import org.herac.tuxguitar.android.action.impl.caret.TGGoUpAction;
import org.herac.tuxguitar.android.action.impl.gui.TGOpenDialogAction;
import org.herac.tuxguitar.android.action.impl.gui.TGOpenMenuAction;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.android.application.TGApplicationUtil;
import org.herac.tuxguitar.android.menu.context.TGContextMenuController;
import org.herac.tuxguitar.android.menu.context.impl.TGCompositionMenu;
import org.herac.tuxguitar.android.menu.context.impl.TGDurationMenu;
import org.herac.tuxguitar.android.view.dialog.TGDialogController;
import org.herac.tuxguitar.android.view.dialog.repeat.TGRepeatCloseDialogController;
import org.herac.tuxguitar.android.view.dialog.tempo.TGTempoDialogController;
import org.herac.tuxguitar.android.view.dialog.speed.TGSpeedDialogController;
import org.herac.tuxguitar.document.TGDocumentContextAttributes;
import org.herac.tuxguitar.editor.action.composition.TGRepeatOpenAction;
import org.herac.tuxguitar.editor.action.duration.TGDecrementDurationAction;
import org.herac.tuxguitar.editor.action.duration.TGIncrementDurationAction;
import org.herac.tuxguitar.editor.action.note.TGDeleteNoteOrRestAction;
import org.herac.tuxguitar.editor.action.note.TGInsertRestBeatAction;
import org.herac.tuxguitar.editor.action.note.TGSetNoteFretNumberAction;
import org.herac.tuxguitar.editor.action.track.TGChangeTrackSoloAction;
import org.herac.tuxguitar.editor.action.track.TGSetTrackSoloAction;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.util.TGContext;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.FrameLayout;

public class TGTabToolbar extends FrameLayout {

	public TGTabToolbar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onFinishInflate() {
		super.onFinishInflate();
		this.attachView();
		this.addListeners();
	}
	
	public void attachView() {
		TGTabToolbarController.getInstance(TGApplicationUtil.findContext(this)).setView(this);
	}
	
	public void addListeners() {
		TGContext context = this.findContext();

	//	findViewById(R.id.tab_tb_button_tempo).setOnClickListener(createContextDialogActionListener(new TGTempoDialogController()));
		findViewById(R.id.tab_tb_button_speed).setOnClickListener(createContextDialogActionListener(new TGSpeedDialogController()));

		findViewById(R.id.tab_tb_button_open_repeat).setOnClickListener(createContextActionListener(TGRepeatOpenAction.NAME));
		findViewById(R.id.tab_tb_button_close_repeat).setOnClickListener(createContextDialogActionListener(new TGRepeatCloseDialogController()));
	//  	findViewById(R.id.tab_tb_button_track_solo).setOnClickListener(createContextActionListener(TGChangeTrackSoloAction.NAME));
		findViewById(R.id.tab_tb_check_change_solo).setOnClickListener(createContextActionListener(TGChangeTrackSoloAction.NAME));

	}
	
	public TGActionProcessorListener createContextMenuActionListener(TGContextMenuController controller) {
		TGActionProcessorListener tgActionProcessor = new TGActionProcessorListener(this.findContext(), TGOpenMenuAction.NAME);
		tgActionProcessor.setAttribute(TGOpenMenuAction.ATTRIBUTE_MENU_CONTROLLER, controller);
		tgActionProcessor.setAttribute(TGOpenMenuAction.ATTRIBUTE_MENU_ACTIVITY, this.findActivity());
		return tgActionProcessor;
	}

	public TGActionProcessorListener createContextDialogActionListener(TGDialogController controller){
		TGActionProcessorListener tgActionProcessor = new TGActionProcessorListener(this.findContext(), TGOpenDialogAction.NAME);
		tgActionProcessor.setAttribute(TGOpenDialogAction.ATTRIBUTE_DIALOG_CONTROLLER, controller);
		tgActionProcessor.setAttribute(TGOpenDialogAction.ATTRIBUTE_DIALOG_ACTIVITY, this.findActivity());
		return tgActionProcessor;
	}

	public TGActionProcessorListener createContextActionListener(  String actionName        ){
		TGActionProcessorListener tgActionProcessor = new TGActionProcessorListener(this.findContext(),actionName  );
		return tgActionProcessor;
	}

	public void toggleVisibility() {
		this.setVisibility(this.getVisibility() == VISIBLE ? GONE : VISIBLE);
	}
	
	private TGActivity findActivity() {
		return (TGActivity) getContext();
	}
	
	private TGContext findContext() {
		return TGApplicationUtil.findContext(this);
	}
}
