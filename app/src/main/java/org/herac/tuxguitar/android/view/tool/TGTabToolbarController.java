package org.herac.tuxguitar.android.view.tool;

import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;

public class TGTabToolbarController {

	private TGTabToolbar view;
	
	public TGTabToolbarController() {
		super();
	}
	
	public TGTabToolbar getView() {
		return view;
	}

	public void setView(TGTabToolbar view) {
		this.view = view;
	}

	public void toggleVisibility() {
		if( this.getView() != null ) {
			this.getView().toggleVisibility();
		}
	}
	
	public static TGTabToolbarController getInstance(TGContext context) {
		return TGSingletonUtil.getInstance(context, TGTabToolbarController.class.getName(), new TGSingletonFactory<TGTabToolbarController>() {
			public TGTabToolbarController createInstance(TGContext context) {
				return new TGTabToolbarController();
			}
		});
	}
}
