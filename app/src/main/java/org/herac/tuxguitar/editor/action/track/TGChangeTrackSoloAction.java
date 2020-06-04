package org.herac.tuxguitar.editor.action.track;

import android.app.Activity;
import android.widget.CheckBox;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.action.TGActionManager;
import org.herac.tuxguitar.android.R;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.android.activity.TGActivityController;
import org.herac.tuxguitar.android.menu.options.TGMainMenu;
import org.herac.tuxguitar.document.TGDocumentContextAttributes;
import org.herac.tuxguitar.editor.action.TGActionBase;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.util.TGContext;

public class TGChangeTrackSoloAction extends TGActionBase {
	
	public static final String NAME = "action.track.change-solo";
	
	public TGChangeTrackSoloAction(TGContext context) {
		super(context, NAME);
	}
	
	protected void processAction(TGActionContext context){
		final TGTrack track = ((TGTrack) context.getAttribute(TGDocumentContextAttributes.ATTRIBUTE_TRACK));
		
		context.setAttribute(TGSetTrackSoloAction.ATTRIBUTE_SOLO, Boolean.valueOf(!track.isSolo()));

		TGActionManager.getInstance(getContext()).execute(TGSetTrackSoloAction.NAME, context);
// you have to run this in a thread to avoid an error
//		TGActivityController activityController = this.getContext().getAttribute("org.herac.tuxguitar.android.activity.TGActivityController");
//
//		CheckBox cb = (CheckBox) activityController.getActivity().findViewById(R.id.tab_tb_check_change_solo);
//
//		if (track.isSolo()) {
//			cb.setChecked(true);
//		} else {
//			cb.setChecked(false);
//		}

 		final TGActivityController activityController = this.getContext().getAttribute("org.herac.tuxguitar.android.activity.TGActivityController");

		Thread thread = new Thread(){
			@Override
			public void run() {
				try {
					synchronized (this) {
						wait(100);
						activityController.getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
							//	TGMainMenu.getInstance(this.getContext());
						//		.findItem(R.id.menu_check_solo)
								TGMainMenu.getInstance(getContext()).getMenu().findItem(R.id.menu_check_solo).setChecked(track.isSolo());
									final	CheckBox cb = (CheckBox) activityController.getActivity().findViewById(R.id.tab_tb_check_change_solo);
                                    if (track.isSolo()) {
                                        cb.setChecked(true);
                                    } else {
                                        cb.setChecked(false);
                                    }
							}
						});
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			};
		};
		thread.start();
	}
}
