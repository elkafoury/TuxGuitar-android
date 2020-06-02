package org.herac.tuxguitar.android.view.dialog.loop;

import android.app.Activity;
import android.util.Log;

import org.herac.tuxguitar.android.view.dialog.TGDialogContext;
import org.herac.tuxguitar.android.view.dialog.TGDialogController;
import org.herac.tuxguitar.android.view.dialog.TGDialogUtil;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;

public class TGLoopDialogController implements TGDialogController {

	@Override
	public void showDialog(Activity activity, TGDialogContext context) {
		Log.d("loopactivity", activity.toString());
		Log.d("loopcontext", context.toString());
		TGDialogUtil.showDialog(activity, new TGLoopDialog(), context);

	}
	public static TGLoopDialogController getInstance(TGContext context) {
		return TGSingletonUtil.getInstance(context, TGLoopDialogController.class.getName(), new TGSingletonFactory<TGLoopDialogController>() {
			public TGLoopDialogController createInstance(TGContext context) {
				return new TGLoopDialogController();
			}
		});
	}

}
