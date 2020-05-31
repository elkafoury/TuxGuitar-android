package org.herac.tuxguitar.android.view.dialog.loop;

import android.app.Activity;

import org.herac.tuxguitar.android.view.dialog.TGDialogContext;
import org.herac.tuxguitar.android.view.dialog.TGDialogController;
import org.herac.tuxguitar.android.view.dialog.TGDialogUtil;

public class TGLoopDialogController implements TGDialogController {

	@Override
	public void showDialog(Activity activity, TGDialogContext context) {
        TGDialogUtil.showDialog(activity, new TGLoopDialog(), context);
	}
}
