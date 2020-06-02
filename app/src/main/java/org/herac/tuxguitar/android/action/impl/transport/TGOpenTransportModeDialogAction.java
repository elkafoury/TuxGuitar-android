package org.herac.tuxguitar.android.action.impl.transport;


import android.util.Log;
import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.activity.TGActivityController;
import org.herac.tuxguitar.android.view.dialog.TGDialogContext;
import org.herac.tuxguitar.editor.action.TGActionBase;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.android.view.dialog.loop.TGLoopDialogController;
import java.util.Iterator;
import java.util.Map;

public class TGOpenTransportModeDialogAction extends TGActionBase {
//
	public static final String NAME = "action.gui.open-transport-mode-dialog";
	public static final String ATTRIBUTE_DIALOG_ACTIVITY_CONTROLLER = TGActivityController.class.getName();
	public static final String ATTRIBUTE_DIALOG_CONTROLLER = TGLoopDialogController.class.getName();


	public TGOpenTransportModeDialogAction(TGContext context) {
		super(context, NAME);
	}

	protected void processAction(final TGActionContext context) {
		TGActivityController activity = (TGActivityController)this.getContext().getAttribute(ATTRIBUTE_DIALOG_ACTIVITY_CONTROLLER);
		TGLoopDialogController tgLoopDialogController= TGLoopDialogController.getInstance(this.getContext()) ;
		tgLoopDialogController.showDialog(activity.getActivity(), createDialogContext(context));
		//Log.d("tgLoopDialogController","jjj");
	}


protected TGDialogContext createDialogContext(TGActionContext context) {
	TGDialogContext tgDialogContext = new TGDialogContext();

	Iterator<Map.Entry<String, Object>> it = context.getAttributes().entrySet().iterator();
	while( it.hasNext() ) {
		Map.Entry<String, Object> entry = it.next();
		tgDialogContext.setAttribute(entry.getKey(), entry.getValue());
	}
	return tgDialogContext;
}

}