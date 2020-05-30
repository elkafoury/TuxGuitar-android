package org.herac.tuxguitar.android.view.dialog.speed;

import android.app.Activity;

import org.herac.tuxguitar.android.view.dialog.TGDialogContext;
import org.herac.tuxguitar.android.view.dialog.TGDialogController;
import org.herac.tuxguitar.android.view.dialog.TGDialogUtil;


public class TGSpeedDialogController implements TGDialogController {

	@Override
	public void showDialog(Activity activity, TGDialogContext context) {
        TGDialogUtil.showDialog(activity, new TGSpeedDialog(), context);
	}
}
